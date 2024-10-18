import dev.ikm.commons.service.discovery.PluginServiceLoader;
import dev.ikm.service.discovery.loader.IkmPluginServiceLoader;

module dev.ikm.service.discovery.loader {
    requires org.slf4j;
    requires dev.ikm.jpms.eclipse.collections;
    requires dev.ikm.jpms.eclipse.collections.api;

    requires dev.ikm.commons.service.discovery;

    provides PluginServiceLoader with IkmPluginServiceLoader;
}