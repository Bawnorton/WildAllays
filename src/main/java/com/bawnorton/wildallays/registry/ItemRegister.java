package com.bawnorton.wildallays.registry;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.item.AllayCharm;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.registry.Registry;

public class ItemRegister {
    public static final AllayCharm ALLAY_CHARM = new AllayCharm(new FabricItemSettings().group(ItemGroup.MISC));

    public static void init() {
        Registry.register(Registry.ITEM, "%s:allay_charm".formatted(WildAllays.MODID), ALLAY_CHARM);
    }
}
