package com.vastworlds.worldgen;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class VastBiomes {

    // Biome Resource Keys
    public static final ResourceKey<Biome> MEGA_MOUNTAINS = register("mega_mountains");
    public static final ResourceKey<Biome> ENDLESS_PLAINS = register("endless_plains");
    public static final ResourceKey<Biome> DEEP_CANYONS = register("deep_canyons");
    public static final ResourceKey<Biome> SKY_PLATEAUS = register("sky_plateaus");
    public static final ResourceKey<Biome> ANCIENT_FOREST = register("ancient_forest");
    public static final ResourceKey<Biome> FROZEN_WASTES = register("frozen_wastes");
    public static final ResourceKey<Biome> SCORCHED_BADLANDS = register("scorched_badlands");
    public static final ResourceKey<Biome> MUSHROOM_VALLEYS = register("mushroom_valleys");
    public static final ResourceKey<Biome> CRYSTAL_PEAKS = register("crystal_peaks");
    public static final ResourceKey<Biome> SUNFLOWER_FIELDS = register("sunflower_fields");
    public static final ResourceKey<Biome> DARK_FOREST = register("dark_forest");
    public static final ResourceKey<Biome> ALPINE_MEADOWS = register("alpine_meadows");
    public static final ResourceKey<Biome> VOLCANIC_HIGHLANDS = register("volcanic_highlands");
    public static final ResourceKey<Biome> VAST_OCEAN = register("vast_ocean");
    public static final ResourceKey<Biome> COASTAL_CLIFFS = register("coastal_cliffs");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(VastWorldsMod.MODID, name));
    }

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        // Register all biomes
        context.register(MEGA_MOUNTAINS, megaMountains(features, carvers));
        context.register(ENDLESS_PLAINS, endlessPlains(features, carvers));
        context.register(DEEP_CANYONS, deepCanyons(features, carvers));
        context.register(SKY_PLATEAUS, skyPlateaus(features, carvers));
        context.register(ANCIENT_FOREST, ancientForest(features, carvers));
        context.register(FROZEN_WASTES, frozenWastes(features, carvers));
        context.register(SCORCHED_BADLANDS, scorchedBadlands(features, carvers));
        context.register(MUSHROOM_VALLEYS, mushroomValleys(features, carvers));
        context.register(CRYSTAL_PEAKS, crystalPeaks(features, carvers));
        context.register(SUNFLOWER_FIELDS, sunflowerFields(features, carvers));
        context.register(DARK_FOREST, darkForest(features, carvers));
        context.register(ALPINE_MEADOWS, alpineMeadows(features, carvers));
        context.register(VOLCANIC_HIGHLANDS, volcanicHighlands(features, carvers));
        context.register(VAST_OCEAN, vastOcean(features, carvers));
        context.register(COASTAL_CLIFFS, coastalCliffs(features, carvers));
    }

    // ====================
    // MOUNTAIN BIOMES
    // ====================

    private static Biome megaMountains(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 5, 1, 3));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 95, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addExtraGold(generation);
        BiomeDefaultFeatures.addExtraEmeralds(generation);
        BiomeDefaultFeatures.addInfestedStone(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.0F)
            .downfall(0.5F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x7BA4FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    private static Biome crystalPeaks(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.GOAT, 8, 2, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 80, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addExtraEmeralds(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(-0.5F)
            .downfall(0.3F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3938C9)
                .waterFogColor(0x050533)
                .fogColor(0xDCE9FF)
                .skyColor(0x8AB8FF)
                .ambientParticle(new AmbientParticleSettings(ParticleTypes.SNOWFLAKE, 0.01F))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    private static Biome alpineMeadows(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.SHEEP, 12, 4, 4));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 10, 2, 3));
        BiomeDefaultFeatures.farmAnimals(spawns);
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addMeadowVegetation(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.8F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x0E4ECF)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x77ADFF)
                .grassColorModifier(BiomeSpecialEffects.GrassColorModifier.NONE)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    private static Biome skyPlateaus(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.LLAMA, 5, 4, 6));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addPlainGrass(generation);
        BiomeDefaultFeatures.addDefaultFlowers(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.6F)
            .downfall(0.4F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x78A7FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    // ====================
    // PLAINS BIOMES
    // ====================

    private static Biome endlessPlains(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.plainsSpawns(spawns);
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addPlainGrass(generation);
        BiomeDefaultFeatures.addDefaultFlowers(generation);
        BiomeDefaultFeatures.addPlainVegetation(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.4F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x78A7FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    private static Biome sunflowerFields(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.plainsSpawns(spawns);
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addPlainGrass(generation);
        BiomeDefaultFeatures.addDefaultFlowers(generation);
        BiomeDefaultFeatures.addPlainVegetation(generation);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_SUNFLOWER);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.8F)
            .downfall(0.4F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x78A7FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    // ====================
    // FOREST BIOMES
    // ====================

    private static Biome ancientForest(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawns);
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 5, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addForestFlowers(generation);
        BiomeDefaultFeatures.addFerns(generation);
        BiomeDefaultFeatures.addDefaultFlowers(generation);
        BiomeDefaultFeatures.addForestGrass(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.7F)
            .downfall(0.8F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x79A6FF)
                .grassColorOverride(0x6BA941)
                .foliageColorOverride(0x59AE30)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    private static Biome darkForest(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(spawns);
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 100, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 100, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addForestFlowers(generation);
        BiomeDefaultFeatures.addFerns(generation);
        BiomeDefaultFeatures.addDefaultMushrooms(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.7F)
            .downfall(0.8F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0x60A17B)
                .skyColor(0x60A17B)
                .grassColorOverride(0x507A56)
                .foliageColorOverride(0x507A56)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    // ====================
    // COLD BIOMES
    // ====================

    private static Biome frozenWastes(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.POLAR_BEAR, 1, 1, 2));
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 10, 2, 3));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 80, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addSurfaceFreezing(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(-0.5F)
            .downfall(0.4F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3938C9)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0xC0D8FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    // ====================
    // HOT/DRY BIOMES
    // ====================

    private static Biome deepCanyons(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 80, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addExtraGold(generation);
        BiomeDefaultFeatures.addBadlandVegetation(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0F)
            .downfall(0.0F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xBFB755)
                .skyColor(0xBFB755)
                .foliageColorOverride(0x9E814D)
                .grassColorOverride(0x90814D)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    private static Biome scorchedBadlands(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.HUSK, 100, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addExtraGold(generation);
        BiomeDefaultFeatures.addBadlandGrass(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0F)
            .downfall(0.0F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xABB1A8)
                .skyColor(0x9AB9E3)
                .foliageColorOverride(0x9E814D)
                .grassColorOverride(0x90814D)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    private static Biome volcanicHighlands(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 50, 4, 4));
        spawns.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 2, 2, 5));
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addExtraGold(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(false)
            .temperature(2.0F)
            .downfall(0.0F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0xFA5000)
                .waterFogColor(0x330808)
                .fogColor(0x4A4035)
                .skyColor(0x584232)
                .ambientParticle(new AmbientParticleSettings(ParticleTypes.ASH, 0.118093334F))
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    // ====================
    // SPECIAL BIOMES
    // ====================

    private static Biome mushroomValleys(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        spawns.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.MOOSHROOM, 8, 4, 8));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addMushroomFieldVegetation(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.9F)
            .downfall(1.0F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x78A7FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    // ====================
    // OCEAN/COAST BIOMES
    // ====================

    private static Biome vastOcean(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.oceanSpawns(spawns, 5, 15, 30);
        spawns.addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.SALMON, 15, 1, 5));
        spawns.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(EntityType.DOLPHIN, 2, 1, 2));

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSeagrass(generation);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_COLD);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.5F)
            .downfall(0.5F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x7BA4FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }

    private static Biome coastalCliffs(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder spawns = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.commonSpawns(spawns);

        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addPlainGrass(generation);

        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.6F)
            .downfall(0.6F)
            .specialEffects(new BiomeSpecialEffects.Builder()
                .waterColor(0x3F76E4)
                .waterFogColor(0x050533)
                .fogColor(0xC0D8FF)
                .skyColor(0x7AA3FF)
                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                .build())
            .mobSpawnSettings(spawns.build())
            .generationSettings(generation.build())
            .build();
    }
}
