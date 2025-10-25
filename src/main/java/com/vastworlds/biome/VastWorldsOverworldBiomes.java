package com.vastworlds.biome;

import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import javax.annotation.Nullable;

/**
 * Factory class for creating Vast Worlds biomes programmatically.
 * Based on BiomesOPlenty's pattern for biome creation.
 */
public class VastWorldsOverworldBiomes {

    @Nullable
    private static final Music NORMAL_MUSIC = null;
    private static final Music MOUNTAIN_MUSIC = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_JAGGED_PEAKS);
    private static final Music MEADOW_MUSIC = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_MEADOW);

    /**
     * Calculate sky color based on temperature
     */
    protected static int calculateSkyColor(float temperature) {
        float temp = temperature / 3.0F;
        temp = Mth.clamp(temp, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F);
    }

    /**
     * Base biome factory method
     */
    private static Biome biome(boolean hasPrecipitation, float temperature, float downfall,
                                MobSpawnSettings.Builder spawnBuilder,
                                BiomeGenerationSettings.Builder biomeBuilder,
                                @Nullable Music music) {
        return biome(hasPrecipitation, temperature, downfall, 4159204, 329011,
                spawnBuilder, biomeBuilder, music);
    }

    /**
     * Biome factory with custom water colors
     */
    private static Biome biome(boolean hasPrecipitation, float temperature, float downfall,
                                int waterColor, int waterFogColor,
                                MobSpawnSettings.Builder spawnBuilder,
                                BiomeGenerationSettings.Builder biomeBuilder,
                                @Nullable Music music) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(hasPrecipitation)
                .temperature(temperature)
                .downfall(downfall)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(waterColor)
                        .waterFogColor(waterFogColor)
                        .fogColor(12638463)
                        .skyColor(calculateSkyColor(temperature))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(music)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }

    /**
     * Biome factory with color overrides
     */
    private static Biome biomeWithColorOverrides(boolean hasPrecipitation, float temperature, float downfall,
                                                   int grassColor, int foliageColor,
                                                   MobSpawnSettings.Builder spawnBuilder,
                                                   BiomeGenerationSettings.Builder biomeBuilder,
                                                   @Nullable Music music) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(hasPrecipitation)
                .temperature(temperature)
                .downfall(downfall)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .waterColor(4159204)
                        .waterFogColor(329011)
                        .fogColor(12638463)
                        .grassColorOverride(grassColor)
                        .foliageColorOverride(foliageColor)
                        .skyColor(calculateSkyColor(temperature))
                        .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                        .backgroundMusic(music)
                        .build())
                .mobSpawnSettings(spawnBuilder.build())
                .generationSettings(biomeBuilder.build())
                .build();
    }

    /**
     * Global generation features for all Vast Worlds biomes
     */
    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    /**
     * MEGA MOUNTAINS - Extreme peaks with snow and ice
     * Temperature: 0.2F (cold), Downfall: 0.3F (some snow)
     */
    public static Biome megaMountains(HolderGetter<PlacedFeature> placedFeatureGetter,
                                       HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
        // Mob spawns - mountain creatures
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 10, 4, 6));
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 5, 2, 3));

        // Biome features - minimal vegetation at extreme heights
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addExtraEmeralds(biomeBuilder);
        BiomeDefaultFeatures.addInfestedStone(biomeBuilder);

        return biome(true, 0.2F, 0.3F, spawnBuilder, biomeBuilder, MOUNTAIN_MUSIC);
    }

    /**
     * ENDLESS PLAINS - Vast flat grasslands
     * Temperature: 0.8F (warm), Downfall: 0.4F (moderate rain)
     */
    public static Biome endlessPlains(HolderGetter<PlacedFeature> placedFeatureGetter,
                                       HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
        // Mob spawns - typical plains creatures
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.plainsSpawns(spawnBuilder);
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 10, 2, 6));
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DONKEY, 3, 1, 3));

        // Biome features - abundant grass and flowers
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeBuilder);
        BiomeDefaultFeatures.addDefaultFlowers(biomeBuilder);

        // Custom colors for endless plains - vibrant green
        return biomeWithColorOverrides(true, 0.8F, 0.4F, 0x91BD59, 0x77AB2F,
                spawnBuilder, biomeBuilder, MEADOW_MUSIC);
    }

    /**
     * DEEP CANYONS - Massive ravines and valleys with dramatic cliffs
     * Temperature: 2.0F (hot), Downfall: 0.0F (no rain - desert-like)
     */
    public static Biome deepCanyons(HolderGetter<PlacedFeature> placedFeatureGetter,
                                     HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
        // Mob spawns - desert/mesa creatures
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        // No passive mobs - harsh environment

        // Biome features - sparse vegetation, desert-like
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addExtraGold(biomeBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeBuilder);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2);
        biomeBuilder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DESERT);

        // Desert-like colors - red/orange tones
        return biomeWithColorOverrides(false, 2.0F, 0.0F, 0xBFB755, 0x9E814D,
                spawnBuilder, biomeBuilder, NORMAL_MUSIC);
    }

    /**
     * SKY PLATEAUS - High plateaus with steep drops and unique vegetation
     * Temperature: 0.5F (cool), Downfall: 0.8F (wet - lush)
     */
    public static Biome skyPlateaus(HolderGetter<PlacedFeature> placedFeatureGetter,
                                     HolderGetter<ConfiguredWorldCarver<?>> carverGetter) {
        // Mob spawns - typical creatures plus some unique spawns
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawnBuilder);
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.PIG, 10, 4, 4));
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 10, 4, 4));
        spawnBuilder.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.COW, 8, 4, 4));

        // Biome features - lush meadow-like vegetation
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        globalOverworldGeneration(biomeBuilder);
        BiomeDefaultFeatures.addDefaultOres(biomeBuilder);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeBuilder);
        BiomeDefaultFeatures.addDefaultFlowers(biomeBuilder);

        // Lush green colors
        return biomeWithColorOverrides(true, 0.5F, 0.8F, 0x83BB6D, 0x63A948,
                spawnBuilder, biomeBuilder, MEADOW_MUSIC);
    }
}
