package com.bawnorton.wildallays.entity;

import com.bawnorton.wildallays.entity.enums.AllayBiome;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class BiomeAllay extends AllayEntity {

    protected AllayBiome biome = AllayBiome.NONE;

    public BiomeAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
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
        } else {
            BlockPos currentPos = this.getBlockPos();
            RegistryEntry<Biome> entry = this.world.getBiome(currentPos);
            this.biome = AllayBiome.fromRegistry(entry);
        }
        super.tickMovement();
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(Items.ALLAY_SPAWN_EGG);
    }
}
