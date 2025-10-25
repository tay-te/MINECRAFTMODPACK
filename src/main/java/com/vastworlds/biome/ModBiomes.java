package com.vastworlds.biome;

import com.vastworlds.VastWorldsMod;
import com.vastworlds.worldgen.VastWorldsRegion;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import terrablender.api.Regions;

/**
 * Biome registration and setup for Vast Worlds.
 * Follows BiomesOPlenty's pattern for biome management.
 */
public class ModBiomes {

    /**
     * Initialize biome registry - called during mod construction
     */
    public static void setup() {
        VastWorldsMod.LOGGER.info("Registering Vast Worlds biomes...");
    }

    /**
     * Setup TerraBlender integration - called during common setup
     */
    public static void setupTerraBlender() {
        VastWorldsMod.LOGGER.info("Setting up TerraBlender integration...");

        // Register our region with TerraBlender (weight 5 = significant presence)
        Regions.register(new VastWorldsRegion(5));

        VastWorldsMod.LOGGER.info("Registered Vast Worlds region with TerraBlender (weight: 5)");
    }

    /**
     * Bootstrap biomes - generates biome JSON files during datagen.
     * Following BiomesOPlenty's pattern.
     * This method is called during the datagen process, NOT at runtime.
     */
    public static void bootstrapBiomes(BootstrapContext<Biome> context) {
        HolderGetter<ConfiguredWorldCarver<?>> carverGetter = context.lookup(Registries.CONFIGURED_CARVER);
        HolderGetter<PlacedFeature> placedFeatureGetter = context.lookup(Registries.PLACED_FEATURE);

        VastWorldsMod.LOGGER.info("Bootstrapping Vast Worlds biomes for datagen...");

        // Register our custom biomes using the factory methods
        register(context, VastWorldsBiomes.MEGA_MOUNTAINS,
                VastWorldsOverworldBiomes.megaMountains(placedFeatureGetter, carverGetter));

        register(context, VastWorldsBiomes.ENDLESS_PLAINS,
                VastWorldsOverworldBiomes.endlessPlains(placedFeatureGetter, carverGetter));

        register(context, VastWorldsBiomes.DEEP_CANYONS,
                VastWorldsOverworldBiomes.deepCanyons(placedFeatureGetter, carverGetter));

        register(context, VastWorldsBiomes.SKY_PLATEAUS,
                VastWorldsOverworldBiomes.skyPlateaus(placedFeatureGetter, carverGetter));

        VastWorldsMod.LOGGER.info("Bootstrapped {} custom biomes for datagen", 4);
    }

    /**
     * Register a biome to the bootstrap context during datagen
     */
    private static void register(BootstrapContext<Biome> context, ResourceKey<Biome> key, Biome biome) {
        context.register(key, biome);
    }
}
