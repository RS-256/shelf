package com.pug523.shelf.config;

import java.io.File;
import java.io.Serializable;
import java.util.function.Supplier;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.serde.ObjectDeserializer;
import com.electronwill.nightconfig.core.serde.ObjectDeserializerBuilder;
import com.electronwill.nightconfig.core.serde.ObjectSerializer;
import com.electronwill.nightconfig.core.serde.ObjectSerializerBuilder;
import com.electronwill.nightconfig.json.JsonFormat;
import com.pug523.shelf.Shelf;
import com.pug523.shelf.compat.BuiltinRegistriesCompat;
import com.pug523.shelf.compat.IdentifierCompat;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class JsonConfigManager<T extends Serializable> implements IConfigManager<T> {

    private final Class<T> configClass;
    private final File configFile;
    private final Supplier<T> defaultSupplier;

    private final ObjectSerializer serializer = createCustomSerializer();
    private final ObjectDeserializer deserializer = createCustomDeserializer();

    private final Migrator migrator;

    private T config;

    public JsonConfigManager(Class<T> configClass, File configFile, Supplier<T> defaultSupplier, Migrator migrator) {
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

        builder.withDeserializerForClass(String.class, Item.class, (value, constraint, context) -> {
            if (value == null || value.equals("minecraft:air")) {
                return Items.AIR;
            }
            Identifier id = IdentifierCompat.tryParse(value);
            return BuiltinRegistriesCompat.getItem(id);
        });

        builder.withDeserializerForClass(Double.class, float.class, (value, constraint, context) -> value.floatValue());
        builder.withDeserializerForClass(Double.class, Float.class, (value, constraint, context) -> value.floatValue());
        builder.withDeserializerForClass(Long.class, int.class, (value, constraint, context) -> value.intValue());
        builder.withDeserializerForClass(Long.class, Integer.class, (value, constraint, context) -> value.intValue());
        builder.withDeserializerForClass(Long.class, double.class, (value, constraint, context) -> value.doubleValue());
        builder.withDeserializerForClass(Long.class, Double.class, (value, constraint, context) -> value.doubleValue());
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

        try (FileConfig fileConfig = FileConfig.builder(configFile, JsonFormat.emptyTolerantInstance()).build()) {
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
                    "Failed to parse user config from json cleanly. Reverting to default config.\nfile: {}\nmessage: {}",
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

        try (FileConfig fileConfig = FileConfig.builder(configFile, JsonFormat.emptyTolerantInstance()).build()) {
            serializer.serializeFields(config, fileConfig);
            fileConfig.save();
        } catch (Exception e) {
            Shelf.LOGGER.error("Failed to save user config to json.\nfile: {}\nmessage: {}", configFile.getName(),
                    e.getMessage());
            e.printStackTrace();
        }
    }
}
