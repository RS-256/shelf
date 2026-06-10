package com.pug523.shelf.config;

import com.electronwill.nightconfig.core.serde.ObjectDeserializer;
import com.electronwill.nightconfig.core.serde.ObjectSerializer;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.util.function.Supplier;

public class TomlConfigManager<T> implements ConfigManager<T> {

    private final Class<T> configClass;
    private final File configFile;
    private final Supplier<T> defaultSupplier;

    private final ObjectSerializer serializer = ObjectSerializer.standard();
    private final ObjectDeserializer deserializer = ObjectDeserializer.standard();

    private T config;

    public TomlConfigManager(Class<T> configClass, File configFile, Supplier<T> defaultSupplier) {
        this.configClass = configClass;
        this.configFile = configFile;
        this.defaultSupplier = defaultSupplier;
        this.config = defaultSupplier.get();
    }

    public TomlConfigManager(Class<T> configClass, String configDirectory, String fileName, Supplier<T> defaultSupplier) {
        this(configClass, resolveConfigFile(configDirectory, fileName), defaultSupplier);
    }

    private static File resolveConfigFile(String dir, String file) {
        return FabricLoader.getInstance().getConfigDir()
            .resolve(dir)
            .resolve(file)
            .toFile();
    }

    @Override
    public T getConfig() {
        return config;
    }

    @Override
    public void load() {
        if (!configFile.exists()) {
            config = defaultSupplier.get();
            save();
            return;
        }

        try (CommentedFileConfig fileConfig =
                     CommentedFileConfig.builder(configFile).build()) {

            fileConfig.load();

            config = defaultSupplier.get();
            deserializer.deserializeFields(fileConfig, config);

        } catch (Exception e) {
            e.printStackTrace();
            config = defaultSupplier.get();
            save();
        }
    }

    @Override
    public void save() {
        File parent = configFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (CommentedFileConfig fileConfig =
                     CommentedFileConfig.builder(configFile).build()) {

            serializer.serializeFields(config, fileConfig);
            fileConfig.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
