package dev.ikm.service.discovery.manager.configuration;

import java.util.HashSet;
import java.util.Set;

public class Config {

    public static Builder builder() {
        return new Builder();
    }

    public final static class Builder {

        private final Set<PluginDirectory> pluginDirectories = new HashSet<>();

        private Builder() {}

        public Builder addDirectory(PluginDirectoryConsumer pluginDirectoryConsumer) {
            PluginDirectory pluginDirectory = new PluginDirectory();
            pluginDirectoryConsumer.accept(pluginDirectory);

            pluginDirectory.discoverPlugins();

            this.pluginDirectories.add(pluginDirectory);
            return this;
        }

        public Config build() {
            return new Config(this.pluginDirectories);
        }
    }

    private final Set<PluginDirectory> pluginDirectories;

    public Config(Set<PluginDirectory> pluginDirectories) {
        this.pluginDirectories = pluginDirectories;
    }

    public Set<PluginDirectory> pluginDirectories() {
        return pluginDirectories;
    }

    public Set<PluginDirectory> defaultPluginDirectories() {
        return createPluginSetByDirectoryType(DirectoryType.DEFAULT_PLUGINS);
    }

    public Set<PluginDirectory> additionalPluginDirectories() {
        return createPluginSetByDirectoryType(DirectoryType.ADDITIONAL_PLUGINS);
    }

    public Set<PluginDirectory> pluginServiceLoaderPluginDirectories() {
        return createPluginSetByDirectoryType(DirectoryType.SERVICE_LOADER);
    }

    public Set<PluginDirectory> pluginServicePluginDirectories() {
        return createPluginSetByDirectoryType(DirectoryType.PLUGGABLE_SERVICE);
    }

    public Set<PluginDirectory> defaultAndAdditionalPluginDirectories() {
        Set<PluginDirectory> defaultAndAdditionalPluginDirectories = new HashSet<>();
        defaultAndAdditionalPluginDirectories.addAll(defaultPluginDirectories());
        defaultAndAdditionalPluginDirectories.addAll(additionalPluginDirectories());
        return defaultAndAdditionalPluginDirectories;
    }

    private Set<PluginDirectory> createPluginSetByDirectoryType(DirectoryType directoryType) {
        Set<PluginDirectory> pluginDirectoriesByType = new HashSet<>();
        pluginDirectories
                .stream()
                .filter(pluginDirectory -> pluginDirectory.type() == directoryType)
                .forEach(pluginDirectoriesByType::add);
        return pluginDirectoriesByType;
    }
}
