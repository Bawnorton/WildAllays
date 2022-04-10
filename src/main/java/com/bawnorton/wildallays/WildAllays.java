package com.bawnorton.wildallays;

import com.bawnorton.wildallays.registry.EntityRegister;
import net.fabricmc.api.ModInitializer;
import net.minecraft.command.argument.NbtPathArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.structure.Structure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public class WildAllays implements ModInitializer {
	public static final String MODID = "wildallays";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		EntityRegister.init();
		EntityRegister.initSpawning();

		LOGGER.info("Wild Allays Initialised");
	}
}
