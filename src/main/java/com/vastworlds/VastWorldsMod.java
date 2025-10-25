package com.vastworlds;

import com.vastworlds.biome.VastBiomes;
import com.vastworlds.biome.VastWorldsRegion;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class VastWorldsMod implements ModInitializer, TerraBlenderApi {
	public static final String MOD_ID = "vastworlds";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Vast Worlds is initializing - Prepare for epic terrain generation!");

		// Initialize biome keys
		VastBiomes.init();

		LOGGER.info("Vast Worlds initialization complete! Biomes are ready.");
	}

	@Override
	public void onTerraBlenderInitialized() {
		LOGGER.info("Registering Vast Worlds region with TerraBlender...");

		// Register our custom region with a weight of 2 (vanilla has weight of 10)
		// This means our biomes will appear alongside vanilla biomes
		Regions.register(new VastWorldsRegion(2));

		LOGGER.info("Vast Worlds region registered successfully!");
		LOGGER.info("Find biomes with /locatebiome vastworlds:mega_plains (or towering_hills, deep_valley)");
	}
}
