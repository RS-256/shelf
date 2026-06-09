package com.pug523.shelf;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

import net.fabricmc.loader.api.FabricLoader;

public class ShelfConfigManager {
    public static ShelfConfig config = new ShelfConfig();

    private static final Gson GSON = new Gson();
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(Shelf.MOD_ID).resolve("config.json5").toFile();

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                boolean migrated = false;
                config = GSON.fromJson(root, ShelfConfig.class);
                if (migrated) {
                    save();
                }
            } catch (Exception e) {
                e.printStackTrace();
                config = new ShelfConfig();
                save();
            }
        } else {
            save();
        }
    }

    public static void save() {
        if (!CONFIG_FILE.getParentFile().exists()) {
            CONFIG_FILE.getParentFile().mkdirs();
        }

        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

