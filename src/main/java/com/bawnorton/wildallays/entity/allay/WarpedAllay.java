package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WarpedAllay extends BiomeAllay {
    public WarpedAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
        biome = Biome.TAIGA;
    }

    @Override
    protected boolean checkDarkness(BlockPos pos) {
        return true;
    }

    @Override
    protected boolean checkSurface(BlockPos pos) {
        return true;
    }
}
