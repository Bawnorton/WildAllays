package com.bawnorton.wildallays.registry;

import com.bawnorton.wildallays.WildAllays;
import com.bawnorton.wildallays.item.AllayCharm;
import com.bawnorton.wildallays.item.AllayIronMaterial;
import com.bawnorton.wildallays.item.armor.AllayIronHelmet;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ItemRegister {
    public static final ArmorMaterial ALLAY_IRON_ARMOR_MATERIAL = new AllayIronMaterial();
    public static final Item ALLAY_CHARM = new AllayCharm(new FabricItemSettings().group(WildAllays.WILD_ALLAY_GROUP));
    public static final Item ALLAY_IRON_HELMET = new AllayIronHelmet(new FabricItemSettings().group(WildAllays.WILD_ALLAY_GROUP));

    public static void init() {
        Registry.register(Registry.ITEM, "%s:allay_charm".formatted(WildAllays.MODID), ALLAY_CHARM);
        Registry.register(Registry.ITEM, "%s:allay_iron_helmet".formatted(WildAllays.MODID), ALLAY_IRON_HELMET);
    }
}
