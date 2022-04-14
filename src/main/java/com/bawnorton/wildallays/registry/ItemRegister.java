package com.bawnorton.wildallays.registry;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.entity.BiomeAllay;
import com.bawnorton.wildallays.item.AllayIdentifier;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class ItemRegister {

    public static final Item ALLAY_IDENTIFIER = new AllayIdentifier(new FabricItemSettings().group(ItemGroup.TOOLS).maxCount(1));

    public static void init() {
        Registry.register(Registry.ITEM, "%s:allay_identifier".formatted(WildAllays.MODID), ALLAY_IDENTIFIER);
        Registry.register(Registry.ITEM, 632, "%s:biome_allay_spawn_egg".formatted(WildAllays.MODID), BiomeAllay.Type.egg);
    }
}
