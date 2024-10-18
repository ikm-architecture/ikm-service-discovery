package dev.ikm.service.discovery.manager;

import dev.ikm.commons.service.discovery.PluginServiceManager;
import dev.ikm.service.discovery.manager.configuration.Config;

public abstract class ConfigBasedPluginServiceManager implements PluginServiceManager {

    public abstract void initialize(Config configuration);
}
