package com.pug523.shelf.config;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.function.Supplier;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.serde.ObjectDeserializer;
import com.electronwill.nightconfig.core.serde.ObjectDeserializerBuilder;
import com.electronwill.nightconfig.core.serde.ObjectSerializer;
import com.electronwill.nightconfig.core.serde.ObjectSerializerBuilder;
import com.pug523.shelf.Shelf;
import com.pug523.shelf.compat.BuiltinRegistriesCompat;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class TomlConfigManager<T extends Serializable> implements IConfigManager<T> {

    private final Class<T> configClass;
    private final File configFile;
    private final Supplier<T> defaultSupplier;

    private final ObjectSerializer serializer = createCustomSerializer();
    private final ObjectDeserializer deserializer = createCustomDeserializer();

    private final Migrator migrator;

    private T config;

    public TomlConfigManager(Class<T> configClass, File configFile, Supplier<T> defaultSupplier, Migrator migrator) {
        this.configClass = configClass;
        this.configFile = configFile;
        this.defaultSupplier = defaultSupplier;
        this.migrator = migrator;
        this.config = defaultSupplier.get();
    }

    private static ObjectSerializer createCustomSerializer() {
        ObjectSerializerBuilder builder = ObjectSerializer.builder();

        builder.withSerializerForClass(Item.class, (value, context) -> {
            if (value == null || value == Items.AIR) {
                return "minecraft:air";
            }
            return BuiltinRegistriesCompat.ITEM.getKey(value).toString();
        });

        builder.withSerializerProvider((valueClass, context) -> {
            if (valueClass == null) {
                return (value, ctx) -> null;
            }
            return null;
        });

        return builder.build();
    }

    private static ObjectDeserializer createCustomDeserializer() {
        ObjectDeserializerBuilder builder = ObjectDeserializer.builder();

        // Double (TOML float) -> Float
        builder.withDeserializerForClass(Double.class, float.class, (value, constraint, context) -> value.floatValue());
        builder.withDeserializerForClass(Double.class, Float.class, (value, constraint, context) -> value.floatValue());

        // Long (TOML int) -> Integer
        builder.withDeserializerForClass(Long.class, int.class, (value, constraint, context) -> value.intValue());
        builder.withDeserializerForClass(Long.class, Integer.class, (value, constraint, context) -> value.intValue());

        // Long (TOML int) -> Double
        builder.withDeserializerForClass(Long.class, double.class, (value, constraint, context) -> value.doubleValue());
        builder.withDeserializerForClass(Long.class, Double.class, (value, constraint, context) -> value.doubleValue());

        // Long (TOML int) -> Float
        builder.withDeserializerForClass(Long.class, float.class, (value, constraint, context) -> value.floatValue());
        builder.withDeserializerForClass(Long.class, Float.class, (value, constraint, context) -> value.floatValue());

        return builder.build();
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

        try (CommentedFileConfig fileConfig = CommentedFileConfig.builder(configFile).build()) {
            fileConfig.load();

            T defaultObj = defaultSupplier.get();
            boolean migrated = migrator.migrate(fileConfig, defaultObj, serializer);

            config = defaultSupplier.get();
            deserializer.deserializeFields(fileConfig, config);

            if (migrated) {
                save();
            }
        } catch (Exception e) {
            Shelf.LOGGER.error(
                    "Failed to parse user config from toml cleanly. Reverting to default config.\nfile: {}\nmessage: {}",
                    configFile.getName(), e.getMessage());
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

        try (CommentedFileConfig fileConfig = CommentedFileConfig.builder(configFile).build()) {

            serializer.serializeFields(config, fileConfig);
            fileConfig.save();
        } catch (Exception e) {
            Shelf.LOGGER.error("Failed to save user config to toml.\nfile: {}\nmessage: {}", configFile.getName(),
                    e.getMessage());
            e.printStackTrace();
        }
    }
}
