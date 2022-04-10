package com.bawnorton.wildallays.entity.enums;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.entity.allay.*;
import com.bawnorton.wildallays.item.BiomeAllaySpawnEgg;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.RegistryEntry;

import static com.bawnorton.wildallays.registry.EntityRegister.*;

public enum Allay {
    DEFAULT("biome", BiomeAllay.class, BIOME_ALLAY, AllayBiome.NONE),
    BIRCH("birch", BirchAllay.class, BIRCH_ALLAY,  AllayBiome.BIRCH_FOREST),
    CRIMSON("crimson", CrimsonAllay.class, CRIMSON_ALLAY,  AllayBiome.CRIMSON_FOREST),
    DARK("dark", DarkAllay.class, DARK_ALLAY,  AllayBiome.DARK_FOREST),
    END("end", EndAllay.class, END_ALLAY,  AllayBiome.END_HIGHLANDS),
    FLOWER("flower", FlowerAllay.class, FLOWER_ALLAY,  AllayBiome.FLOWER_FOREST),
    FOREST("forest", ForestAllay.class, FOREST_ALLAY,  AllayBiome.FOREST),
    JUNGLE("jungle", JungleAllay.class, JUNGLE_ALLAY,  AllayBiome.JUNGLE),
    LUSH("lush", LushAllay.class, LUSH_ALLAY, AllayBiome.LUSH_CAVES),
    PLAINS("plains", PlainsAllay.class, PLAINS_ALLAY,  AllayBiome.PLAINS),
    SAVANNA("savanna", SavannaAllay.class, SAVANNA_ALLAY,  AllayBiome.SAVANNA),
    TAIGA("taiga", TaigaAllay.class, TAIGA_ALLAY,  AllayBiome.TAIGA),
    WARPED("warped", WarpedAllay.class, WARPED_ALLAY,  AllayBiome.WARPED_FOREST),
    WOODED_BADLANDS("wooded_badlands", WoodedBadlandsAllay.class, WOODED_BADLANDS_ALLAY,  AllayBiome.WOODED_BADLANDS);

    public final String name;
    public final EntityType<? extends BiomeAllay> type;
    public final AllayBiome biome;
    public final Object clazz;

    public static final Item egg = new BiomeAllaySpawnEgg();

    Allay(String name,  Object clazz, EntityType<? extends BiomeAllay>  type, AllayBiome biome) {
        this.name = name;
        this.clazz = clazz;
        this.type = type;
        this.biome = biome;
    }

    public static Allay fromClass(Object clazz) {
        for(Allay allay: Allay.values()) {
            if(allay.clazz == clazz) {
                return allay;
            }
        }
        return DEFAULT;
    }

    public static Allay fromBiome(AllayBiome biome) {
        for(Allay allay: Allay.values()) {
            if(allay.biome == biome) {
                return allay;
            }
        }
        return DEFAULT;
    }

    public static Allay fromBiome(RegistryEntry<net.minecraft.world.biome.Biome> biome) {
        return Allay.fromBiome(AllayBiome.fromRegistry(biome));
    }
}
