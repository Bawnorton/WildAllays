package com.bawnorton.wildallays.config;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.util.file.Directories;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.internal.LinkedTreeMap;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameRules;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.function.BiConsumer;

public class ConfigManager {
    public static final Hashtable<String, GameRules.Key<?>> gamerules = new Hashtable<>();

    public static void init() {
        loadConfig();
    }

    @SuppressWarnings("unchecked")
    private static void loadConfig() {
        try {
            Gson gson = new Gson();

            Reader in = Files.newBufferedReader(Directories.configFile);
            Map<String, LinkedTreeMap<String, String>> jsonMap = gson.fromJson(in, Map.class);
            if (jsonMap == null) {
                writeDefault();
                return;
            }

            for (Map.Entry<String, LinkedTreeMap<String, String>> entry : jsonMap.entrySet()) {
                String key = entry.getKey();
                LinkedTreeMap<String, String> map = entry.getValue();
                GameRules.Category category = GameRules.Category.valueOf(map.get("category"));
                String type = map.get("type");
                if ("Integer".equals(type)) {
                    int defaultValue = Integer.parseInt(map.get("current"));
                    int min = Integer.parseInt(map.get("min"));
                    int max = Integer.parseInt(map.get("max"));
                    gamerules.put(key, GameRuleRegistry.register(key, category, GameRuleFactory.createIntRule(defaultValue, min, max, (server, rule) -> {})));
                }
            }
            in.close();
        } catch (JsonIOException | NumberFormatException e) {
            WildAllays.LOGGER.error("Malformed config file, resetting");
            writeDefault();
        } catch (IOException e) {
            WildAllays.LOGGER.error("Failure during config reading, this should be unreachable\n" +
                    Arrays.toString(e.getStackTrace()));
        }
    }

    @SuppressWarnings("unchecked")
    private static void writeDefault() {
        WildAllays.LOGGER.info("Creating Config File");
        Gson gson = new Gson();

        Map<String, GameRule<?, ?>> jsonMap = new HashMap<>();
        jsonMap.put("allaySpawnWeight", new AllaySpawnRate(
                GameRules.Category.SPAWNING,
                "Integer",
                10, 0, 1000,
                (server, rule) -> updateConfig("allaySpawnWeight", rule.get())));

        for (Map.Entry<String, GameRule<?, ?>> entry : jsonMap.entrySet()) {
            String key = entry.getKey();
            GameRule<?, ?> rule = entry.getValue();
            if ("Integer".equals(rule.type())) {
                Integer[] values = (Integer[]) rule.values();
                gamerules.put(key, GameRuleRegistry.register(
                        key, rule.category(),
                        GameRuleFactory.createIntRule(
                                values[0], values[1], values[2],
                                (BiConsumer<MinecraftServer, GameRules.IntRule>) rule.callback()
                        )
                ));
            }
        }

        try (Writer out = Files.newBufferedWriter(Directories.configFile)) {
            gson.toJson(jsonMap, out);
        } catch (IOException e) {
            WildAllays.LOGGER.error("Failure during config writing, this should be unreachable\n" +
                    Arrays.toString(e.getStackTrace()));
        }
    }

    @SuppressWarnings("unchecked")
    private static void updateConfig(String key, Object value) {
        try {
            Gson gson = new Gson();

            Reader in = Files.newBufferedReader(Directories.configFile);
            Map<String, LinkedTreeMap<String, String>> jsonMap = gson.fromJson(in, Map.class);
            LinkedTreeMap<String, String> treeMap = jsonMap.get(key);
            treeMap.replace("current", String.valueOf(value));
            in.close();

            Writer out = Files.newBufferedWriter(Directories.configFile);
            gson.toJson(jsonMap, out);
            out.close();

        } catch (IOException e) {
        WildAllays.LOGGER.error("Failure during config updating, this should be unreachable\n" +
                Arrays.toString(e.getStackTrace()));
        }
    }

    private interface GameRule<T, R> {
        GameRules.Category category();

        String type();

        T[] values();

        BiConsumer<MinecraftServer, R> callback();
    }

    public record AllaySpawnRate(GameRules.Category category, String type, Integer current, Integer min, Integer max,
                                 BiConsumer<MinecraftServer, GameRules.IntRule> callback) implements GameRule<Integer, GameRules.IntRule> {
        public Integer[] values() {
            return new Integer[]{current, min, max};
        }
    }
}
