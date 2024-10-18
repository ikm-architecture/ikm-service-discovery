package dev.ikm.service.discovery.manager.configuration;

@FunctionalInterface
public interface PluginDirectoryConsumer {
    void accept(PluginDirectory pluginDirectory);
}
