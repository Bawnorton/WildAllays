package com.bawnorton.wildallays.registry;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.entity.allays.*;
import com.bawnorton.wildallays.entity.attribute.Biome;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.registry.Registry;

import java.util.Hashtable;

public class EntityRegister {
    public static final EntityType<? extends BiomeAllay> BIRCH_ALLAY = register("birch_allay", BirchAllay::new);
    public static final EntityType<? extends BiomeAllay> CRIMSON_ALLAY = register("crimson_allay", CrimsonAllay::new);
    public static final EntityType<? extends BiomeAllay> DARK_ALLAY = register("dark_allay", DarkAllay::new);
    public static final EntityType<? extends BiomeAllay> END_ALLAY = register("end_allay", EndAllay::new);
    public static final EntityType<? extends BiomeAllay> FLOWER_ALLAY = register("flower_allay", FlowerAllay::new);
    public static final EntityType<? extends BiomeAllay> JUNGLE_ALLAY = register("jungle_allay", JungleAllay::new);
    public static final EntityType<? extends BiomeAllay> LUSH_ALLAY = register("lush_allay", LushAllay::new);
    public static final EntityType<? extends BiomeAllay> PLAINS_ALLAY = register("plains_allay", PlainsAllay::new);
    public static final EntityType<? extends BiomeAllay> SAVANNA_ALLAY = register("savanna_allay", SavannaAllay::new);
    public static final EntityType<? extends BiomeAllay> TAIGA_ALLAY = register("taiga_allay", TaigaAllay::new);
    public static final EntityType<? extends BiomeAllay> WARPED_ALLAY = register("warped_allay", WarpedAllay::new);
    public static final EntityType<? extends BiomeAllay> WOODED_BADLANDS_ALLAY = register("wooded_badlands_allay", WoodedBadlandsAllay::new);

    public static final Hashtable<String, EntityWrapper<? extends BiomeAllay>> ENTITIES = new Hashtable<>() {{
        put("birch", new EntityWrapper<>(BIRCH_ALLAY, spawnEgg(BIRCH_ALLAY, 0x939393, 0x1E1E17), Biome.BIRCH_FOREST));
        put("crimson", new EntityWrapper<>(CRIMSON_ALLAY, spawnEgg(CRIMSON_ALLAY, 0x8F1E20, 0x3D212D), Biome.CRIMSON_FOREST));
        put("dark", new EntityWrapper<>(DARK_ALLAY, spawnEgg(DARK_ALLAY, 0x2E2413, 0x211A0F), Biome.DARK_FOREST));
        put("end", new EntityWrapper<>(END_ALLAY, spawnEgg(END_ALLAY, 0x371E36, 0x918593), Biome.END_HIGHLANDS));
        put("flower", new EntityWrapper<>(FLOWER_ALLAY, spawnEgg(FLOWER_ALLAY, 0xFFEC4E, 0x3DB9E7), Biome.FLOWER_FOREST));
        put("jungle", new EntityWrapper<>(JUNGLE_ALLAY, spawnEgg(JUNGLE_ALLAY, 0x23670F, 0x8A9D1C), Biome.JUNGLE));
        put("lush", new EntityWrapper<>(LUSH_ALLAY, spawnEgg(LUSH_ALLAY, 0x789637, 0xE5812D), Biome.LUSH_CAVES));
        put("plains", new EntityWrapper<>(PLAINS_ALLAY, spawnEgg(PLAINS_ALLAY, 0x546A37, 0x78533F), Biome.PLAINS));
        put("savanna", new EntityWrapper<>(SAVANNA_ALLAY, spawnEgg(SAVANNA_ALLAY, 0x6A652D, 0x5D534A), Biome.SAVANNA));
        put("taiga", new EntityWrapper<>(TAIGA_ALLAY, spawnEgg(TAIGA_ALLAY, 0x1A0A00, 0x1A2B1D), Biome.TAIGA));
        put("warped", new EntityWrapper<>(WARPED_ALLAY, spawnEgg(WARPED_ALLAY, 0x2E1822, 0x124F4B), Biome.WARPED_FOREST));
        put("wooded_badlands", new EntityWrapper<>(WOODED_BADLANDS_ALLAY, spawnEgg(WOODED_BADLANDS_ALLAY, 0x935943, 0x4D3D29), Biome.WOODED_BADLANDS));
    }};

    private static <T extends BiomeAllay> EntityType<T> register(String id, EntityType.EntityFactory<T> initialiser) {
        return Registry.register(Registry.ENTITY_TYPE, "wildallays:" + id, FabricEntityTypeBuilder.create(SpawnGroup.CREATURE, initialiser)
                        .dimensions(EntityDimensions.fixed(0.6F, 0.6F))
                        .build());
    }

    private static <T extends BiomeAllay> Item spawnEgg(EntityType<T> type, int primaryColour, int secondaryColour) {
        return new SpawnEggItem(type, primaryColour, secondaryColour, (new Item.Settings()).group(ItemGroup.MISC));
    }

    public static void init() {
        for(String key: ENTITIES.keySet()) {
            FabricDefaultAttributeRegistry.register(ENTITIES.get(key).entityType, BiomeAllay.createAllayAttributes());
            Registry.register(Registry.ITEM, "%s:%s_allay_spawn_egg".formatted(WildAllays.MODID, key), ENTITIES.get(key).egg);
        }
    }

    public static void initSpawning() {
        for(String key: ENTITIES.keySet()) {
            BiomeModifications.addSpawn(ENTITIES.get(key).spawnBiome.getContext(), SpawnGroup.CREATURE, PLAINS_ALLAY, 100, 4, 4);
        }
    }

    public record EntityWrapper<T extends BiomeAllay>(EntityType<T> entityType, Item egg, Biome spawnBiome) {
        public EntityType<T> getEntityType() {
            return entityType;
        }
    }
}
