package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;

public class FlowerAllay extends BiomeAllay {
    public FlowerAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
        biome = Biome.FLOWER_FOREST;
    }
}
