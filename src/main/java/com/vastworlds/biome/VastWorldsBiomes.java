package com.vastworlds.biome;

import com.vastworlds.worldgen.placement.VastWorldsVegetationPlacements;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
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
     * Global generation for all VastWorlds biomes
     * Adds common features that don't cause cycles
     * Based on BiomesOPlenty's pattern
     */
    private static void globalOverworldGeneration(BiomeGenerationSettings.Builder builder) {
        // Add vanilla carvers manually (CRITICAL: must be added first!)
        builder.addCarver(Carvers.CAVE);
        builder.addCarver(Carvers.CAVE_EXTRA_UNDERGROUND);
        builder.addCarver(Carvers.CANYON);

        // Add selective default features (not all BiomeDefaultFeatures to avoid cycles)
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addSurfaceFreezing(builder);
    }

    /**
     * Creates a MEGA PLAINS biome - vast, lively, rolling grasslands full of character
     *
     * ENHANCEMENTS:
     * - Rolling terrain with gentle hills and valleys (using custom density functions)
     * - 5 diverse grass types (short, tall, wheat-like, fern, mixed)
     * - 3 wildflower types (mixed wildflowers, sunflowers, poppies)
     * - Terrain micro-features (boulders, fallen logs, gravel/dirt patches)
     * - Rare features (lone oak trees, shrubs)
     * - Custom grass and foliage colors for vibrant appearance
     *
     * Total: 14+ unique vegetation/terrain features for maximum liveliness!
     */
    public static Biome megaPlains(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        // Step 1: Add common features (carvers + basic underground features)
        globalOverworldGeneration(generation);

        // Step 2: Add ores and surface disks
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        // Step 3: Add TERRAIN MICRO-FEATURES FIRST (so vegetation appears on top)
        addFeature(generation, GenerationStep.Decoration.LOCAL_MODIFICATIONS,
            VastWorldsVegetationPlacements.SCATTERED_BOULDERS_PLAINS); // Landmark boulders
        addFeature(generation, GenerationStep.Decoration.LOCAL_MODIFICATIONS,
            VastWorldsVegetationPlacements.FALLEN_OAK_LOG_PLAINS); // Natural decay

        // Surface disks (gravel and coarse dirt patches)
        addFeature(generation, GenerationStep.Decoration.UNDERGROUND_ORES,
            VastWorldsVegetationPlacements.GRAVEL_PATCHES_PLAINS); // Dry riverbeds
        addFeature(generation, GenerationStep.Decoration.UNDERGROUND_ORES,
            VastWorldsVegetationPlacements.COARSE_DIRT_PATCHES_PLAINS); // Weathered areas

        // Step 4: Add DIVERSE GRASS LAYERS (5 types for rich, varied coverage)
        // These create the lush, windswept plains aesthetic
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.PATCH_PLAINS_GRASS_SHORT); // Base layer (20/chunk)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.PATCH_PLAINS_GRASS_TALL); // Windswept drama (15/chunk)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.PATCH_PLAINS_GRASS_MIXED); // Realistic variety (12/chunk)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.PATCH_PLAINS_GRASS_WHEAT_LIKE); // Golden texture (8/chunk)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.PATCH_PLAINS_GRASS_FERN); // Low area variety (6/chunk)

        // Step 5: Add VIBRANT WILDFLOWERS (3 types for colorful splashes)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.PATCH_PLAINS_WILDFLOWERS); // Mixed flowers (10/chunk)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.PATCH_PLAINS_POPPIES); // Red splashes (5/chunk)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.PATCH_PLAINS_SUNFLOWERS); // Iconic plains (uncommon)

        // Step 6: Add RARE FEATURES (special exploration touches)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.LONE_OAK_TREE); // Rare landmarks (1% of chunks)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION,
            VastWorldsVegetationPlacements.SMALL_SHRUB_PLAINS); // Windswept character

        // Mob spawns - peaceful plains animals
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.plainsSpawns(spawns); // Includes sheep, pigs, chickens, cows
        spawns.addSpawn(MobCategory.CREATURE, 12, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 3, 5)); // More sheep
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.COW, 2, 4)); // More cows
        spawns.addSpawn(MobCategory.CREATURE, 8, new MobSpawnSettings.SpawnerData(EntityType.HORSE, 2, 6)); // Wild horses!
        spawns.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 2, 3)); // Rabbits

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.75F) // Temperate, pleasant climate
            .downfall(0.5F) // Moderate rainfall for lush grass
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .waterColor(0x3F76E4) // Clear blue water for small ponds
                    .waterFogColor(0x050533)
                    .fogColor(0xC0D8FF) // Crisp, clear sky feeling
                    .skyColor(calculateSkyColor(0.75F)) // Bright, open sky
                    .grassColorOverride(0x88D957) // Vibrant, lively grass green
                    .foliageColorOverride(0x6DBF47) // Rich foliage green
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
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

        // Add common features (carvers + basic underground features)
        globalOverworldGeneration(generation);

        // Add ores and surface disks
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        // Add custom vegetation (NO default vegetation to avoid cycles!)
        // TODO: Implement custom mountain vegetation features
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_PLAINS_GRASS_SHORT);

        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.CREATURE, 5, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 1, 3));
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

        // Add common features (carvers + basic underground features)
        globalOverworldGeneration(generation);

        // Add ores and surface disks
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        // Add custom vegetation (NO default vegetation to avoid cycles!)
        // TODO: Implement custom valley vegetation features (trees, mushrooms, grass)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_PLAINS_GRASS_SHORT);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_PLAINS_GRASS_TALL);

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
     * Creates a Misty Cascade Gorge biome - COMPLETELY OVERHAULED!
     *
     * DESIGN GOALS:
     * - Lush, lively jungle gorge atmosphere (Victoria Falls style)
     * - Dense vegetation with multiple layers
     * - Vibrant colors from tropical flowers
     * - Water features everywhere (pools, streams, waterfalls)
     * - Atmospheric details (vines, moss, mist)
     *
     * FEATURES (35+ custom features for maximum detail):
     * - 4 tree types (jungle, mega jungle, azalea, oak)
     * - 5 ground cover types (grass, tall grass, ferns, large ferns, mixed)
     * - 3 flower types (tropical mix, orchids, dandelions)
     * - 4 water features (lily pads, watergrass, sugar cane, seagrass)
     * - 5 atmospheric details (vines, hanging vines, moss, dripleafs)
     * - 4 mushroom types (brown, red, huge brown, huge red)
     * - 4 special features (bamboo, melons, pumpkins, moss splatters)
     */
    public static Biome mistyCascadeGorge(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(placedFeatures, carvers);

        // Step 1: Add ALL carvers (TIER 1 CUSTOM CARVERS + vanilla for variety)
        // Custom carvers for unique terrain features:
        // - LAKE_BASIN: Wide, shallow lake basins for controlled water features (3.0-5.0x radius)
        // - MASSIVE_RAVINE: HUGE deep ravines (4.0-8.0x width, Y=20 to Y=160, very rare)
        // - GORGE_FLOODED_CAVE: Water-filled caves at mid-high elevations (wider, taller, flooded)
        // - GORGE_CANYON: Wide open-air gorges cutting through terraces
        generation.addCarver(carvers.getOrThrow(com.vastworlds.datagen.VastWorldsCarverProvider.LAKE_BASIN));
        generation.addCarver(carvers.getOrThrow(com.vastworlds.datagen.VastWorldsCarverProvider.MASSIVE_RAVINE));
        generation.addCarver(carvers.getOrThrow(com.vastworlds.datagen.VastWorldsCarverProvider.GORGE_FLOODED_CAVE));
        generation.addCarver(carvers.getOrThrow(com.vastworlds.datagen.VastWorldsCarverProvider.GORGE_CANYON));

        // Vanilla carvers for additional variety
        generation.addCarver(Carvers.CAVE);
        generation.addCarver(Carvers.CAVE_EXTRA_UNDERGROUND);

        // Step 2: Add basic underground features
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(generation);
        // CRITICAL: DO NOT add default springs - they spawn water everywhere on surface!
        // BiomeDefaultFeatures.addDefaultSprings(generation); // DISABLED - causes water flooding
        BiomeDefaultFeatures.addSurfaceFreezing(generation);

        // Step 3: Add ores and disks (always safe)
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);

        // Step 4: === TREES & VEGETATION STRUCTURES === (7 types for variety - INCLUDING TIER 1 CUSTOM TREES!)
        // Standard trees
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.JUNGLE_TREE_GORGE);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.JUNGLE_TREE_LARGE_GORGE);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.AZALEA_TREE_GORGE);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.OAK_TREE_SMALL_GORGE);

        // TIER 1 CUSTOM TREES - Unique to Misty Cascade Gorge!
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.CASCADE_WILLOW_TREE_GORGE);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.MIST_PALM_TREE_GORGE);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.GIANT_TROPICAL_TREE_GORGE);

        // Step 5: === GROUND COVER - GRASS & FERNS === (5 types for lush layers)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_GRASS);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_TALL_GRASS);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_FERN);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_LARGE_FERN);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_MIXED_VEGETATION);

        // Step 6: === FLOWERS & DECORATIVE PLANTS === (3 types for vibrant colors)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_FLOWERS);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_ORCHIDS);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_DANDELIONS);

        // Step 7: === WATER FEATURES === (minimal for dry canyon)
        // Removed lily pads, watergrass, and seagrass for dry conditions
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_SUGAR_CANE);

        // Step 8: === ATMOSPHERIC DETAILS === (5 types for jungle atmosphere)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_VINES);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_HANGING_VINES);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_MOSS_CARPET);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_DRIPLEAF_SMALL);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_DRIPLEAF_BIG);

        // Step 9: === MUSHROOMS === (4 types for shaded areas)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_BROWN_MUSHROOM);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_RED_MUSHROOM);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.HUGE_BROWN_MUSHROOM_GORGE);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.HUGE_RED_MUSHROOM_GORGE);

        // Step 10: === SPECIAL FEATURES === (4 types for unique character)
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_BAMBOO);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_MELON);
        addFeature(generation, GenerationStep.Decoration.VEGETAL_DECORATION, VastWorldsVegetationPlacements.PATCH_GORGE_PUMPKIN);
        addFeature(generation, GenerationStep.Decoration.LOCAL_MODIFICATIONS, VastWorldsVegetationPlacements.MOSS_SPLATTER_GORGE);

        // Mob spawns - tropical rainforest animals
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawns);
        // Lots of parrots for jungle atmosphere
        spawns.addSpawn(MobCategory.CREATURE, 40, new MobSpawnSettings.SpawnerData(EntityType.PARROT, 10, 20));
        // Pandas in bamboo areas
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.PANDA, 1, 2));
        // Ocelots in dense jungle
        spawns.addSpawn(MobCategory.MONSTER, 5, new MobSpawnSettings.SpawnerData(EntityType.OCELOT, 1, 3));
        // Chickens (jungle birds)
        spawns.addSpawn(MobCategory.CREATURE, 10, new MobSpawnSettings.SpawnerData(EntityType.CHICKEN, 4, 4));

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.8F) // Hot dry climate - arid canyon
            .downfall(0.15F) // Low rainfall - dry canyon conditions
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .waterColor(0x3D57D6) // Deep blue water for massive waterfalls
                    .waterFogColor(0x050533) // Dark blue underwater (deep plunge pools)
                    .fogColor(0xC0E8FF) // Misty/foggy atmosphere from waterfall spray
                    .skyColor(calculateSkyColor(0.95F))
                    .grassColorOverride(0x55C93F) // Vibrant tropical green (lush vegetation)
                    .foliageColorOverride(0x30BB0B) // Bright jungle green foliage
                    .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
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

    /**
     * Helper method to safely add a feature to a biome.
     * Following BiomesOPlenty's pattern for feature addition.
     *
     * This method ensures features are added in a way that avoids ordering cycles.
     */
    protected static void addFeature(
        BiomeGenerationSettings.Builder builder,
        GenerationStep.Decoration step,
        ResourceKey<PlacedFeature> feature
    ) {
        builder.addFeature(step, feature);
    }
}
