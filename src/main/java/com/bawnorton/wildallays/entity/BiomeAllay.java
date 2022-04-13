package com.bawnorton.wildallays.entity;

import com.bawnorton.wildallays.config.ConfigManager;
import com.bawnorton.wildallays.entity.allay.*;
import com.bawnorton.wildallays.item.BiomeAllaySpawnEgg;
import com.bawnorton.wildallays.util.Colour;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.level.ColorResolver;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Predicate;

import static com.bawnorton.wildallays.registry.EntityRegister.*;

public abstract class BiomeAllay extends AllayEntity {

    protected Biome biome;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    protected static Hashtable<Material, ColorResolver> materialColourMap;
    protected Colour colour = new Colour();

    public BiomeAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
        materialColourMap = new Hashtable<>(){{
            put(Material.WATER, BiomeColors.WATER_COLOR);
            put(Material.LEAVES, BiomeColors.FOLIAGE_COLOR);
            put(Material.SOLID_ORGANIC, BiomeColors.GRASS_COLOR);
        }};
    }

    protected void setColour() {
        colour = Colour.fromBinary(world.getColor(this.getBlockPos(), BiomeColors.GRASS_COLOR));
    }

    @Override
    public void setPosition(double x, double y, double z) {
        setColour();
        super.setPosition(x, y, z);
    }

    protected boolean checkSurface(BlockPos pos) {
        while(pos.getY() < world.getTopY()) {
            BlockState state = world.getBlockState(pos);
            Material material = state.getMaterial();
            pos = pos.up();
            if(state.isAir() || material == Material.LEAVES || material == Material.WOOD) {
                continue;
            }
            return false;
        }
        return true;
    }

    protected boolean checkDarkness(BlockPos pos) {
        return this.world.isNight() && world.getLightLevel(LightType.BLOCK, pos) < 1;
    }

    @Override
    public boolean canSpawn(WorldView world) {
        BlockPos pos = this.getBlockPos();
        boolean spawnInDark = checkDarkness(pos);
        boolean surfaceSpawn = checkSurface(pos);
        return spawnInDark && surfaceSpawn && super.canSpawn(world);
    }

    protected void spawnParticles() {
        if(ConfigManager.get("allay_gives_off_particles", Boolean.class)) {
            this.world.addParticle(ParticleTypes.END_ROD,
                    this.getParticleX(1.2D),
                    this.getRandomBodyY(),
                    this.getParticleZ(1.2D),
                    (this.random.nextInt(21) - 10) / 100D,
                    (this.random.nextInt(21) - 10) / 100D,
                    (this.random.nextInt(21) - 10) / 100D);
        }
    }

    @Override
    public void tickMovement() {
        if(this.world.isClient) {
            if(this.random.nextInt(20) == 0) {
                spawnParticles();
            }
        }
        super.tickMovement();
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(Items.ALLAY_SPAWN_EGG);
    }

    public Colour getColor() {
        return colour;
    }

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
        private final List<Identifier> identifiers;

        Biome(String... ids) {
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

        public static Biome fromRegistry(RegistryEntry<net.minecraft.world.biome.Biome> entry) {
            for(Biome biome: Biome.values()) {
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
            return "Biome{" +
                    "keys=" + keys +
                    '}';
        }
    }

    public enum Type {
        LOST("lost", LostAllay.class, LOST_ALLAY, Biome.NONE),
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
        public final EntityType<? extends BiomeAllay> entityType;
        public final Biome biome;
        public final Object clazz;

        public static final Item egg = new BiomeAllaySpawnEgg();

        Type(String name, Object clazz, EntityType<? extends BiomeAllay> entityType, Biome biome) {
            this.name = name;
            this.clazz = clazz;
            this.entityType = entityType;
            this.biome = biome;
        }

        public static Type fromClass(Object clazz) {
            for(Type allay: Type.values()) {
                if(allay.clazz == clazz) {
                    return allay;
                }
            }
            return LOST;
        }

        public static Type fromBiome(Biome biome) {
            for(Type allay: Type.values()) {
                if(allay.biome == biome) {
                    return allay;
                }
            }
            return LOST;
        }

        public static Type fromBiome(RegistryEntry<net.minecraft.world.biome.Biome> biome) {
            return Type.fromBiome(Biome.fromRegistry(biome));
        }
    }
}
