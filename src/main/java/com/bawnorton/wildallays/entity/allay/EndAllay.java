package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.util.Colour;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EndAllay extends BiomeAllay {
    public EndAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void setColour() {
        colour = new Colour(92, 61, 94);
    }

    @Override
    protected boolean checkDarkness(BlockPos pos) {
        return true;
    }
}
