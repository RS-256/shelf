package com.pug523.shelf.config;

import java.io.File;

import net.fabricmc.loader.api.FabricLoader;

public class ConfigUtil {
    public static File resolveConfigFile(String dir, String file) {
        return FabricLoader.getInstance().getConfigDir().resolve(dir).resolve(file).toFile();
    }
}
