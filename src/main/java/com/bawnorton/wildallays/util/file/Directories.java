package com.bawnorton.wildallays.util.file;

import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

import static com.bawnorton.wildallays.util.file.FileUtil.*;

public class Directories {
    public static Path configDirectory;
    public static Path configFile;

    public static void init() {
        Path root = FabricLoader.getInstance().getConfigDir().getParent();
        configDirectory = createDirectory(toPath(root.toString() + "/config/wildallays"));
        configFile = getFile(configDirectory + "/config.json");
    }
}
