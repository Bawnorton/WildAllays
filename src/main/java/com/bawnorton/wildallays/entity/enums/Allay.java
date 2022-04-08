package com.bawnorton.wildallays.entity.enums;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.entity.allay.*;
import com.bawnorton.wildallays.item.BiomeAllaySpawnEgg;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

import static com.bawnorton.wildallays.registry.EntityRegister.*;

public enum Allay {
    DEFAULT("biome", BiomeAllay.class, BIOME_ALLAY, Biome.NONE),
    BIRCH("birch", BirchAllay.class, BIRCH_ALLAY,  Biome.BIRCH_FOREST),
    CRIMSON("crimson", CrimsonAllay.class, CRIMSON_ALLAY,  Biome.CRIMSON_FOREST),
    DARK("dark", DarkAllay.class, DARK_ALLAY,  Biome.DARK_FOREST),
    END("end", EndAllay.class, END_ALLAY,  Biome.END_HIGHLANDS),
    FLOWER("flower", FlowerAllay.class, FLOWER_ALLAY,  Biome.FLOWER_FOREST),
    FOREST("forest", ForestAllay.class, FOREST_ALLAY,  Biome.FOREST),
    JUNGLE("jungle", JungleAllay.class, JUNGLE_ALLAY,  Biome.JUNGLE),
    LUSH("lush", LushAllay.class, LUSH_ALLAY, Biome.LUSH_CAVES),
    PLAINS("plains", PlainsAllay.class, PLAINS_ALLAY,  Biome.PLAINS),
    SAVANNA("savanna", SavannaAllay.class, SAVANNA_ALLAY,  Biome.SAVANNA),
    TAIGA("taiga", TaigaAllay.class, TAIGA_ALLAY,  Biome.TAIGA),
    WARPED("warped", WarpedAllay.class, WARPED_ALLAY,  Biome.WARPED_FOREST),
    WOODED_BADLANDS("wooded_badlands", WoodedBadlandsAllay.class, WOODED_BADLANDS_ALLAY,  Biome.WOODED_BADLANDS);

    public final String name;
    public final EntityType<? extends BiomeAllay> type;
    public final Biome biome;
    public final Object clazz;

    public static final Item egg = new BiomeAllaySpawnEgg();

    Allay(String name,  Object clazz, EntityType<? extends BiomeAllay>  type, Biome biome) {
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
        return null;
    }
}
