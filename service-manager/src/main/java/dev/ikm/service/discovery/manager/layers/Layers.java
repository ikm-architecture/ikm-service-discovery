/*
 * Copyright © 2015 Integrated Knowledge Management (support@ikm.dev)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dev.ikm.service.discovery.manager.layers;

import dev.ikm.service.discovery.manager.configuration.PluginDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.module.Configuration;
import java.lang.module.ModuleFinder;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * The Layers class represents a system of module layers used to manage plugins in an application.
 * It provides functionality for setting up and configuring the layers, deploying plugins, and handling
 * directory change events.
 */
public class Layers {
    private static final Logger LOG = LoggerFactory.getLogger(Layers.class);
    public final String BOOT_LAYER = "boot-layer";
    private final List<ModuleLayer> PLUGIN_PARENT_LAYER_AS_LIST = List.of(ModuleLayer.boot());

    /**
     * The actual module layers by name.
     */
    private final CopyOnWriteArraySet<Layer> layers = new CopyOnWriteArraySet<>();
    private final Layer bootLayer = new Layer(BOOT_LAYER, ModuleLayer.boot());

    /**
     * Creates a new instance of Layers.
     *
     * @param pluginsDirectories a set of PluginsDirectory objects representing the directories where plugins are stored
     */
    public Layers(Set<PluginDirectory> pluginsDirectories) {
        this.layers.add(bootLayer);
        pluginsDirectories.forEach(this::createLayer);
    }

    public List<ModuleLayer> getLayers() {
        return layers.stream().map(Layer::moduleLayer).toList();
    }

    /**
     * Handles the pluginDirectory component by creating module layer for each pluginDirectory artifact found in the directory.
     *
     * @param pluginDirectory the pluginDirectory object representing the pluginDirectory component
     * @return a map of pluginDirectory names and associated module layers
     * @throws IOException if an I/O error occurs while handling the pluginDirectory component
     */
    private void createLayer(PluginDirectory pluginDirectory) {
        ModuleLayer pluginLayer = createModuleLayer(PLUGIN_PARENT_LAYER_AS_LIST, pluginDirectory.plugins());
        Layer layer = new Layer(pluginDirectory.name(), pluginLayer);
        layers.add(layer);
    }

    /**
     * Creates a module layer with the given parent layers and module path entries.
     *
     * @param parentLayers      the list of parent module layers
     * @param modulePathEntries the list of module path entries
     * @return the created module layer
     */
    private ModuleLayer createModuleLayer(List<ModuleLayer> parentLayers, List<Path> modulePathEntries) {
        ClassLoader scl = ClassLoader.getSystemClassLoader();
        ModuleFinder finder = ModuleFinder.of(modulePathEntries.toArray(Path[]::new));

        Set<String> roots = finder.findAll()
                .stream()
                .map(m -> m.descriptor().name())
                .collect(Collectors.toSet());

        Configuration appConfig = Configuration.resolve(
                finder,
                parentLayers.stream().map(ModuleLayer::configuration).collect(Collectors.toList()),
                ModuleFinder.of(),
                roots);

        return ModuleLayer.defineModulesWithOneLoader(appConfig, parentLayers, scl).layer();
    }

    /**
     * Creates a module layer with the parent layers that implicitly exist based on Layers object creation
     *
     * @param modulePathEntries the list of module path entries
     * @return the created module layer
     */
    public ModuleLayer createModuleLayer(List<Path> modulePathEntries) {
        return createModuleLayer(getLayers(), modulePathEntries);
    }

    public void clear() {
        this.layers.clear();
    }
}
