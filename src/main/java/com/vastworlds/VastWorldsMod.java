package com.vastworlds;

import com.vastworlds.biome.ModBiomes;
import com.vastworlds.worldgen.VastWorldsSurfaceRules;
import com.vastworlds.worldgen.carver.VastWorldsCarvers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod class for Vast Worlds.
 * Refactored to use BiomesOPlenty patterns with TerraBlender integration.
 */
@Mod(VastWorldsMod.MOD_ID)
public class VastWorldsMod {
    public static final String MODID = "vastworlds";
    public static final String MOD_ID = MODID;  // Backwards compatibility
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    public VastWorldsMod(IEventBus modEventBus) {
        LOGGER.info("========================================");
        LOGGER.info("Initializing Vast Worlds Mod");
        LOGGER.info("========================================");

        // Initialize biome registry
        ModBiomes.setup();

        // Register custom carvers
        VastWorldsCarvers.register(modEventBus);

        // Register setup event for TerraBlender integration
        modEventBus.addListener(this::commonSetup);

        LOGGER.info("Custom biomes registered:");
        LOGGER.info("  - Mega Mountains (extreme peaks with snow)");
        LOGGER.info("  - Endless Plains (vast flat grasslands)");
        LOGGER.info("  - Deep Canyons (dramatic valleys and cliffs)");
        LOGGER.info("  - Sky Plateaus (high plateaus with steep drops)");
        LOGGER.info("========================================");
        LOGGER.info("Terrain generation: BiomesOPlenty-style system");
        LOGGER.info("Using TerraBlender for biome distribution");
        LOGGER.info("Terrain slices: Peaks, High, Mid, Low, Valleys");
        LOGGER.info("========================================");
    }

    /**
     * Common setup event - registers TerraBlender region and surface rules.
     * This runs after registries are available.
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Running common setup for Vast Worlds...");

            // Setup TerraBlender integration (registers regions)
            ModBiomes.setupTerraBlender();

            // Register surface rules
            VastWorldsSurfaceRules.registerSurfaceRules();

            LOGGER.info("========================================");
            LOGGER.info("Vast Worlds initialized successfully!");
            LOGGER.info("========================================");
        });
    }

    /**
     * Setup TerraBlender - called from platform-specific setup
     */
    public static void setupTerraBlender() {
        ModBiomes.setupTerraBlender();
    }
}
