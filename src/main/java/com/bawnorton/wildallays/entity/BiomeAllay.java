package com.bawnorton.wildallays.entity;

import com.bawnorton.wildallays.config.ConfigManager;
import com.bawnorton.wildallays.entity.ai.BiomeAllayBrain;
import com.bawnorton.wildallays.entity.allay.*;
import com.bawnorton.wildallays.registry.Items;
import com.bawnorton.wildallays.util.Colour;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.EntityLookTarget;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.brain.task.UpdateAttackTargetTask;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.ProjectileDamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.GameEventTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.EntityGameEventHandler;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.world.event.listener.VibrationListener;
import net.minecraft.world.level.ColorResolver;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static com.bawnorton.wildallays.registry.Entities.*;
import static net.minecraft.item.Items.*;

public abstract class BiomeAllay extends PathAwareEntity implements InventoryOwner, VibrationListener.Callback {
    private static final Logger field_39045 = LogUtils.getLogger();
    private static final Vec3i ITEM_PICKUP_RANGE_EXPANDER = new Vec3i(1, 1, 1);
    private final EntityGameEventHandler<VibrationListener> gameEventHandler;
    private final SimpleInventory inventory = new SimpleInventory(1);
    private float field_38935;
    private float field_38936;

    protected static final Hashtable<Material, ColorResolver> materialColourMap;
    protected Colour colour = new Colour();

    public BiomeAllay(EntityType<? extends BiomeAllay> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.setCanPickUpLoot(this.canPickUpLoot());
        this.gameEventHandler = new EntityGameEventHandler<>(new VibrationListener(
                        new EntityPositionSource(this, this.getStandingEyeHeight()),
                        16, this, null, 0, 0));
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
        return new ItemStack(ALLAY_SPAWN_EGG);
    }

    public Colour getColor() {
        return colour;
    }

    protected Brain<?> deserializeBrain(Dynamic<?> dynamic) {
        return BiomeAllayBrain.create(this, dynamic);
    }

    @SuppressWarnings("unchecked")
    public Brain<BiomeAllay> getBrain() {
        return (Brain<BiomeAllay>) super.getBrain();
    }

