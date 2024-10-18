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
package dev.ikm.service.discovery.manager;

import dev.ikm.commons.service.discovery.PluggableService;
import dev.ikm.commons.service.discovery.PluginServiceLoader;
import dev.ikm.service.discovery.manager.configuration.Config;
import dev.ikm.service.discovery.manager.layers.Layers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

/**
 * The IkmServiceManager class enables provision of plugin services via jar files.
 * This manager handles loading module layers from jar files places in a specified plugins
 * directory. It provides methods to load and access the plugins.
 * <p>
 * This class follows the Singleton design pattern to ensure that only one instance of the IkmServiceManager exists.
 * Use the {@link #getInstance()} method to initialize and/or retrieve the Singleton instance.
 * <p>s
 * Use the {@link PluggableService#load(Class)} method to obtain a {@link ServiceLoader} that can be used to load plugin services
 * of a specific type.
 */
public class IkmPluginServiceManager extends ConfigBasedPluginServiceManager {
    private static final Logger LOG = LoggerFactory.getLogger(IkmPluginServiceManager.class);

    private static IkmPluginServiceManager instance;
    private PluginServiceLoader pluginServiceLoader;
    private PluggableService pluggableService;
    private Layers layers;

    private IkmPluginServiceManager() {}

    public static synchronized IkmPluginServiceManager getInstance() {
        if (instance == null) {
            instance = new IkmPluginServiceManager();
        }
        return instance;
    }

    @Override
    public void initialize(Config configuration) {
        //Build a Layers object from available jars in plugin directories (default and additional)
        layers = new Layers(configuration.defaultAndAdditionalPluginDirectories());

        //Instantiate and set custom PluggableService, if present
        configuration.pluginServicePluginDirectories().stream()
                .findFirst()
                .ifPresent(pluginServiceDirectory -> {
                    ModuleLayer moduleLayer = layers.createModuleLayer(pluginServiceDirectory.plugins());
                    ServiceLoader<PluggableService> pluggableServiceLoaderLoader = ServiceLoader.load(moduleLayer, PluggableService.class);
                    pluggableServiceLoaderLoader
                            .findFirst()
                            .ifPresent(pluggableService -> this.pluggableService = pluggableService);
                });

        //Instantiate and set custom PluginServiceLoader, if present
        configuration.pluginServiceLoaderPluginDirectories().stream()
                .findFirst()
                .ifPresent(pluginLoaderDirectory -> {
                    ModuleLayer moduleLayer = layers.createModuleLayer(pluginLoaderDirectory.plugins());
                    ServiceLoader<PluginServiceLoader> pluginServiceLoaderLoader = ServiceLoader.load(moduleLayer, PluginServiceLoader.class);
                    pluginServiceLoaderLoader
                            .findFirst()
                            .ifPresent(pluginServiceLoader -> this.pluginServiceLoader = pluginServiceLoader);
                    //Set discovered PluginServiceLoader as ServiceLoader to be used by PluggableService
                    pluggableService.setPluginServiceLoader(pluginServiceLoader);
                });
    }

    //TODO-aks8m: Add directory watch functionality to configured Plugin Directories

    @Override
    public PluggableService pluggableService() {
        return pluggableService;
    }
}
