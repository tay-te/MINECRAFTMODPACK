package com.vastworlds.biome;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;

import java.util.function.Consumer;

/**
 * BiomesOPlenty-style biome builder using climate parameters and terrain slices.
 * This class distributes Vast Worlds biomes using sophisticated climate-based placement.
 */
public class VastWorldsBiomeBuilder {

    // Climate parameter ranges following BiomesOPlenty pattern

    // Temperature ranges (5 levels: Frozen, Cold, Cool, Warm, Hot)
    protected final Climate.Parameter[] temperatures = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.45F),   // Frozen
            Climate.Parameter.span(-0.45F, -0.15F),  // Cold
            Climate.Parameter.span(-0.15F, 0.2F),    // Cool
            Climate.Parameter.span(0.2F, 0.55F),     // Warm
            Climate.Parameter.span(0.55F, 1.0F)      // Hot
    };

    // Humidity ranges (5 levels: Arid, Dry, Neutral, Wet, Very Wet)
    protected final Climate.Parameter[] humidities = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.35F),   // Arid
            Climate.Parameter.span(-0.35F, -0.1F),   // Dry
            Climate.Parameter.span(-0.1F, 0.1F),     // Neutral
            Climate.Parameter.span(0.1F, 0.3F),      // Wet
            Climate.Parameter.span(0.3F, 1.0F)       // Very Wet
    };

    // Erosion ranges (7 levels for terrain shape)
    protected final Climate.Parameter[] erosions = new Climate.Parameter[]{
            Climate.Parameter.span(-1.0F, -0.78F),      // Erosion 0 - Rocky/Jagged peaks
            Climate.Parameter.span(-0.78F, -0.375F),    // Erosion 1 - Hilly
            Climate.Parameter.span(-0.375F, -0.2225F),  // Erosion 2
            Climate.Parameter.span(-0.2225F, 0.05F),    // Erosion 3
            Climate.Parameter.span(0.05F, 0.45F),       // Erosion 4 - Moderate
            Climate.Parameter.span(0.45F, 0.55F),       // Erosion 5
            Climate.Parameter.span(0.55F, 1.0F)         // Erosion 6 - Flat plains
    };

    // Continentalness (distance from ocean)
    protected final Climate.Parameter coastContinentalness = Climate.Parameter.span(-0.19F, -0.11F);
    protected final Climate.Parameter nearInlandContinentalness = Climate.Parameter.span(-0.11F, 0.03F);
    protected final Climate.Parameter midInlandContinentalness = Climate.Parameter.span(0.03F, 0.3F);
    protected final Climate.Parameter farInlandContinentalness = Climate.Parameter.span(0.3F, 1.0F);
    protected final Climate.Parameter inlandContinentalness = Climate.Parameter.span(-0.11F, 0.55F);

    // Weirdness and depth for variation
    protected final Climate.Parameter FULL_RANGE = Climate.Parameter.span(-1.0F, 1.0F);

    /**
     * Adds biomes to the world using terrain slices.
     * Following BiomesOPlenty's sophisticated placement system.
     */
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Add terrain slices similar to BiomesOPlenty
        // Weirdness creates a repeating wave pattern for terrain variation

        // First cycle - full wave
        addPeaks(registry, mapper, Climate.Parameter.span(-0.7666667F, -0.56666666F));
        addHighSlice(registry, mapper, Climate.Parameter.span(-0.93333334F, -0.7666667F));
        addHighSlice(registry, mapper, Climate.Parameter.span(-0.56666666F, -0.4F));
        addMidSlice(registry, mapper, Climate.Parameter.span(-1.0F, -0.93333334F));
        addMidSlice(registry, mapper, Climate.Parameter.span(-0.4F, -0.26666668F));
        addLowSlice(registry, mapper, Climate.Parameter.span(-0.26666668F, -0.05F));
        addValleys(registry, mapper, Climate.Parameter.span(-0.05F, 0.05F));
        addLowSlice(registry, mapper, Climate.Parameter.span(0.05F, 0.26666668F));
        addMidSlice(registry, mapper, Climate.Parameter.span(0.26666668F, 0.4F));

        // Second cycle - truncated
        addHighSlice(registry, mapper, Climate.Parameter.span(0.4F, 0.56666666F));
        addPeaks(registry, mapper, Climate.Parameter.span(0.56666666F, 0.7666667F));
        addHighSlice(registry, mapper, Climate.Parameter.span(0.7666667F, 0.93333334F));
        addMidSlice(registry, mapper, Climate.Parameter.span(0.93333334F, 1.0F));
    }

    /**
     * Add peak terrain - extreme mountains
     */
    protected void addPeaks(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temperature = temperatures[i];

            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter humidity = humidities[j];

                // Use MEGA_MOUNTAINS for peaks in cold/frozen temperatures
                ResourceKey<Biome> peakBiome = (i <= 1) ? VastWorldsBiomes.MEGA_MOUNTAINS :
                        (i == 4 && j <= 1) ? VastWorldsBiomes.DEEP_CANYONS : Biomes.STONY_PEAKS;

                // Peaks appear at lowest erosion (most jagged)
                addSurfaceBiome(mapper, temperature, humidity,
                        Climate.Parameter.span(coastContinentalness, farInlandContinentalness),
                        erosions[0], weirdness, 0.0F, peakBiome);
            }
        }
    }

    /**
     * Add high elevation terrain
     */
    protected void addHighSlice(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temperature = temperatures[i];

            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter humidity = humidities[j];

                // Sky plateaus for wet, cool/warm areas
                ResourceKey<Biome> highBiome = (i >= 2 && i <= 3 && j >= 3) ? VastWorldsBiomes.SKY_PLATEAUS :
                        (i <= 1) ? VastWorldsBiomes.MEGA_MOUNTAINS : Biomes.WINDSWEPT_HILLS;

                addSurfaceBiome(mapper, temperature, humidity,
                        farInlandContinentalness, erosions[0], weirdness, 0.0F, highBiome);
                addSurfaceBiome(mapper, temperature, humidity,
                        farInlandContinentalness, erosions[1], weirdness, 0.0F, highBiome);
            }
        }
    }

    /**
     * Add mid elevation terrain - standard elevation
     */
    protected void addMidSlice(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temperature = temperatures[i];

            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter humidity = humidities[j];

                // Default to vanilla biomes, with custom biomes in specific conditions
                ResourceKey<Biome> midBiome = pickMiddleBiome(i, j);

                addSurfaceBiome(mapper, temperature, humidity,
                        inlandContinentalness, erosions[3], weirdness, 0.0F, midBiome);
                addSurfaceBiome(mapper, temperature, humidity,
                        inlandContinentalness, erosions[4], weirdness, 0.0F, midBiome);
            }
        }
    }

    /**
     * Add low elevation terrain - flattest areas
     */
    protected void addLowSlice(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temperature = temperatures[i];

            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter humidity = humidities[j];

                ResourceKey<Biome> lowBiome = pickLowBiome(i, j);

                // Use erosion 5 and 6 for flattest terrain
                addSurfaceBiome(mapper, temperature, humidity,
                        nearInlandContinentalness, erosions[5], weirdness, 0.0F, lowBiome);
                addSurfaceBiome(mapper, temperature, humidity,
                        nearInlandContinentalness, erosions[6], weirdness, 0.0F, lowBiome);

                // Also add to inland areas for more coverage
                addSurfaceBiome(mapper, temperature, humidity,
                        midInlandContinentalness, erosions[5], weirdness, 0.0F, lowBiome);
                addSurfaceBiome(mapper, temperature, humidity,
                        midInlandContinentalness, erosions[6], weirdness, 0.0F, lowBiome);
            }
        }
    }

    /**
     * Add valleys - rivers and lowlands
     */
    protected void addValleys(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper, Climate.Parameter weirdness) {
        for (int i = 0; i < temperatures.length; i++) {
            Climate.Parameter temperature = temperatures[i];

            for (int j = 0; j < humidities.length; j++) {
                Climate.Parameter humidity = humidities[j];

                // Rivers or swamps depending on temperature
                ResourceKey<Biome> valleyBiome = (i == 0) ? Biomes.FROZEN_RIVER :
                        (i >= 3 && j >= 3) ? Biomes.SWAMP : Biomes.RIVER;

                addSurfaceBiome(mapper, temperature, humidity,
                        Climate.Parameter.span(coastContinentalness, farInlandContinentalness),
                        Climate.Parameter.span(erosions[2], erosions[5]),
                        weirdness, 0.0F, valleyBiome);
            }
        }
    }

    /**
     * Pick a middle biome based on temperature and humidity
     */
    protected ResourceKey<Biome> pickMiddleBiome(int temperatureIndex, int humidityIndex) {
        // Deep Canyons - hot + arid/dry
        if (temperatureIndex == 4 && humidityIndex <= 1) {
            return VastWorldsBiomes.DEEP_CANYONS;
        }
        // Sky Plateaus - cool/warm + very wet
        else if (temperatureIndex >= 2 && temperatureIndex <= 3 && humidityIndex == 4) {
            return VastWorldsBiomes.SKY_PLATEAUS;
        }
        // Default vanilla biomes
        else if (temperatureIndex == 0) {
            return Biomes.SNOWY_PLAINS;
        } else if (temperatureIndex == 1) {
            return Biomes.TAIGA;
        } else if (temperatureIndex == 2) {
            return Biomes.PLAINS;
        } else if (temperatureIndex == 3) {
            return Biomes.SAVANNA;
        } else {
            return Biomes.DESERT;
        }
    }

    /**
     * Pick a low/flat biome based on temperature and humidity
     */
    protected ResourceKey<Biome> pickLowBiome(int temperatureIndex, int humidityIndex) {
        // Endless Plains - warm/hot + neutral/wet (flattest areas)
        if (temperatureIndex >= 3 && humidityIndex >= 2) {
            return VastWorldsBiomes.ENDLESS_PLAINS;
        }
        // Default to vanilla flat biomes
        else if (temperatureIndex == 0) {
            return Biomes.SNOWY_PLAINS;
        } else if (temperatureIndex == 1) {
            return Biomes.PLAINS;
        } else if (temperatureIndex == 2) {
            return Biomes.PLAINS;
        } else if (temperatureIndex == 3) {
            return Biomes.SAVANNA;
        } else {
            return Biomes.DESERT;
        }
    }

    /**
     * Helper method to add a surface biome
     */
    protected void addSurfaceBiome(Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper,
                                    Climate.Parameter temperature, Climate.Parameter humidity,
                                    Climate.Parameter continentalness, Climate.Parameter erosion,
                                    Climate.Parameter weirdness, float offset, ResourceKey<Biome> biome) {
        // Add for both surface depth points (0.0 and 1.0)
        mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion,
                Climate.Parameter.point(0.0F), weirdness, offset), biome));
        mapper.accept(Pair.of(Climate.parameters(temperature, humidity, continentalness, erosion,
                Climate.Parameter.point(1.0F), weirdness, offset), biome));
    }
}
