package com.vastworlds.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.function.Consumer;

/**
 * Maps Vast Worlds biomes to climate parameters for TerraBlender integration.
 * Climate parameters control where biomes spawn in the world:
 * - Temperature: -1.0 (frozen) to 1.0 (hot)
 * - Humidity: -1.0 (dry) to 1.0 (wet)
 * - Continentalness: -1.0 (oceans) to 1.0 (inland)
 * - Erosion: -1.0 (mountains/peaks) to 1.0 (flat terrain)
 * - Depth: Unused in 1.18+
 * - Weirdness: -1.0 to 1.0 (controls terrain variation)
 */
public class VastWorldsBiomeBuilder {
    private static final float VALLEY_SIZE = 0.05F;
    private static final float LOW_START = 0.26666668F;
    private static final float HIGH_START = 0.4F;
    private static final float HIGH_END = 0.93333334F;
    private static final float PEAK_SIZE = 0.1F;
    private static final float PEAK_START = 0.56666666F;
    private static final float PEAK_END = 0.7666667F;

    // Full parameter range
    private final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);

    // Temperature ranges (5 zones from frozen to hot)
    private final Climate.Parameter[] temperatures = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.45F),     // Frozen
            Climate.Parameter.span(-0.45F, -0.15F),    // Cold
            Climate.Parameter.span(-0.15F, 0.2F),      // Temperate
            Climate.Parameter.span(0.2F, 0.55F),       // Warm
            Climate.Parameter.span(0.55F, 1.0F)        // Hot
    };

    // Humidity ranges (5 zones from dry to wet)
    private final Climate.Parameter[] humidities = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.35F),     // Arid
            Climate.Parameter.span(-0.35F, -0.1F),     // Dry
            Climate.Parameter.span(-0.1F, 0.1F),       // Moderate
            Climate.Parameter.span(0.1F, 0.3F),        // Humid
            Climate.Parameter.span(0.3F, 1.0F)         // Wet
    };

    // Erosion levels (controls terrain height variation)
    private final Climate.Parameter[] erosions = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.78F),     // Erosion 0: Peaks
            Climate.Parameter.span(-0.78F, -0.375F),   // Erosion 1: High terrain
            Climate.Parameter.span(-0.375F, -0.2225F), // Erosion 2: Mid-high terrain
            Climate.Parameter.span(-0.2225F, 0.05F),   // Erosion 3: Mid terrain
            Climate.Parameter.span(0.05F, 0.45F),      // Erosion 4: Low terrain
            Climate.Parameter.span(0.45F, 0.55F),      // Erosion 5: Lower terrain
            Climate.Parameter.span(0.55F, 1.0F)        // Erosion 6: Valleys/flat
    };

    // Continentalness zones
    private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);
    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);

    public void addBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // ONLY add our custom biomes to specific climate zones
        // Vanilla biomes will be handled by Minecraft's default region

        // Add mega_plains - flat terrain (high erosion = flat)
        this.addMegaPlainsBiome(mapper);

        // Add towering_hills - hilly terrain (low erosion = hills/mountains)
        this.addToweringHillsBiome(mapper);

        // Add deep_valley - valleys between mountains
        this.addDeepValleyBiome(mapper);
    }

    /**
     * Mega Plains - appears in flat, inland areas
     * High erosion values = flat terrain
     */
    private void addMegaPlainsBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Add in temperate zones with moderate humidity
        // Use high erosion (flat terrain)
        for (int tempIndex = 1; tempIndex <= 3; tempIndex++) { // Cold to warm
            for (int humidIndex = 1; humidIndex <= 3; humidIndex++) { // Dry to humid
                this.addSurfaceBiome(mapper,
                        this.temperatures[tempIndex],
                        this.humidities[humidIndex],
                        this.farInlandContinentalness,
                        this.erosions[6], // Flattest terrain
                        FULL_RANGE,
                        0.0F,
                        VastBiomes.MEGA_PLAINS);
            }
        }
    }

    /**
     * Towering Hills - appears in hilly/mountainous areas
     * Low erosion values = mountains/hills
     */
    private void addToweringHillsBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Add in temperate to warm zones
        // Use low erosion (hilly terrain)
        for (int tempIndex = 2; tempIndex <= 3; tempIndex++) { // Temperate to warm
            for (int humidIndex = 1; humidIndex <= 3; humidIndex++) { // Dry to humid
                this.addSurfaceBiome(mapper,
                        this.temperatures[tempIndex],
                        this.humidities[humidIndex],
                        this.inlandContinentalness,
                        this.erosions[1], // High, hilly terrain
                        FULL_RANGE,
                        0.0F,
                        VastBiomes.TOWERING_HILLS);
            }
        }
    }

    /**
     * Deep Valley - appears in valleys (very low weirdness)
     * Can appear with various erosion but prefers valley-like weirdness
     */
    private void addDeepValleyBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Valley weirdness parameter (-0.05 to 0.05)
        Climate.Parameter valleyWeirdness = Climate.Parameter.span(-VALLEY_SIZE, VALLEY_SIZE);

        // Add in moderate temperature zones
        for (int tempIndex = 1; tempIndex <= 3; tempIndex++) {
            for (int humidIndex = 1; humidIndex <= 3; humidIndex++) {
                this.addSurfaceBiome(mapper,
                        this.temperatures[tempIndex],
                        this.humidities[humidIndex],
                        this.midInlandContinentalness,
                        this.erosions[3], // Mid terrain
                        valleyWeirdness,  // Valley formation
                        0.0F,
                        VastBiomes.DEEP_VALLEY);
            }
        }
    }

    /**
     * Helper method to add a biome with the specified climate parameters
     */
    private void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper,
                                  Climate.Parameter temperature,
                                  Climate.Parameter humidity,
                                  Climate.Parameter continentalness,
                                  Climate.Parameter erosion,
                                  Climate.Parameter weirdness,
                                  float offset,
                                  ResourceKey<Biome> biome) {
        mapper.accept(Pair.of(
                Climate.parameters(temperature, humidity, continentalness, erosion,
                        Climate.Parameter.point(0.0F), weirdness, offset),
                biome));
    }
}
