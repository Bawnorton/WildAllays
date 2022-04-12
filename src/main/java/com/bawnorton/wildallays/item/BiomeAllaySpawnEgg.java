package com.bawnorton.wildallays.item;

import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.util.reflect.Reflection;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import static com.bawnorton.wildallays.registry.EntityRegister.LOST_ALLAY;

public class BiomeAllaySpawnEgg extends SpawnEggItem {
    public BiomeAllaySpawnEgg() {
        super(LOST_ALLAY, 0x00DAFF, 0x00ADFF, (new Item.Settings()).group(ItemGroup.MISC));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos block = context.getBlockPos();
        RegistryEntry<Biome> biome = context.getWorld().getBiome(block);
        BiomeAllay.Type allay = BiomeAllay.Type.fromBiome(biome);
        Reflection.setField(this, allay.entityType, "type");
        return super.useOnBlock(context);
    }
}
