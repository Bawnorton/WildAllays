package com.bawnorton.wildallays;

import com.bawnorton.wildallays.config.ConfigManager;
import com.bawnorton.wildallays.util.file.Directories;
import com.bawnorton.wildallays.registry.EntityRegister;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildAllays implements ModInitializer {
	public static final String MODID = "wildallays";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		EntityRegister.init();
		EntityRegister.initSpawning();

		LOGGER.info("Wild Allays Initialised");
	}

	static {
		Directories.init();
//		ConfigManager.init();
	}
}
