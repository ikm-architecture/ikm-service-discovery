import dev.ikm.commons.service.discovery.PluggableService;
import dev.ikm.commons.service.discovery.PluginServiceLoader;

module dev.ikm.service.discovery.manager {
    requires org.slf4j;
    requires dev.ikm.commons.service.discovery;

    exports dev.ikm.service.discovery.manager;
    exports dev.ikm.service.discovery.manager.configuration;

    uses PluggableService;
    uses PluginServiceLoader;
}