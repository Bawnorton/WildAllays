package com.bawnorton.wildallays;

import com.bawnorton.wildallays.config.ConfigManager;
import com.bawnorton.wildallays.registry.Entities;
import com.bawnorton.wildallays.registry.Items;
import com.bawnorton.wildallays.util.file.Directories;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildAllays implements ModInitializer {
	public static final String MODID = "wildallays";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static final ItemGroup WILD_ALLAY_GROUP = FabricItemGroupBuilder.create(
			new Identifier(MODID, "wild_allays_group")).icon(() -> new ItemStack(Items.ALLAY_CHARM)).build();

	@Override
	public void onInitialize() {
		Entities.register();
		Items.register();

		LOGGER.info("Wild Allays Initialised");
	}

	static {
		Directories.init();
		ConfigManager.init();
	}
}
