package com.bawnorton.wildallays.entity;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.entity.enums.Allay;
import com.bawnorton.wildallays.entity.enums.Biome;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class BiomeAllay extends AllayEntity {

    protected Biome biome = Biome.NONE;

    public BiomeAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
        return super.canSpawn(world, spawnReason);
    }

    private boolean checkSurface(BlockPos pos) {
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

    @Override
    public boolean canSpawn(WorldView world) {
        if(world.getDimension().hasSkyLight()) {
            BlockPos pos = this.getBlockPos();
            boolean spawnInDark = this.world.isNight() && world.getLightLevel(LightType.BLOCK, pos) < 1;
            boolean surfaceSpawn = checkSurface(pos);
            return spawnInDark && surfaceSpawn && super.canSpawn(world);
        }
        return super.canSpawn(world);
    }

    @Override
    public void tickMovement() {
        if(this.world.isClient) {
            if(this.random.nextInt(20) == 0) {
                this.world.addParticle(ParticleTypes.END_ROD,
                        this.getParticleX(1.2D),
                        this.getRandomBodyY(),
                        this.getParticleZ(1.2D),
                        (this.random.nextInt(21) - 10) / 100D,
                        (this.random.nextInt(21) - 10) / 100D,
                        (this.random.nextInt(21) - 10) / 100D);
            }
        }
        super.tickMovement();
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(Allay.egg);
    }
}
