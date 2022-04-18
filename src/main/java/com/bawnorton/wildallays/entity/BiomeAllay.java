package com.bawnorton.wildallays.entity;

import com.bawnorton.wildallays.config.ConfigManager;
import com.bawnorton.wildallays.entity.allay.*;
import com.bawnorton.wildallays.registry.Items;
import com.bawnorton.wildallays.util.Colour;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
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

import java.util.Hashtable;
import java.util.List;
import java.util.function.Predicate;

import static com.bawnorton.wildallays.registry.Entities.*;
import static net.minecraft.item.Items.*;

public abstract class BiomeAllay extends AllayEntity {

    protected static Hashtable<Material, ColorResolver> materialColourMap;
    protected Colour colour = new Colour();

    public BiomeAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
        materialColourMap = new Hashtable<>() {{
            put(Material.WATER, BiomeColors.WATER_COLOR);
            put(Material.LEAVES, BiomeColors.FOLIAGE_COLOR);
            put(Material.SOLID_ORGANIC, BiomeColors.GRASS_COLOR);
        }};
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack playerStack = player.getStackInHand(hand);
        ItemStack allayStack = this.getStackInHand(hand);
        ItemStack allayHead = this.getEquippedStack(EquipmentSlot.HEAD);
        if(allayStack.isEmpty()) {
            if(!playerStack.isEmpty() && allayHead.isEmpty() && !player.isSneaking()) {
                ItemStack helmet;
                if(playerStack.getItem() == IRON_HELMET) {
                    helmet = Items.ALLAY_IRON_HELMET.fromVanillaItem(playerStack);
                } else if (playerStack.getItem() == CHAINMAIL_HELMET) {
                    helmet = Items.ALLAY_CHAIN_HELMET.fromVanillaItem(playerStack);
                } else if (playerStack.getItem() == GOLDEN_HELMET) {
                    helmet = Items.ALLAY_GOLD_HELMET.fromVanillaItem(playerStack);
                } else {
                    return super.interactMob(player, hand);
                }
                this.equipStack(EquipmentSlot.HEAD, helmet);
                if(!player.getAbilities().creativeMode) {
                    playerStack.decrement(1);
                }
                this.world.playSoundFromEntity(player, this, SoundEvents.ENTITY_ALLAY_ITEM_GIVEN, SoundCategory.NEUTRAL, 2.0f, 1.2f);
                this.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
                return ActionResult.SUCCESS;
            } else if (playerStack.isEmpty() && !allayHead.isEmpty()) {
                this.equipStack(EquipmentSlot.HEAD, ItemStack.EMPTY);
                this.world.playSoundFromEntity(player, this, SoundEvents.ENTITY_ALLAY_ITEM_TAKEN, SoundCategory.NEUTRAL, 2.0f, 1.0f);
                this.getBrain().forget(MemoryModuleType.LIKED_PLAYER);
                ItemStack helmet = allayHead;
                if(allayHead.getItem() == Items.ALLAY_IRON_HELMET) {
                   helmet = Items.ALLAY_IRON_HELMET.toVanillaItem(allayHead);
                } else if (allayHead.getItem() == Items.ALLAY_CHAIN_HELMET) {
                    helmet = Items.ALLAY_CHAIN_HELMET.toVanillaItem(allayHead);
                } else if (allayHead.getItem() == Items.ALLAY_GOLD_HELMET) {
                    helmet = Items.ALLAY_GOLD_HELMET.toVanillaItem(allayHead);
                }
                player.giveItemStack(helmet);
                return ActionResult.SUCCESS;
            }
        }
        return super.interactMob(player, hand);
    }

    protected boolean checkSurface(BlockPos pos) {
        while (pos.getY() < world.getTopY()) {
            BlockState state = world.getBlockState(pos);
            Material material = state.getMaterial();
            pos = pos.up();
            if (state.isAir() || material == Material.LEAVES || material == Material.WOOD) {
                continue;
            }
            return false;
        }
        return true;
    }

    protected boolean checkDarkness(BlockPos pos) {
        return ConfigManager.get("allay_spawn_during_day", Boolean.class) ||
                this.world.isNight() && world.getLightLevel(LightType.BLOCK, pos) < 1;
    }

    protected boolean randFailure() {
        return true;
    }

    @Override
    public boolean canSpawn(WorldView world) {
        BlockPos pos = this.getBlockPos();
        return randFailure() && checkDarkness(pos) && checkSurface(pos) && super.canSpawn(world);
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(net.minecraft.item.Items.ALLAY_SPAWN_EGG);
    }

    protected void spawnParticles() {
        if (ConfigManager.get("allay_gives_off_particles", Boolean.class)) {
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
        if (this.world.isClient) {
            if (this.random.nextInt(20) == 0) {
                spawnParticles();
            }
        }
        super.tickMovement();
    }

    public Colour getColor() {
        return colour;
    }

    public enum Biome {
        NONE,
        BAMBOO_JUNGLE("bamboo_jungle"),
        BIRCH_FOREST("birch_forest"),
        CRIMSON_FOREST("crimson_forest"),
        DARK_FOREST("dark_forest"),
        END_HIGHLANDS("end_highlands"),
        FLOWER_FOREST("flower_forest"),
        FOREST("forest"),
        JUNGLE("jungle"),
        LUSH_CAVES("lush_caves"),
        MANGROVE_SWAMP("mangrove_swamp"),
        MEADOW("meadow"),
        OLD_GROWTH_BIRCH_FOREST("old_growth_birch_forest"),
        OLD_GROWTH_PINE_TAIGA("old_growth_pine_taiga"),
        OLD_GROWTH_SPRUCE_TAIGA("old_growth_spruce_taiga"),
        PLAINS("plains"),
        SAVANNA("savanna"),
        SAVANNA_PLATEAU("savanna_plateau"),
        SPARSE_JUNGLE("sparse_jungle"),
        SWAMP("swamp"),
        SUNFLOWER_PLAINS("sunflower_plains"),
        TAIGA("taiga"),
        WARPED_FOREST("warped_forest"),
        WINDSWEPT_SAVANNA("windswept_savanna"),
        WOODED_BADLANDS("wooded_badlands");

        public final boolean enabled;
        private final Predicate<BiomeSelectionContext> context;
        private final Identifier identifier;

        Biome() {
            identifier = null;
            context = null;
            enabled = false;
        }

        Biome(String id) {
            identifier = new Identifier("minecraft", id);
            context = BiomeSelectors.includeByKey(RegistryKey.of(Registry.BIOME_KEY, identifier));
            enabled = ConfigManager.get(id, Boolean.class);
        }

        public static Biome fromRegistry(RegistryEntry<net.minecraft.world.biome.Biome> entry) {
            for (Biome biome : Biome.values()) {
                if (entry.matchesId(biome.identifier)) {
                    return biome;
                }
            }
            return NONE;
        }

        public Predicate<BiomeSelectionContext> getContext() {
            return context;
        }

        @Override
        public String toString() {
            return "Biome{" +
                    "identifier=" + identifier +
                    ", enabled=" + enabled +
                    '}';
        }
    }

    public enum Type {
        BIRCH("birch", BirchAllay.class, BIRCH_ALLAY, Biome.BIRCH_FOREST, Biome.OLD_GROWTH_BIRCH_FOREST),
        CRIMSON("crimson", CrimsonAllay.class, CRIMSON_ALLAY, Biome.CRIMSON_FOREST),
        DARK("dark", DarkAllay.class, DARK_ALLAY, Biome.DARK_FOREST),
        END("end", EndAllay.class, END_ALLAY, Biome.END_HIGHLANDS),
        FLOWER("flower", FlowerAllay.class, FLOWER_ALLAY, Biome.FLOWER_FOREST),
        FOREST("forest", ForestAllay.class, FOREST_ALLAY, Biome.FOREST),
        JUNGLE("jungle", JungleAllay.class, JUNGLE_ALLAY, Biome.JUNGLE, Biome.BAMBOO_JUNGLE, Biome.SPARSE_JUNGLE),
        LUSH("lush", LushAllay.class, LUSH_ALLAY, Biome.LUSH_CAVES),
        LOST("lost", LostAllay.class, LOST_ALLAY, Biome.NONE),
        PLAINS("plains", PlainsAllay.class, PLAINS_ALLAY, Biome.PLAINS, Biome.MEADOW, Biome.SUNFLOWER_PLAINS),
        SAVANNA("savanna", SavannaAllay.class, SAVANNA_ALLAY, Biome.SAVANNA, Biome.SAVANNA_PLATEAU, Biome.WINDSWEPT_SAVANNA),
        SWAMP("swamp", SwampAllay.class, SWAMP_ALLAY, Biome.SWAMP, Biome.MANGROVE_SWAMP),
        TAIGA("taiga", TaigaAllay.class, TAIGA_ALLAY, Biome.TAIGA, Biome.OLD_GROWTH_PINE_TAIGA, Biome.OLD_GROWTH_SPRUCE_TAIGA),
        WARPED("warped", WarpedAllay.class, WARPED_ALLAY, Biome.WARPED_FOREST),
        WOODED_BADLANDS("wooded_badlands", WoodedBadlandsAllay.class, WOODED_BADLANDS_ALLAY, Biome.WOODED_BADLANDS);

        private static final Hashtable<Object, Type> classMap = new Hashtable<>();

        static {
            for (Type type : values()) {
                classMap.put(type.clazz, type);
            }
        }

        public final String name;
        public final EntityType<? extends BiomeAllay> entityType;
        public final List<Biome> biomes;
        private final Object clazz;

        Type(String name, Object clazz, EntityType<? extends BiomeAllay> entityType, Biome... biomes) {
            this.name = name;
            this.clazz = clazz;
            this.entityType = entityType;
            this.biomes = List.of(biomes);
        }

        public static Type fromBiome(Biome biome) {
            for (Type allay : Type.values()) {
                if (allay.biomes.contains(biome)) {
                    return allay;
                }
            }
            return LOST;
        }

        public static Type fromClass(Object clazz) {
            return classMap.get(clazz);
        }

        public static Type fromBiome(RegistryEntry<net.minecraft.world.biome.Biome> biome) {
            return Type.fromBiome(Biome.fromRegistry(biome));
        }
    }
}
