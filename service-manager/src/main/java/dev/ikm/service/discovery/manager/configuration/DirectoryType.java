package dev.ikm.service.discovery.manager.configuration;

public enum DirectoryType {
    SERVICE_LOADER("service-loader"),
    PLUGGABLE_SERVICE("pluggable-service"),
    DEFAULT_PLUGINS("default-plugins"),
    ADDITIONAL_PLUGINS("additional-plugins");

    private String label;

    DirectoryType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
