package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.entity.enums.AllayBiome;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;

public class WarpedAllay extends BiomeAllay {
    public WarpedAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
        biome = AllayBiome.TAIGA;
    }
}
