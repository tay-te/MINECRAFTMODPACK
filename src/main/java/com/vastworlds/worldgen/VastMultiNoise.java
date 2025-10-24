package com.vastworlds.worldgen;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSource;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;

import java.util.List;

public class VastMultiNoise {

    public static final ResourceKey<MultiNoiseBiomeSourceParameterList> VAST_OVERWORLD = register("vast_overworld");

    private static ResourceKey<MultiNoiseBiomeSourceParameterList> register(String name) {
        return ResourceKey.create(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST,
            ResourceLocation.fromNamespaceAndPath(VastWorldsMod.MODID, name));
    }

    public static void bootstrap(BootstrapContext<MultiNoiseBiomeSourceParameterList> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);

        context.register(VAST_OVERWORLD, new MultiNoiseBiomeSourceParameterList(
            MultiNoiseBiomeSource.Preset.OVERWORLD,
            createBiomeParameters(biomes)
        ));
    }

    private static Climate.ParameterList<net.minecraft.core.Holder<Biome>> createBiomeParameters(HolderGetter<Biome> biomes) {
        return new Climate.ParameterList<>(List.of(
            // OCEAN BIOMES (low continentalness)
            pair(biomes, VastBiomes.VAST_OCEAN,
                Climate.parameters(
                    Climate.Parameter.span(0.0F, 1.0F),      // temperature: any
                    Climate.Parameter.span(0.0F, 1.0F),      // humidity: any
                    Climate.Parameter.span(-1.0F, -0.3F),    // continentalness: ocean
                    Climate.Parameter.span(0.0F, 1.0F),      // erosion: any
                    Climate.Parameter.span(0.0F, 1.0F),      // weirdness: any
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // COASTAL/BEACH BIOMES (transition)
            pair(biomes, VastBiomes.COASTAL_CLIFFS,
                Climate.parameters(
                    Climate.Parameter.span(0.0F, 1.0F),      // temperature: any
                    Climate.Parameter.span(0.0F, 1.0F),      // humidity: any
                    Climate.Parameter.span(-0.3F, 0.0F),     // continentalness: coast
                    Climate.Parameter.span(0.0F, 1.0F),      // erosion: any
                    Climate.Parameter.span(0.0F, 1.0F),      // weirdness: any
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // MOUNTAIN BIOMES (low erosion, high continentalness)
            // Mega Mountains - cold, high altitude
            pair(biomes, VastBiomes.MEGA_MOUNTAINS,
                Climate.parameters(
                    Climate.Parameter.span(-1.0F, -0.3F),    // temperature: cold
                    Climate.Parameter.span(0.0F, 0.6F),      // humidity: any to moderate
                    Climate.Parameter.span(0.1F, 1.0F),      // continentalness: inland
                    Climate.Parameter.span(-1.0F, -0.3F),    // erosion: low (peaks)
                    Climate.Parameter.span(-1.0F, 1.0F),     // weirdness: any
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // Crystal Peaks - very cold, extreme mountains
            pair(biomes, VastBiomes.CRYSTAL_PEAKS,
                Climate.parameters(
                    Climate.Parameter.span(-1.0F, -0.6F),    // temperature: very cold
                    Climate.Parameter.span(0.0F, 0.4F),      // humidity: dry to moderate
                    Climate.Parameter.span(0.3F, 1.0F),      // continentalness: far inland
                    Climate.Parameter.span(-1.0F, -0.5F),    // erosion: very low (highest peaks)
                    Climate.Parameter.span(0.0F, 1.0F),      // weirdness: positive (peaks)
                    Climate.Parameter.point(0.0F),           // depth
                    0.1F                                      // offset (priority)
                )),

            // Alpine Meadows - moderate cold, high altitude
            pair(biomes, VastBiomes.ALPINE_MEADOWS,
                Climate.parameters(
                    Climate.Parameter.span(-0.3F, 0.3F),     // temperature: cool to moderate
                    Climate.Parameter.span(0.4F, 1.0F),      // humidity: moderate to wet
                    Climate.Parameter.span(0.0F, 0.6F),      // continentalness: inland
                    Climate.Parameter.span(-0.5F, -0.1F),    // erosion: low to moderate
                    Climate.Parameter.span(-1.0F, 1.0F),     // weirdness: any
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // Sky Plateaus - high flat areas
            pair(biomes, VastBiomes.SKY_PLATEAUS,
                Climate.parameters(
                    Climate.Parameter.span(0.0F, 0.6F),      // temperature: moderate
                    Climate.Parameter.span(0.0F, 0.6F),      // humidity: any to moderate
                    Climate.Parameter.span(0.2F, 1.0F),      // continentalness: inland
                    Climate.Parameter.span(-0.3F, 0.1F),     // erosion: low to moderate
                    Climate.Parameter.span(-0.3F, 0.3F),     // weirdness: centered (plateaus)
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // PLAINS BIOMES (moderate erosion, varied continentalness)
            // Endless Plains - warm, flat
            pair(biomes, VastBiomes.ENDLESS_PLAINS,
                Climate.parameters(
                    Climate.Parameter.span(0.3F, 1.0F),      // temperature: warm
                    Climate.Parameter.span(0.0F, 0.6F),      // humidity: dry to moderate
                    Climate.Parameter.span(0.0F, 0.5F),      // continentalness: near to mid inland
                    Climate.Parameter.span(0.2F, 0.8F),      // erosion: moderate to high (flatter)
                    Climate.Parameter.span(-1.0F, 1.0F),     // weirdness: any
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // Sunflower Fields - warm, flat, rarer
            pair(biomes, VastBiomes.SUNFLOWER_FIELDS,
                Climate.parameters(
                    Climate.Parameter.span(0.5F, 1.0F),      // temperature: warm to hot
                    Climate.Parameter.span(0.3F, 0.7F),      // humidity: moderate
                    Climate.Parameter.span(0.0F, 0.4F),      // continentalness: near inland
                    Climate.Parameter.span(0.4F, 0.9F),      // erosion: moderate to high
                    Climate.Parameter.span(0.0F, 1.0F),      // weirdness: positive
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // FOREST BIOMES (moderate conditions)
            // Ancient Forest - temperate, moderate
            pair(biomes, VastBiomes.ANCIENT_FOREST,
                Climate.parameters(
                    Climate.Parameter.span(0.2F, 0.8F),      // temperature: moderate
                    Climate.Parameter.span(0.5F, 1.0F),      // humidity: wet
                    Climate.Parameter.span(0.0F, 0.7F),      // continentalness: inland
                    Climate.Parameter.span(0.0F, 0.6F),      // erosion: low to moderate
                    Climate.Parameter.span(-1.0F, 0.0F),     // weirdness: negative to neutral
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // Dark Forest - temperate, dense
            pair(biomes, VastBiomes.DARK_FOREST,
                Climate.parameters(
                    Climate.Parameter.span(0.3F, 0.7F),      // temperature: moderate
                    Climate.Parameter.span(0.6F, 1.0F),      // humidity: very wet
                    Climate.Parameter.span(0.2F, 0.8F),      // continentalness: inland
                    Climate.Parameter.span(-0.2F, 0.4F),     // erosion: varied
                    Climate.Parameter.span(-1.0F, -0.3F),    // weirdness: negative (valleys)
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // COLD BIOMES
            // Frozen Wastes - very cold, flat
            pair(biomes, VastBiomes.FROZEN_WASTES,
                Climate.parameters(
                    Climate.Parameter.span(-1.0F, -0.4F),    // temperature: very cold
                    Climate.Parameter.span(0.0F, 0.5F),      // humidity: dry to moderate
                    Climate.Parameter.span(0.0F, 0.6F),      // continentalness: inland
                    Climate.Parameter.span(0.3F, 1.0F),      // erosion: moderate to high (flatter)
                    Climate.Parameter.span(-1.0F, 1.0F),     // weirdness: any
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // HOT/DRY BIOMES
            // Deep Canyons - hot, carved terrain
            pair(biomes, VastBiomes.DEEP_CANYONS,
                Climate.parameters(
                    Climate.Parameter.span(0.8F, 1.0F),      // temperature: hot
                    Climate.Parameter.span(-1.0F, 0.2F),     // humidity: very dry
                    Climate.Parameter.span(0.1F, 0.7F),      // continentalness: inland
                    Climate.Parameter.span(0.5F, 1.0F),      // erosion: high (deep valleys)
                    Climate.Parameter.span(-1.0F, -0.2F),    // weirdness: negative (canyons)
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // Scorched Badlands - hot, dry
            pair(biomes, VastBiomes.SCORCHED_BADLANDS,
                Climate.parameters(
                    Climate.Parameter.span(0.7F, 1.0F),      // temperature: hot
                    Climate.Parameter.span(-1.0F, 0.1F),     // humidity: very dry
                    Climate.Parameter.span(0.2F, 0.8F),      // continentalness: inland
                    Climate.Parameter.span(0.1F, 0.7F),      // erosion: varied
                    Climate.Parameter.span(0.0F, 1.0F),      // weirdness: positive
                    Climate.Parameter.point(0.0F),           // depth
                    0.0F                                      // offset
                )),

            // Volcanic Highlands - very hot, extreme
            pair(biomes, VastBiomes.VOLCANIC_HIGHLANDS,
                Climate.parameters(
                    Climate.Parameter.span(0.9F, 1.0F),      // temperature: very hot
                    Climate.Parameter.span(-1.0F, 0.0F),     // humidity: dry
                    Climate.Parameter.span(0.3F, 1.0F),      // continentalness: far inland
                    Climate.Parameter.span(-0.5F, 0.2F),     // erosion: low to moderate
                    Climate.Parameter.span(0.3F, 1.0F),      // weirdness: positive (peaks)
                    Climate.Parameter.point(0.0F),           // depth
                    0.1F                                      // offset (priority)
                )),

            // SPECIAL BIOMES
            // Mushroom Valleys - rare, moderate conditions
            pair(biomes, VastBiomes.MUSHROOM_VALLEYS,
                Climate.parameters(
                    Climate.Parameter.span(0.4F, 0.8F),      // temperature: moderate
                    Climate.Parameter.span(0.7F, 1.0F),      // humidity: very wet
                    Climate.Parameter.span(0.2F, 0.6F),      // continentalness: inland
                    Climate.Parameter.span(0.3F, 0.8F),      // erosion: moderate to high
                    Climate.Parameter.span(-0.5F, 0.0F),     // weirdness: negative (valleys)
                    Climate.Parameter.point(0.0F),           // depth
                    0.05F                                     // offset (slightly higher priority)
                ))
        ));
    }

    /**
     * Helper method to create a climate parameter pair
     */
    private static Climate.ParameterPoint pair(HolderGetter<Biome> biomes,
                                               ResourceKey<Biome> biomeKey,
                                               Climate.ParameterPoint parameters) {
        return parameters;
    }

    /**
     * Wrapper to create the biome entry
     */
    private static com.mojang.datafixers.util.Pair<Climate.ParameterPoint, net.minecraft.core.Holder<Biome>>
    pair(HolderGetter<Biome> biomes, ResourceKey<Biome> biomeKey, Climate.ParameterPoint parameters) {
        return com.mojang.datafixers.util.Pair.of(parameters, biomes.getOrThrow(biomeKey));
    }
}
