package com.bawnorton.wildallays.entity.ai;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.*;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.GlobalPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class BiomeAllayBrain {
    protected static final ImmutableList<SensorType<? extends Sensor<? super BiomeAllay>>> SENSORS;
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_MODULES;

    public static void tick(BiomeAllay allay) {
        Brain<BiomeAllay> brain =  allay.getBrain();
        brain.resetPossibleActivities(ImmutableList.of(Activity.IDLE, Activity.FIGHT));
        allay.setAttacking(brain.hasMemoryModule(MemoryModuleType.ATTACK_TARGET));
    }

    public static Brain<?> create(BiomeAllay biomeAllay, Dynamic<?> dynamic) {
        Brain.Profile<BiomeAllay> profile = Brain.createProfile(MEMORY_MODULES, SENSORS);
        Brain<BiomeAllay> brain = profile.deserialize(dynamic);
        addCoreActivities(brain);
        addIdleActivities(brain);
        addFightActivities(biomeAllay, brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.resetPossibleActivities();
        return brain;
    }

    private static void addCoreActivities(Brain<BiomeAllay> brain) {
        brain.setTaskList(Activity.CORE, 0, ImmutableList.of(
                new StayAboveWaterTask(0.8F),
                new LookAroundTask(45, 90),
                new WanderAroundTask(),
                new TemptationCooldownTask(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS),
                new TemptationCooldownTask(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)));
    }

    private static void addIdleActivities(Brain<BiomeAllay> brain) {
        brain.setTaskList(Activity.IDLE, ImmutableList.of(
                Pair.of(0, new WalkToNearestVisibleWantedItemTask<>((allay) -> true, 1.75F, true, 32)),
                Pair.of(1, new GiveInventoryToLookTargetTask<>(BiomeAllayBrain::getLookTarget, 2.25F)),
                Pair.of(2, new WalkTowardsLookTargetTask<>(BiomeAllayBrain::getLookTarget, 4, 16, 2.25F)),
                Pair.of(3, new TimeLimitedTask<>(new FollowMobTask((allay) -> true, 6.0F), UniformIntProvider.create(30, 60))),
                Pair.of(4, new RandomTask<>(ImmutableList.of(
                        Pair.of(new NoPenaltyStrollTask(1.0F), 2),
                        Pair.of(new GoTowardsLookTarget(1.0F, 3), 2),
                        Pair.of(new WaitTask(30, 60), 1))))));
    }

    private static void addFightActivities(BiomeAllay biomeAllay, Brain<BiomeAllay> brain) {
        brain.setTaskList(Activity.FIGHT, 5, ImmutableList.of(
                        new ForgetAttackTargetTask<>(biomeAllay::isValidTarget),
                        new FollowMobTask((entity) -> isTargeting(biomeAllay, entity), (float) biomeAllay.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE)),
                        new RangedApproachTask(1.2F),
                        new MeleeAttackTask(18)),
                MemoryModuleType.ATTACK_TARGET);
    }

    private static boolean isTargeting(BiomeAllay biomeAllay, LivingEntity entity) {
        return biomeAllay.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).filter((entityx) -> entityx == entity).isPresent();
    }

    public static void rememberNoteBlock(LivingEntity allay, BlockPos pos) {
        Brain<?> brain = allay.getBrain();
        GlobalPos globalPos = GlobalPos.create(allay.getWorld().getRegistryKey(), pos);
        Optional<GlobalPos> optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK);
        if (optional.isEmpty()) {
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK, globalPos);
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, 600);
        } else if (optional.get().equals(globalPos)) {
            brain.remember(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, 600);
        }

    }

    private static Optional<LookTarget> getLookTarget(LivingEntity allay) {
        Brain<?> brain = allay.getBrain();
        Optional<GlobalPos> optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK);
        if (optional.isPresent()) {
            GlobalPos globalPos = optional.get();
            if (shouldGoTowardsNoteBlock(allay, brain, globalPos)) {
                return Optional.of(new BlockPosLookTarget(globalPos.getPos().up()));
            }
            brain.forget(MemoryModuleType.LIKED_NOTEBLOCK);
        }
        return getLikedLookTarget(allay);
    }

    private static boolean shouldGoTowardsNoteBlock(LivingEntity allay, Brain<?> brain, GlobalPos globalPos) {
        Optional<Integer> optional = brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS);
        World world = allay.getWorld();
        return world.getRegistryKey() == globalPos.getDimension() && world.getBlockState(globalPos.getPos()).isOf(Blocks.NOTE_BLOCK) && optional.isPresent();
    }

    private static Optional<LookTarget> getLikedLookTarget(LivingEntity allay) {
        return getLikedPlayer(allay).map((player) -> new EntityLookTarget(player, true));
    }

    public static Optional<ServerPlayerEntity> getLikedPlayer(LivingEntity allay) {
        World world = allay.getWorld();
        if (!world.isClient() && world instanceof ServerWorld serverWorld) {
            Optional<UUID> optional = allay.getBrain().getOptionalMemory(MemoryModuleType.LIKED_PLAYER);
            if (optional.isPresent()) {
                Entity entity = serverWorld.getEntity(optional.get());
                if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                    if((serverPlayerEntity.interactionManager.isSurvivalLike() || serverPlayerEntity.interactionManager.isCreative()) && serverPlayerEntity.isInRange(allay, 64.0D)) {
                        return Optional.of(serverPlayerEntity);
                    }
                }
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    static {
        SENSORS = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS);
        MEMORY_MODULES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.VISIBLE_MOBS, MemoryModuleType.ATTACK_TARGET, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM, MemoryModuleType.LIKED_PLAYER, MemoryModuleType.LIKED_NOTEBLOCK, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS);
    }
}
