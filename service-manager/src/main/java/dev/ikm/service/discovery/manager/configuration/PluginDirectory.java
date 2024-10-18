package dev.ikm.service.discovery.manager.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class PluginDirectory implements Comparable<Path> {

    private String name;
    private Path directory;
    private DirectoryType type;
    private final List<Path> plugins = new ArrayList<>();

    public PluginDirectory name(String name) {
        this.name = name;
        return this;
    }

    public PluginDirectory directory(Path directory) {
        this.directory = directory;
        return this;
    }

    public PluginDirectory type(DirectoryType type) {
        this.type = type;
        return this;
    }

    public String name() {
        return this.name;
    }

    public Path directory() {
        return this.directory;
    }

    public DirectoryType type() {
        return this.type;
    }

    public List<Path> plugins() {
        return this.plugins;
    }

    protected void discoverPlugins() {
        try (Stream<Path> fileStream = Files.walk(directory)) {
            plugins.addAll(fileStream.filter(path -> path.toString().endsWith(".jar")).toList());
        } catch (IOException e) {
            throw new RuntimeException("Error finding plugins in directory " + directory , e);
        }
    }

    @Override
    public int compareTo(Path otherDirectory) {
        return otherDirectory.compareTo(this.directory);
    }
}
