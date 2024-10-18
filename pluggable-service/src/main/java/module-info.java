import dev.ikm.commons.service.discovery.PluggableService;
import dev.ikm.service.discovery.pluggable.IkmPluggableService;

module dev.ikm.service.discovery.pluggable {
    requires org.slf4j;
    requires dev.ikm.commons.service.discovery;

    provides PluggableService with IkmPluggableService;
}