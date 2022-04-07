package com.bawnorton.wildallays.entity.attribute;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
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

    private final Identifier[] identifiers;
    private Predicate<BiomeSelectionContext> context;

    Biome(String... ids) {
        identifiers = new Identifier[ids.length];
        for(int i = 0; i < ids.length; i++) {
            identifiers[i] = new Identifier(ids[i]);
        }

        context = BiomeSelectors.tag(TagKey.of(Registry.BIOME_KEY, identifiers[0]));
        if(identifiers.length > 1) {
            for(int i = 1; i < identifiers.length; i++) {
                context = context.or(BiomeSelectors.tag(TagKey.of(Registry.BIOME_KEY, identifiers[i])));
            }
        }
    }

    public Predicate<BiomeSelectionContext> getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "Biome{" +
                "identifiers=" + Arrays.toString(identifiers) +
                '}';
    }
}
