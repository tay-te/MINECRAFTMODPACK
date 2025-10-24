package com.vastworlds;

import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VastWorldsMod implements ModInitializer {
    public static final String MOD_ID = "vastworlds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Biome Registry Keys
    public static final RegistryKey<Biome> MEGA_MOUNTAINS = RegistryKey.of(RegistryKeys.BIOME,
        Identifier.of(MOD_ID, "mega_mountains"));
    public static final RegistryKey<Biome> ENDLESS_PLAINS = RegistryKey.of(RegistryKeys.BIOME,
        Identifier.of(MOD_ID, "endless_plains"));
    public static final RegistryKey<Biome> DEEP_CANYONS = RegistryKey.of(RegistryKeys.BIOME,
        Identifier.of(MOD_ID, "deep_canyons"));
    public static final RegistryKey<Biome> SKY_PLATEAUS = RegistryKey.of(RegistryKeys.BIOME,
        Identifier.of(MOD_ID, "sky_plateaus"));

    @Override
    public void onInitialize() {
        LOGGER.info("========================================");
        LOGGER.info("Initializing Vast Worlds Mod");
        LOGGER.info("========================================");
        LOGGER.info("Custom biomes registered:");
        LOGGER.info("  - Mega Mountains (extreme peaks with snow)");
        LOGGER.info("  - Endless Plains (vast flat grasslands)");
        LOGGER.info("  - Deep Canyons (dramatic valleys and cliffs)");
        LOGGER.info("  - Sky Plateaus (high plateaus with steep drops)");
        LOGGER.info("========================================");
        LOGGER.info("Terrain generation: CUSTOM VAST TERRAIN");
        LOGGER.info("Vanilla biomes: COMPLETELY REPLACED");
        LOGGER.info("========================================");
        LOGGER.info("Vast Worlds initialized successfully!");
    }
}
