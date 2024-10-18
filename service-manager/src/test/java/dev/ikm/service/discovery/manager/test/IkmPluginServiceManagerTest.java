package dev.ikm.service.discovery.manager.test;

import dev.ikm.service.discovery.example.MyExampleService;
import dev.ikm.service.discovery.manager.IkmPluginServiceManager;
import dev.ikm.service.discovery.manager.configuration.Config;
import dev.ikm.service.discovery.manager.configuration.DirectoryType;
import org.junit.jupiter.api.*;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IkmPluginServiceManagerTest {


    @BeforeEach
    public void setUp() {
        final Path plugins = Path.of(System.getProperty("user.dir")).resolve("target/plugins");
        final Config configuration = Config.builder()
                .addDirectory(pluginDirectory -> pluginDirectory
                        .name("Service Loader")
                        .directory(plugins.resolve("service-loader"))
                        .type(DirectoryType.SERVICE_LOADER))
                .addDirectory(pluginDirectory -> pluginDirectory
                        .name("Pluggable Service")
                        .directory(plugins.resolve("pluggable-service"))
                        .type(DirectoryType.PLUGGABLE_SERVICE))
                .addDirectory(pluginDirectory -> pluginDirectory
                        .name("Default Plugins")
                        .directory(plugins.resolve("default-plugins"))
                        .type(DirectoryType.DEFAULT_PLUGINS))
                .build();

        IkmPluginServiceManager.getInstance().initialize(configuration);
    }

    @Test
    @Order(1)
    public void getPluggableServiceTest() {
        assertNotNull(IkmPluginServiceManager.getInstance().pluggableService());
    }


    @Test
    @Order(2)
    public void firstPluginDiscoverTest() {
        assertNotNull(IkmPluginServiceManager
                .getInstance()
                .pluggableService()
                .first(MyExampleService.class));
    }

    @Disabled
    @Test
    public void forNamePluginDiscoverTest() throws ClassNotFoundException {
        assertNotNull(IkmPluginServiceManager
                .getInstance()
                .pluggableService()
                .forName("MyExampleService"));
    }

    @Disabled
    @Test
    public void loadPluginDiscoverTest() {
        IkmPluginServiceManager
                .getInstance()
                .pluggableService()
                .load(null);
    }


}
