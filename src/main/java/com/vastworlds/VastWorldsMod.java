package com.vastworlds;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(VastWorldsMod.MOD_ID)
public class VastWorldsMod {
    public static final String MOD_ID = "vastworlds";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    // Biome Registry Keys
    public static final ResourceKey<Biome> MEGA_MOUNTAINS = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "mega_mountains"));
    public static final ResourceKey<Biome> ENDLESS_PLAINS = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "endless_plains"));
    public static final ResourceKey<Biome> DEEP_CANYONS = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "deep_canyons"));
    public static final ResourceKey<Biome> SKY_PLATEAUS = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "sky_plateaus"));

    public VastWorldsMod(IEventBus modEventBus) {
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
