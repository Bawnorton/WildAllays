package com.bawnorton.wildallays.util.file.json;

import com.bawnorton.wildallays.util.file.json.setting.BooleanSetting;
import com.bawnorton.wildallays.util.file.json.setting.IntSetting;
import com.bawnorton.wildallays.util.file.json.setting.Setting;
import com.bawnorton.wildallays.util.file.json.setting.SettingMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.Map;

public class JsonUtil {
    public static JsonObject loadJson(Path jsonFile) {
        JsonObject json = null;
        try (Reader in = Files.newBufferedReader(jsonFile)) {
            Gson gson = new Gson();
            json = gson.fromJson(in, JsonObject.class);
        } catch (IOException ignored) {}
        return json;
    }

    public static void writeJson(Path jsonFile, JsonObject json) {
        try (Writer out = Files.newBufferedWriter(jsonFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(json, out);
        } catch (IOException ignored) {}
    }

    public static JsonObject serialize(Map<String, Setting<?>> settingMap, JsonObject json) {
        for(Map.Entry<String, Setting<?>> entry: settingMap.entrySet()) {
            Setting<?> setting = entry.getValue();
            if(setting instanceof SettingMap listSetting) {
                json.add(entry.getKey(), serialize(listSetting.settings(), new JsonObject()));
            } else {
                json.addProperty(entry.getKey(), setting.getValue().toString());
            }
        }
        return json;
    }

    public static Map<String, Setting<?>> deserialize(Map<String, Setting<?>> settingMap, JsonObject json) throws AssertionError, NumberFormatException {
        Map<String, Boolean> checking = new Hashtable<>();
        for(String key: settingMap.keySet()) {
            checking.put(key, false);
        }
        for(Map.Entry<String, JsonElement> entry: json.entrySet()) {
            String key = entry.getKey();
            if(checking.replace(key, true) == null) throw new AssertionError();
            Setting<?> setting = settingMap.get(key);
            if(setting == null) throw new AssertionError();
            if(setting instanceof IntSetting) {
                settingMap.replace(key, new IntSetting(entry.getValue().getAsInt()));
            } else if (setting instanceof BooleanSetting) {
                settingMap.replace(key, new BooleanSetting(entry.getValue().getAsBoolean()));
            } else if (setting instanceof SettingMap settingMap1) {
                settingMap.replace(key, new SettingMap(deserialize(settingMap1.settings(), entry.getValue().getAsJsonObject())));
            }
        }
        for(String key: settingMap.keySet()) {
            if(!checking.get(key)) throw new AssertionError();
        }
        return settingMap;
    }
}
