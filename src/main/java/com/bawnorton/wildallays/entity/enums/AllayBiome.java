package com.bawnorton.wildallays.entity.enums;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public enum AllayBiome {
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

    private final List<RegistryKey<Biome>> keys;
    private final Predicate<BiomeSelectionContext> context;
    private final List<Identifier> identifiers;

    AllayBiome(String... ids) {
        keys = new ArrayList<>(ids.length);
        identifiers = new ArrayList<>(ids.length);
        for (String id : ids) {
            Identifier identifer = new Identifier("minecraft", id);
            identifiers.add(identifer);
            keys.add(RegistryKey.of(Registry.BIOME_KEY, identifer));
        }
        context = BiomeSelectors.includeByKey(keys);
    }

    public Predicate<BiomeSelectionContext> getContext() {
        return context;
    }

    public List<Identifier> getIdentifiers() {
        return identifiers;
    }

    public static AllayBiome fromRegistry(RegistryEntry<Biome> entry) {
        for(AllayBiome biome: AllayBiome.values()) {
            for(Identifier identifier: biome.identifiers) {
                if(entry.matchesId(identifier)) {
                    return biome;
                }
            }
        }
        return NONE;
    }

    @Override
    public String toString() {
        return "AllayBiome{" +
                "keys=" + keys +
                '}';
    }
}
