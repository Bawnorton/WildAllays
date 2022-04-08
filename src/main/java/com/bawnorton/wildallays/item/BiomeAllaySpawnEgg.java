package com.bawnorton.wildallays.item;

import com.bawnorton.wildallays.entity.enums.Allay;
import com.bawnorton.wildallays.reflect.Reflection;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;

import java.util.List;

public class BiomeAllaySpawnEgg extends SpawnEggItem {
    public BiomeAllaySpawnEgg() {
        super(Allay.DEFAULT.type, 0x00DAFF, 0x00ADFF, (new Item.Settings()).group(ItemGroup.MISC));
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos block = context.getBlockPos();
        RegistryEntry<Biome> biome = context.getWorld().getBiome(block);
        outer: for(Allay allay: Allay.values()) {
            List<Identifier> identifiers = allay.biome.getIdentifiers();
            for(Identifier identifier : identifiers) {
                if(biome.matchesId(identifier)) {
                    Reflection.setField(this, allay.type, "type");
                    break outer;
                }
            }
        }
        return super.useOnBlock(context);
    }
}
