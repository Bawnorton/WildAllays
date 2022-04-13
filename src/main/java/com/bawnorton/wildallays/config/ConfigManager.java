package com.bawnorton.wildallays.config;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.config.setting.BooleanSetting;
import com.bawnorton.wildallays.config.setting.IntSetting;
import com.bawnorton.wildallays.config.setting.Setting;
import com.bawnorton.wildallays.util.file.Directories;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Hashtable;
import java.util.Map;

public class ConfigManager {

    private static final IntSetting ALLAY_SPAWN_WEIGHT = new IntSetting("allaySpawnWeight", 40, 0, 1000);
    private static final BooleanSetting ALLAY_PARTICLES = new BooleanSetting("allayGivesOffParticles", true);
    private static final BooleanSetting LOST_ALLAY_PARTICLES = new BooleanSetting("lostAllayGivesOffParticles", false);

    private static final Map<String, Setting<?>> settings;

    static {
        settings = new Hashtable<>() {{
            put(ALLAY_SPAWN_WEIGHT.name, ALLAY_SPAWN_WEIGHT);
            put(ALLAY_PARTICLES.name, ALLAY_PARTICLES);
            put(LOST_ALLAY_PARTICLES.name, LOST_ALLAY_PARTICLES);
        }};
    }

    public static void init() {
        loadConfig();
    }

    public static <T> T get(String key, Class<T> type) {
        return settings.get(key).getValue(type);
    }

    private static void loadConfig() {
        try (Reader in = Files.newBufferedReader(Directories.configFile)) {
            Gson gson = new Gson();
            JsonObject json = gson.fromJson(in, JsonObject.class);
            if(json == null) {
                json = writeDefault();
            }
            Map<String, Boolean> loadedKeys = new Hashtable<>();
            for(String key: settings.keySet()) {
                loadedKeys.put(key, false);
            }
            for(Map.Entry<String, JsonElement> entry: json.entrySet()) {
                String key = entry.getKey();
                if(loadedKeys.replace(key, true) == null) throw new AssertionError();
                Setting<?> setting = settings.get(key);
                if(setting == null) throw new AssertionError();
                if(setting instanceof IntSetting intSetting) {
                    intSetting.setValue(entry.getValue().getAsInt());
                    settings.put(key, intSetting);
                } else if (setting instanceof BooleanSetting booleanSetting) {
                    booleanSetting.setValue(entry.getValue().getAsBoolean());
                    settings.put(key, booleanSetting);
                }
            }
            for(String key: settings.keySet()) {
                if(!loadedKeys.get(key)) throw new AssertionError();
            }
        } catch (IOException ignored) {
        } catch (AssertionError | NumberFormatException e) {
            WildAllays.LOGGER.warn("Malformed Config File. Resetting...");
            writeDefault();
            loadConfig();
        }
    }

    private static JsonObject writeDefault() {
        try (Writer out = Files.newBufferedWriter(Directories.configFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonObject json = new JsonObject();
            for(Map.Entry<String, Setting<?>> entry: settings.entrySet()) {
                json.addProperty(entry.getKey(), entry.getValue().getValue().toString());
            }
            gson.toJson(json, out);
            return json;
        } catch (IOException ignored) {
        }
        throw new AssertionError();
    }
}
