package com.bawnorton.wildallays.registry;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.item.AllayCharm;
import com.bawnorton.wildallays.item.armor.AllayArmorItem;
import com.bawnorton.wildallays.item.armor.AllayChainHelmet;
import com.bawnorton.wildallays.item.armor.AllayGoldHelmet;
import com.bawnorton.wildallays.item.armor.AllayIronHelmet;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class Items {
    public static final Item ALLAY_CHARM = new AllayCharm(new FabricItemSettings().group(WildAllays.WILD_ALLAY_GROUP));
    public static final AllayArmorItem ALLAY_IRON_HELMET = new AllayIronHelmet(new FabricItemSettings().group(WildAllays.WILD_ALLAY_GROUP));
    public static final AllayArmorItem ALLAY_CHAIN_HELMET = new AllayChainHelmet(new FabricItemSettings().group(WildAllays.WILD_ALLAY_GROUP));
    public static final AllayArmorItem ALLAY_GOLD_HELMET = new AllayGoldHelmet(new FabricItemSettings().group(WildAllays.WILD_ALLAY_GROUP));

    public static void register() {
        Registry.register(Registry.ITEM, "%s:allay_charm".formatted(WildAllays.MODID), ALLAY_CHARM);
        Registry.register(Registry.ITEM, "%s:allay_iron_helmet".formatted(WildAllays.MODID), ALLAY_IRON_HELMET);
        Registry.register(Registry.ITEM, "%s:allay_chain_helmet".formatted(WildAllays.MODID), ALLAY_CHAIN_HELMET);
        Registry.register(Registry.ITEM, "%s:allay_gold_helmet".formatted(WildAllays.MODID), ALLAY_GOLD_HELMET);

    }
}
