package com.bawnorton.wildallays.entity.allay;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.util.Colour;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.world.World;
import net.minecraft.world.level.ColorResolver;

public class WoodedBadlandsAllay extends BiomeAllay {
    public WoodedBadlandsAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
    }
}
