package com.bawnorton.wildallays.entity.attribute;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public enum Biome {
    NONE("none"),
    BIRCH_FOREST("birch_forest", "old_growth_birch_forest"),
    CRIMSON_FOREST("crimson_forest"),
    DARK_FOREST("dark_forest"),
    END_HIGHLANDS("end_highlands"),
    FLOWER_FOREST("flower_forest"),
    FOREST("forest"),
    JUNGLE("jungle", "bamboo_jungle", "sparse_jungle"),
    LUSH_CAVES("lush_caves"),
    PLAINS("plains", "meadow", "sunflower_plains"),
    SAVANNA("savanna", "savanna_plateau", "windswept_savanna"),
    TAIGA("taiga", "old_growth_pine_taiga", "old_growth_spruce_taiga"),
    WARPED_FOREST("warped_forest"),
    WOODED_BADLANDS("wooded_badlands");

    private final List<RegistryKey<net.minecraft.world.biome.Biome>> keys;
    private final Predicate<BiomeSelectionContext> context;

    Biome(String... ids) {
        keys = new ArrayList<>(ids.length);
        for (String id : ids) {
            keys.add(RegistryKey.of(Registry.BIOME_KEY, new Identifier("minecraft", id)));
        }
        context = BiomeSelectors.includeByKey(keys);
    }

    public Predicate<BiomeSelectionContext> getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "Biome{" +
                "keys=" + keys +
                '}';
    }
}
