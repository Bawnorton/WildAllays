package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.entity.enums.Biome;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;

public class ForestAllay extends BiomeAllay {
    public ForestAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
        biome = Biome.FOREST;
    }
}
