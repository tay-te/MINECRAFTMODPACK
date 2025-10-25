package com.vastworlds.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.util.Mth;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * Biome definitions for Vast Worlds
 * This class creates the actual Biome objects with their properties
 */
public class VastWorldsBiomes {

    /**
     * Creates a Mega Plains biome - vast, flat grasslands
     */
    public static Biome megaPlains(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        // Add basic features similar to plains
        // BiomeDefaultFeatures.addDefaultOverworldLandStructures(generation); // Method name changed in Mojang mappings
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        // Abundant grass for mega plains feel
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_PLAIN);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_TALL_GRASS_2);

        BiomeDefaultFeatures.addPlainGrass(generation);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);
        BiomeDefaultFeatures.addDefaultExtraVegetation(generation);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.plainsSpawns(spawns);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.4F)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(12638463)
                    .skyColor(calculateSkyColor(0.8F))
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    // .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_PLAINS)) // Sound event name changed
                    .build()
            )
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    /**
     * Creates a Towering Hills biome - extreme elevation changes
     */
    public static Biome toweringHills(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        // BiomeDefaultFeatures.addDefaultOverworldLandStructures(generation); // Method name changed in Mojang mappings
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        // Sparse vegetation for high altitudes
        BiomeDefaultFeatures.addDefaultGrass(generation);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        // Goats for mountain vibes
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 5, 1, 3));
        BiomeDefaultFeatures.commonSpawns(spawns);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.2F)
            .downfall(0.3F)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(12638463)
                    .skyColor(calculateSkyColor(0.2F))
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    // .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW))
                    .build()
            )
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    /**
     * Creates a Deep Valley biome - low-lying areas between hills
     */
    public static Biome deepValley(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        // BiomeDefaultFeatures.addDefaultOverworldLandStructures(generation); // Method name changed in Mojang mappings
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        // Dense vegetation in valleys
        BiomeDefaultFeatures.addForestFlowers(generation);
        BiomeDefaultFeatures.addDefaultGrass(generation);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);
        BiomeDefaultFeatures.addDefaultExtraVegetation(generation);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawns);
        BiomeDefaultFeatures.commonSpawns(spawns);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.7F)
            .downfall(0.8F)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .waterColor(4159204)
                    .waterFogColor(329011)
                    .fogColor(12638463)
                    .skyColor(calculateSkyColor(0.7F))
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                    // .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
                    .build()
            )
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    /**
     * Calculate sky color based on temperature (Vanilla algorithm)
     */
    private static int calculateSkyColor(float temperature) {
        float f = temperature / 3.0F;
        f = Math.min(Math.max(f, -1.0F), 1.0F);
        return Mth.hsvToRgb(0.62222224F - f * 0.05F, 0.5F + f * 0.1F, 1.0F);
    }
}
