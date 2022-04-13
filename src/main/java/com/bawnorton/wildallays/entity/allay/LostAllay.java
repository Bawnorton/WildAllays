package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.config.ConfigManager;
import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.util.Colour;
import net.minecraft.block.BlockState;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.level.ColorResolver;

public class LostAllay extends BiomeAllay {

    private Colour toColour = new Colour();

    public LostAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
        biome = Biome.NONE;
    }

    @Override
    public void tick() {
        super.tick();
        if(world.isClient) {
            if(!colour.equals(toColour)) {
                colour.adjustTo(toColour, 7);
            }
        }
    }

    @Override
    protected void spawnParticles() {
        if(ConfigManager.get("lostAllayGivesOffParticles", Boolean.class)) {
            super.spawnParticles();
        }
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        BlockPos currentPos = this.getBlockPos();
        BlockState state = null;
        while(currentPos.getY() > world.getBottomY()) {
            state = world.getBlockState(currentPos);
            Material material = state.getMaterial();
            if(!state.isAir() && material != Material.PLANT && material != Material.REPLACEABLE_PLANT) break;
            currentPos = currentPos.down();
        }
        int allayColour = 1644825;
        if(state != null) {
            ColorResolver resolver = materialColourMap.get(state.getMaterial());
            if(resolver != null) {
                allayColour = world.getColor(currentPos, resolver);
            } else {
                MapColor mapColour = state.getMapColor(world, currentPos);
                allayColour = mapColour.color;
            }
        }
        this.toColour = Colour.fromBinary(allayColour);
        this.biome = Biome.fromRegistry(world.getBiome(currentPos));
    }
}
