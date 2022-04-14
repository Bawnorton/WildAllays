package com.bawnorton.wildallays.config;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.util.file.json.setting.BooleanSetting;
import com.bawnorton.wildallays.util.file.json.setting.IntSetting;
import com.bawnorton.wildallays.util.file.json.setting.Setting;
import com.bawnorton.wildallays.util.file.json.setting.SettingMap;
import com.bawnorton.wildallays.util.file.Directories;
import com.bawnorton.wildallays.util.file.json.JsonUtil;
import com.google.gson.JsonObject;

import java.util.Hashtable;
import java.util.Map;

public class ConfigManager {

    private static final Map<String, Setting<?>> settings;

    static {
        settings = new Hashtable<>() {{
            put("allay_features", new SettingMap(new Hashtable<>() {{
                put("allay_spawn_weight", new IntSetting(20));
                put("allay_spawn_during_day", new BooleanSetting(false));
            }}));
            put("allay_appearance", new SettingMap(new Hashtable<>() {{
                put("allay_gives_off_particles", new BooleanSetting(true));
                put("allay_glow", new BooleanSetting(false));
                put("lost_allay_camoflages", new BooleanSetting(true));
                put("lost_allay_gives_off_particles", new BooleanSetting(false));
            }}));
            put("allay_spawn_in_biomes", new SettingMap(new Hashtable<>() {{
                put("bamboo_jungle", new BooleanSetting(true));
                put("birch_forest", new BooleanSetting(true));
                put("crimson_forest", new BooleanSetting(true));
                put("dark_forest", new BooleanSetting(true));
                put("end_highlands", new BooleanSetting(true));
                put("flower_forest", new BooleanSetting(true));
                put("forest", new BooleanSetting(true));
                put("jungle", new BooleanSetting(true));
                put("lush_caves", new BooleanSetting(true));
                put("meadow", new BooleanSetting(true));
                put("mangrove_swamp", new BooleanSetting(true));
                put("old_growth_birch_forest", new BooleanSetting(true));
                put("old_growth_pine_taiga", new BooleanSetting(true));
                put("old_growth_spruce_taiga", new BooleanSetting(true));
                put("plains", new BooleanSetting(true));
                put("savanna", new BooleanSetting(true));
                put("savanna_plateau", new BooleanSetting(true));
                put("sparse_jungle", new BooleanSetting(true));
                put("swamp", new BooleanSetting(true));
                put("sunflower_plains", new BooleanSetting(true));
                put("taiga", new BooleanSetting(true));
                put("warped_forest", new BooleanSetting(true));
                put("windswept_savanna", new BooleanSetting(true));
                put("wooded_badlands", new BooleanSetting(true));
            }}));
        }};
    }

    public static void init() {
        loadConfig();
    }

    private static Setting<?> searchSettings(String key) {
        Setting<?> setting = searchSettings(settings, key);
        if(setting == null) throw new AssertionError("\"%s\" is not a setting".formatted(key));
        return setting;
    }

    private static Setting<?> searchSettings(Map<String, Setting<?>> settingMap, String key) {
        if(settingMap.containsKey(key)) {
            return settingMap.get(key);
        }
        Setting<?> result = null;
        for(Setting<?> setting: settingMap.values()) {
            if(setting instanceof SettingMap map) {
                result = searchSettings(map.settings(), key);
                if(result != null) break;
            }
        }
        return result;
    }

    public static <T> T get(String key, Class<T> type) {
        return searchSettings(key).getValue(type);
    }

    private static void loadConfig() {
        JsonObject json = JsonUtil.loadJson(Directories.configFile);
        if(json == null) json = writeDefault();
        try {
            JsonUtil.deserialize(settings, json);
        } catch (AssertionError | NumberFormatException e) {
            WildAllays.LOGGER.warn("Malformed Config File. Resetting...");
            writeDefault();
            loadConfig();
        }
    }

    private static JsonObject writeDefault() {
        WildAllays.LOGGER.info("Creating Config File");
        JsonObject json = JsonUtil.serialize(settings, new JsonObject());
        JsonUtil.writeJson(Directories.configFile, json);
        return json;
    }
}
