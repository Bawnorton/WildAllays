package com.bawnorton.wildallays.entity.allays;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.entity.attribute.Biome;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;

public class CrimsonAllay extends BiomeAllay {
    public CrimsonAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
        biome = Biome.CRIMSON_FOREST;
    }
}
