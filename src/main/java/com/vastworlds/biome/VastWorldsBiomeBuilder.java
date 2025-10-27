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

    // Continentalness zones - far from oceans for huge inland biomes
    private final Climate.Parameter inlandContinentalness = Climate.Parameter.span(0.0F, 0.7F);
    private final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.2F, 0.6F); // Moved inland to avoid oceans
    private final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);

    // Coastal/island zones for dramatic coastal cliffs
    private final Climate.Parameter coastalContinentalness = Climate.Parameter.span(-0.4F, -0.15F); // Near ocean
    private final Climate.Parameter islandContinentalness = Climate.Parameter.span(-0.3F, -0.1F); // Island positions

    public void addBiomes(Registry<Biome> biomeRegistry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // ONLY add our custom biomes to specific climate zones
        // Vanilla biomes will be handled by Minecraft's default region

        // Add mega_plains - flat terrain (high erosion = flat)
        this.addMegaPlainsBiome(mapper);

        // Add towering_hills - hilly terrain (low erosion = hills/mountains)
        this.addToweringHillsBiome(mapper);

        // Add deep_valley - valleys between mountains
        this.addDeepValleyBiome(mapper);

        // Add misty_cascade_gorge - tropical waterfall gorges
        this.addMistyCascadeGorgeBiome(mapper);
    }

    /**
     * Mega Plains - appears in rolling hills terrain (like tea plantations)
     * Mid erosion values = gentle rolling hills with large-scale undulations
     * HUGE continuous biome spanning wide climate ranges
     * Now uses custom density functions for massive rolling hills effect
     */
    private void addMegaPlainsBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Expanded parameter range for larger biome presence
        Climate.Parameter plainsTempRange = Climate.Parameter.span(-0.3F, 0.4F); // Cool to warm temperate
        Climate.Parameter plainsHumidityRange = Climate.Parameter.span(-0.3F, 0.3F); // Dry to humid

        this.addSurfaceBiome(mapper,
                plainsTempRange,
                plainsHumidityRange,
                this.farInlandContinentalness,
                this.erosions[3], // Mid terrain for rolling hills
                FULL_RANGE,
                0.0F,
                VastBiomes.MEGA_PLAINS);
    }

    /**
     * Towering Hills - appears in hilly/mountainous areas
     * Low erosion values = mountains/hills
     * HUGE continuous mountainous biome
     * Now with MORE DISTINCT parameters to avoid spawning too close to other biomes
     */
    private void addToweringHillsBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // More focused parameter range - cooler temperatures and drier conditions
        Climate.Parameter hillsTempRange = Climate.Parameter.span(-0.5F, 0.1F); // Cold to temperate
        Climate.Parameter hillsHumidityRange = Climate.Parameter.span(-0.4F, 0.0F); // Dry to moderate

        this.addSurfaceBiome(mapper,
                hillsTempRange,
                hillsHumidityRange,
                this.inlandContinentalness,
                this.erosions[0], // Highest peaks for TOWERING hills
                FULL_RANGE,
                0.0F,
                VastBiomes.TOWERING_HILLS);
    }

    /**
     * Deep Valley - appears as dramatic coastal cliffs and island mountains
     * Based on reference image: towering jagged peaks dropping to ocean
     * Spawns in coastal/near-ocean areas with high erosion (mountains/ridges)
     * Climate: temperate to warm, moderate to high humidity (green vegetation)
     */
    private void addDeepValleyBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Temperate to warm temperatures for green vegetation on slopes
        Climate.Parameter valleyTempRange = Climate.Parameter.span(0.1F, 0.5F); // Cool temperate to warm

        // Moderate to high humidity for lush vegetation
        Climate.Parameter valleyHumidityRange = Climate.Parameter.span(0.2F, 0.8F); // Humid

        // High erosion creates the dramatic peaks and valleys
        // We want ridges and jagged terrain, so use low erosion (mountains)
        Climate.Parameter mountainousErosion = this.erosions[0]; // Peaks/ridges

        // Mid-range weirdness for varied terrain
        Climate.Parameter variedWeirdness = Climate.Parameter.span(-0.3F, 0.3F);

        // Coastal placement - near oceans for dramatic coastal cliffs
        this.addSurfaceBiome(mapper,
                valleyTempRange,
                valleyHumidityRange,
                this.coastalContinentalness, // -0.4 to -0.15: coastal/near-ocean
                mountainousErosion,
                variedWeirdness,
                0.0F,
                VastBiomes.DEEP_VALLEY);

        // Also spawn on islands (like in reference image)
        this.addSurfaceBiome(mapper,
                valleyTempRange,
                valleyHumidityRange,
                this.islandContinentalness, // -0.3 to -0.1: island positions
                mountainousErosion,
                variedWeirdness,
                0.0F,
                VastBiomes.DEEP_VALLEY);
    }

    /**
     * Misty Cascade Gorge - Terraced mountain landscape with rice paddy-style lakes
     *
     * REDESIGNED for terraced terrain aesthetic:
     * - Spawns in mid-to-high elevation zones (erosion 2-4) for terracing effect
     * - Hot, humid climate (tropical/subtropical) for lush vegetation
     * - Large continuous patches via wide parameter ranges
     * - Custom density functions create stepped terrain with small lakes
     * - Focuses on mid-inland to far-inland (no coastal) for mountain valleys
     *
     * This creates a biome that looks like terraced rice paddies or natural
     * mountain lakes at different elevations, NOT continuous waterfalls.
     */
    private void addMistyCascadeGorgeBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Warm to hot temperature - hot dry climate
        Climate.Parameter warmTempRange = Climate.Parameter.span(0.4F, 1.0F);

        // Dry to moderate humidity - canyon/gorge conditions
        Climate.Parameter wetHumidityRange = Climate.Parameter.span(0.0F, 0.5F);

        // Inland elevated terrain - MUST be >= 0.5 to prevent water flooding
        Climate.Parameter inlandContinentalness = Climate.Parameter.span(0.5F, 0.9F);

        // Wide weirdness range for variety
        Climate.Parameter wideWeirdness = Climate.Parameter.span(-0.8F, 0.8F);

        // Mid erosion levels (2-4) create the best terracing with valleys
        // Erosion 2: Mid-high terrain (upper terraces)
        // Erosion 3: Mid terrain (main terrace zones)
        // Erosion 4: Low terrain (valley terraces)

        // Primary terrace zone - mid terrain with best stepping
        this.addSurfaceBiome(mapper,
            warmTempRange,
            wetHumidityRange,
            inlandContinentalness,
            erosions[3], // Mid terrain - optimal for terraces
            wideWeirdness,
            0.15F, // High priority
            VastBiomes.MISTY_CASCADE_GORGE);

        // Upper terrace zone - mid-high terrain
        this.addSurfaceBiome(mapper,
            warmTempRange,
            wetHumidityRange,
            inlandContinentalness,
            erosions[2], // Mid-high terrain
            wideWeirdness,
            0.1F,
            VastBiomes.MISTY_CASCADE_GORGE);

        // Lower terrace zone - valley terraces
        this.addSurfaceBiome(mapper,
            warmTempRange,
            wetHumidityRange,
            inlandContinentalness,
            erosions[4], // Low terrain
            wideWeirdness,
            0.1F,
            VastBiomes.MISTY_CASCADE_GORGE);

        // Combined mid-erosion range for larger continuous patches
        Climate.Parameter midErosionRange = Climate.Parameter.span(erosions[2], erosions[4]);
        this.addSurfaceBiome(mapper,
            warmTempRange,
            wetHumidityRange,
            inlandContinentalness,
            midErosionRange,
            wideWeirdness,
            0.05F,
            VastBiomes.MISTY_CASCADE_GORGE);

        // Expanded version for even MORE coverage
        // Wider temperature range but DRY humidity for dry canyon
        Climate.Parameter expandedTempRange = Climate.Parameter.span(0.3F, 1.0F);
        Climate.Parameter expandedHumidityRange = Climate.Parameter.span(0.0F, 0.6F);
        // CRITICAL: Continentalness MUST be >= 0.5 to ensure terrain spawns WELL ABOVE sea level!
        // 0.5+ = elevated inland terrain, prevents aquifer water flooding
        Climate.Parameter expandedContinentalness = Climate.Parameter.span(0.5F, 1.0F);

        this.addSurfaceBiome(mapper,
            expandedTempRange,
            expandedHumidityRange,
            expandedContinentalness,
            midErosionRange,
            wideWeirdness,
            0.0F, // Lower priority, but fills gaps
            VastBiomes.MISTY_CASCADE_GORGE);
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
