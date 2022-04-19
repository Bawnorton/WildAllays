package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;

public class WoodedBadlandsAllay extends BiomeAllay {
    public WoodedBadlandsAllay(EntityType<? extends BiomeAllay> entityType, World world) {
        super(entityType, world);
    }
}
