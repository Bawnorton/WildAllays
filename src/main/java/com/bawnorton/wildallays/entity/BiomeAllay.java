package com.bawnorton.wildallays.entity;

import com.bawnorton.wildallays.entity.attribute.Biome;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;

public abstract class BiomeAllay extends AllayEntity {

    protected Biome biome = Biome.NONE;

    public BiomeAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
    }
}
