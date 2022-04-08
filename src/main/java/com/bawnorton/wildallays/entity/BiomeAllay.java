package com.bawnorton.wildallays.entity;

import com.bawnorton.wildallays.entity.enums.Allay;
import com.bawnorton.wildallays.entity.enums.Biome;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BiomeAllay extends AllayEntity {

    protected Biome biome = Biome.NONE;

    public BiomeAllay(EntityType<? extends AllayEntity> entityType, World world) {
        super(entityType, world);
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return new ItemStack(Allay.egg);
    }
}
