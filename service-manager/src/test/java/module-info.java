import dev.ikm.commons.service.discovery.PluggableService;
import dev.ikm.commons.service.discovery.PluginServiceLoader;

module dev.ikm.service.discovery.manager.test {
    requires dev.ikm.jpms.eclipse.collections;
    requires dev.ikm.jpms.eclipse.collections.api;
    requires dev.ikm.service.discovery.manager;

    requires org.junit.jupiter.api;
    requires dev.ikm.commons.service.discovery;

    requires dev.ikm.service.discovery.example;

    exports dev.ikm.service.discovery.manager.test;

    uses PluggableService;
    uses PluginServiceLoader;
}