    public static DefaultAttributeContainer.Builder createAllayAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 0.1D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.5D)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    protected EntityNavigation createNavigation(World world) {
        BirdNavigation birdNavigation = new BirdNavigation(this, world);
        birdNavigation.setCanPathThroughDoors(false);
        birdNavigation.setCanSwim(true);
        birdNavigation.setCanEnterOpenDoors(true);
        return birdNavigation;
    }

    public void travel(Vec3d movementInput) {
        if (this.canMoveVoluntarily() || this.isLogicalSideForUpdatingMovement()) {
            if (this.isTouchingWater()) {
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.800000011920929D));
            } else if (this.isInLava()) {
                this.updateVelocity(0.02F, movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.5D));
            } else {
                this.updateVelocity(this.getMovementSpeed(), movementInput);
                this.move(MovementType.SELF, this.getVelocity());
                this.setVelocity(this.getVelocity().multiply(0.9100000262260437D));
            }
        }
        this.updateLimbs(this, false);
    }

    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.6F;
    }

    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
        return false;
    }

    public boolean isValidTarget(@Nullable Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            return !EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(entity) || this.isTeammate(entity) || livingEntity.getType() == EntityType.ARMOR_STAND || livingEntity.getType() == EntityType.WARDEN || livingEntity.isInvulnerable() || livingEntity.isDead() || !this.world.getWorldBorder().contains(livingEntity.getBoundingBox());
        }
        return true;
    }

    @Nullable
    public LivingEntity getTarget() {
        return this.getBrain().getOptionalMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    public boolean damage(DamageSource source, float amount) {
        if (this.world.isClient) return false;
        Entity attacker = source.getAttacker();
        if (attacker instanceof PlayerEntity playerEntity) {
            Optional<UUID> optional = this.getBrain().getOptionalMemory(MemoryModuleType.LIKED_PLAYER);
            if (optional.isPresent() && playerEntity.getUuid().equals(optional.get())) {
                return false;
            }
        }
        boolean damaged = super.damage(source, amount);
        if(damaged) {
            if (this.brain.getOptionalMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && attacker instanceof LivingEntity livingEntity) {
                if(!(source instanceof ProjectileDamageSource) || this.isInRange(livingEntity, 16.0D)) {
                    UpdateAttackTargetTask.updateAttackTarget(this, livingEntity);
                }
            }
        }
        return damaged;
    }

    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
    }

    protected SoundEvent getAmbientSound() {
        return this.hasStackEquipped(EquipmentSlot.MAINHAND) ? SoundEvents.ENTITY_ALLAY_AMBIENT_WITH_ITEM : SoundEvents.ENTITY_ALLAY_AMBIENT_WITHOUT_ITEM;
    }

    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ALLAY_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ALLAY_DEATH;
    }

    protected float getSoundVolume() {
        return 0.4F;
    }

    protected void attack() {
        LivingEntity target = this.getTarget();
        if(target != null) {
            if(target.isDead()) {
                this.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
                return;
            }
            if(canSee(target)) {
                this.getBrain().remember(MemoryModuleType.LOOK_TARGET, new EntityLookTarget(target, true));
                this.lookAtEntity(target, 360, 360);
                this.tryAttack(target);
                this.getBrain().forget(MemoryModuleType.ATTACK_TARGET);
            }
        }
    }

    @Override
    protected void mobTick() {
        this.world.getProfiler().push("biomeAllayBrain");
        this.getBrain().tick((ServerWorld)this.world, this);
        this.world.getProfiler().pop();
        this.world.getProfiler().push("biomeAllayActivityUpdate");
        BiomeAllayBrain.tick(this);
        this.world.getProfiler().pop();
        super.mobTick();
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
        super.tickMovement();
        if (this.world.isClient) {
            if (this.random.nextInt(20) == 0) {
                spawnParticles();
            }
        } else if (this.isAlive() && this.age % 10 == 0) {
            this.heal(1.0F);
        }
    }

    public boolean tryAttack(Entity target) {
        this.world.sendEntityStatus(this, (byte)4);
        return super.tryAttack(target);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.world.isClient) {
            this.field_38936 = this.field_38935;
            if (this.isHoldingItem()) {
                this.field_38935 = MathHelper.clamp(this.field_38935 + 1.0F, 0.0F, 5.0F);
            } else {
                this.field_38935 = MathHelper.clamp(this.field_38935 - 1.0F, 0.0F, 5.0F);
            }
        } else {
            this.gameEventHandler.getListener().tick(this.world);
        }
    }

    public boolean canPickUpLoot() {
        return !this.isItemPickupCoolingDown() && !this.getStackInHand(Hand.MAIN_HAND).isEmpty();
    }

    public boolean isHoldingItem() {
        return !this.getStackInHand(Hand.MAIN_HAND).isEmpty();
    }

    private boolean isItemPickupCoolingDown() {
        return this.getBrain().isMemoryInState(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS, MemoryModuleState.VALUE_PRESENT);
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack playerStack = player.getStackInHand(hand);
        ItemStack allayStack = this.getStackInHand(hand);
        ItemStack allayHead = this.getEquippedStack(EquipmentSlot.HEAD);
        outer: if(allayStack.isEmpty()) {
            if(!playerStack.isEmpty() && allayHead.isEmpty() && !player.isSneaking()) {
                ItemStack helmet;

                if (playerStack.getItem() == LEATHER_HELMET) {
                    helmet = Items.ALLAY_LEATHER_HELMET.fromVanillaItem(playerStack);
                } else if(playerStack.getItem() == GOLDEN_HELMET) {
                    helmet = Items.ALLAY_GOLD_HELMET.fromVanillaItem(playerStack);
                } else if (playerStack.getItem() == CHAINMAIL_HELMET) {
                    helmet = Items.ALLAY_CHAIN_HELMET.fromVanillaItem(playerStack);
                } else if (playerStack.getItem() == IRON_HELMET) {
                    helmet = Items.ALLAY_IRON_HELMET.fromVanillaItem(playerStack);
                } else if (playerStack.getItem() == DIAMOND_HELMET) {
                    helmet = Items.ALLAY_DIAMOND_HELMET.fromVanillaItem(playerStack);
                } else if (playerStack.getItem() == NETHERITE_HELMET) {
                    helmet = Items.ALLAY_NETHERITE_HELMET.fromVanillaItem(playerStack);
                } else {
                    break outer;
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

                if (allayHead.getItem() == Items.ALLAY_LEATHER_HELMET) {
                    helmet = Items.ALLAY_LEATHER_HELMET.toVanillaItem(allayHead);
                } else if (allayHead.getItem() == Items.ALLAY_GOLD_HELMET) {
                    helmet = Items.ALLAY_GOLD_HELMET.toVanillaItem(allayHead);
                } else if (allayHead.getItem() == Items.ALLAY_CHAIN_HELMET) {
                    helmet = Items.ALLAY_CHAIN_HELMET.toVanillaItem(allayHead);
                } else if(allayHead.getItem() == Items.ALLAY_IRON_HELMET) {
                    helmet = Items.ALLAY_IRON_HELMET.toVanillaItem(allayHead);
                } else if (allayHead.getItem() == Items.ALLAY_DIAMOND_HELMET) {
                    helmet = Items.ALLAY_DIAMOND_HELMET.toVanillaItem(allayHead);
                } else if (allayHead.getItem() == Items.ALLAY_NETHERITE_HELMET) {
                    helmet = Items.ALLAY_NETHERITE_HELMET.toVanillaItem(allayHead);
                }

                player.giveItemStack(helmet);
                return ActionResult.SUCCESS;
            }
        }
        if (allayStack.isEmpty() && !playerStack.isEmpty()) {
            ItemStack itemStack3 = playerStack.copy();
            itemStack3.setCount(1);
            this.setStackInHand(Hand.MAIN_HAND, itemStack3);
            if (!player.getAbilities().creativeMode) {
                playerStack.decrement(1);
            }

            this.world.playSoundFromEntity(player, this, SoundEvents.ENTITY_ALLAY_ITEM_GIVEN, SoundCategory.NEUTRAL, 2.0F, 1.0F);
            this.getBrain().remember(MemoryModuleType.LIKED_PLAYER, player.getUuid());
            return ActionResult.SUCCESS;
        } else if (!allayStack.isEmpty() && hand == Hand.MAIN_HAND && playerStack.isEmpty()) {
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            this.world.playSoundFromEntity(player, this, SoundEvents.ENTITY_ALLAY_ITEM_TAKEN, SoundCategory.NEUTRAL, 2.0F, 1.0F);
            this.swingHand(Hand.MAIN_HAND);

            for (ItemStack itemStack4 : this.getInventory().clearToList()) {
                LookTargetUtil.give(this, itemStack4, this.getPos());
            }

            if(this.getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
                this.getBrain().forget(MemoryModuleType.LIKED_PLAYER);
            }
            player.giveItemStack(allayStack);
            return ActionResult.SUCCESS;
        } else {
            return super.interactMob(player, hand);
        }
    }

    public SimpleInventory getInventory() {
        return this.inventory;
    }

    protected Vec3i getItemPickUpRangeExpander() {
        return ITEM_PICKUP_RANGE_EXPANDER;
    }

    public boolean canGather(ItemStack stack) {
        ItemStack itemStack = this.getStackInHand(Hand.MAIN_HAND);
        return !itemStack.isEmpty() && itemStack.isItemEqual(stack) && this.inventory.canInsert(itemStack);
    }

    protected void loot(ItemEntity item) {
        InventoryOwner.pickUpItem(this, this, item);
    }

    protected void sendAiDebugData() {
        super.sendAiDebugData();
        DebugInfoSender.sendBrainDebugData(this);
    }

    public boolean hasWings() {
        return !this.isOnGround();
    }

    public void updateEventHandler(BiConsumer<EntityGameEventHandler<?>, ServerWorld> callback) {
        World var3 = this.world;
        if (var3 instanceof ServerWorld serverWorld) {
            callback.accept(this.gameEventHandler, serverWorld);
        }
    }

    public boolean method_43395() {
        return this.limbDistance > 0.3F;
    }

    public float method_43397(float f) {
        return MathHelper.lerp(f, this.field_38936, this.field_38935) / 5.0F;
    }

    protected void dropInventory() {
        super.dropInventory();
        this.inventory.clearToList().forEach(this::dropStack);
        ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
            this.dropStack(itemStack);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    public boolean canImmediatelyDespawn(double distanceSquared) {
        return false;
    }

    public boolean accepts(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, GameEvent.Emitter emitter) {
        if (this.world == world && !this.isRemoved() && !this.isAiDisabled()) {
            if (!this.brain.hasMemoryModule(MemoryModuleType.LIKED_NOTEBLOCK)) {
                return true;
            } else {
                Optional<GlobalPos> optional = this.brain.getOptionalMemory(MemoryModuleType.LIKED_NOTEBLOCK);
                return optional.isPresent() && optional.get().getDimension() == world.getRegistryKey() && optional.get().getPos().equals(pos);
            }
        } else {
            return false;
        }
    }

    public void accept(ServerWorld world, GameEventListener listener, BlockPos pos, GameEvent event, @Nullable Entity entity, @Nullable Entity sourceEntity, int delay) {
        if (event == GameEvent.NOTE_BLOCK_PLAY) {
            BiomeAllayBrain.rememberNoteBlock(this, new BlockPos(pos));
        }

    }

    public TagKey<GameEvent> getTag() {
        return GameEventTags.ALLAY_CAN_LISTEN;
    }

    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.put("Inventory", this.inventory.toNbtList());
        DataResult<NbtElement> var10000 = VibrationListener.createCodec(this).encodeStart(NbtOps.INSTANCE, this.gameEventHandler.getListener());
        Logger var10001 = field_39045;
        Objects.requireNonNull(var10001);
        var10000.resultOrPartial(var10001::error).ifPresent((nbtElement) -> nbt.put("listener", nbtElement));
    }

    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.inventory.readNbtList(nbt.getList("Inventory", 10));
        if (nbt.contains("listener", 10)) {
            DataResult<VibrationListener> var10000 = VibrationListener.createCodec(this).parse(new Dynamic<>(NbtOps.INSTANCE, nbt.getCompound("listener")));
            Logger var10001 = field_39045;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((vibrationListener) -> this.gameEventHandler.setListener(vibrationListener, this.world));
        }
    }

    protected boolean method_43689() {
        return false;
    }

    static {
        materialColourMap = new Hashtable<>() {{
            put(Material.WATER, BiomeColors.WATER_COLOR);
            put(Material.LEAVES, BiomeColors.FOLIAGE_COLOR);
            put(Material.SOLID_ORGANIC, BiomeColors.GRASS_COLOR);
        }};
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
