package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.util.Colour;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;

import java.util.List;

public class BirchAllay extends BiomeAllay {
    public BirchAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
    }
}
