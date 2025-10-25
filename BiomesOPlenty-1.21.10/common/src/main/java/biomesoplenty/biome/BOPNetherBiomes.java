/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.biome;

import biomesoplenty.api.sound.BOPSounds;
import biomesoplenty.worldgen.placement.BOPNetherPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.NetherPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BOPNetherBiomes
{
    //public static final Holder.Reference<SoundEvent> AMETHYST_BLOCK_CHIME = BuiltInRegistries.SOUND_EVENT.createIntrusiveHolder(SoundEvents.AMETHYST_BLOCK_CHIME);

    private static void addFeature(BiomeGenerationSettings.Builder builder, GenerationStep.Decoration step, ResourceKey<PlacedFeature> feature)
    {
        builder.addFeature(step, feature);
    }

    public static Biome crystallineChasm(HolderGetter<PlacedFeature> placedFeatureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter)
    {
        // Mob spawns
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.CREATURE, 60, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2));

        // Biome features
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        biomeBuilder.addCarver(Carvers.NETHER_CAVE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NETHER);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.RED_MUSHROOM_NETHER);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED);
        BiomeDefaultFeatures.addNetherDefaultOres(biomeBuilder);
        addFeature(biomeBuilder, GenerationStep.Decoration.LOCAL_MODIFICATIONS, BOPNetherPlacements.LARGE_ROSE_QUARTZ);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.SMALL_CRYSTAL);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false).temperature(2.0F).downfall(0.0F)
                .specialEffects((new BiomeSpecialEffects.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(0x59002C).skyColor(BOPOverworldBiomes.calculateSkyColor(2.0F)).ambientParticle(new AmbientParticleSettings(ParticleTypes.ELECTRIC_SPARK, 0.0008925F)).ambientLoopSound(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0D)).backgroundMusic(Musics.createGameMusic(BOPSounds.MUSIC_BIOME_CRYSTALLINE_CHASM)).build())
                .mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }

    public static Biome eruptingInferno(HolderGetter<PlacedFeature> placedFeatureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter)
    {
        // Mob spawns
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, 60, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, 15, new MobSpawnSettings.SpawnerData(EntityType.PIGLIN, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, 1, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 4));
        spawnBuilder.addSpawn(MobCategory.CREATURE, 100, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2));

        // Biome features
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        biomeBuilder.addCarver(Carvers.NETHER_CAVE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED);
        BiomeDefaultFeatures.addNetherDefaultOres(biomeBuilder);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.INFERNO_LAVA_LAKE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.INFERNO_LAVA_SPRING);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.INFERNO_SPLATTER);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.LARGE_FUMAROLE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.SMALL_FUMAROLE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.BRIMSTONE_BUD);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.BRIMSTONE_CLUSTER);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false).temperature(2.0F).downfall(0.0F)
                .specialEffects((new BiomeSpecialEffects.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(0x4F2B13).skyColor(BOPOverworldBiomes.calculateSkyColor(2.0F)).ambientParticle(new AmbientParticleSettings(ParticleTypes.SMOKE, 0.00023065104F)).ambientLoopSound(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD, 6000, 8, 2.0D)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS, 0.0111D)).backgroundMusic(Musics.createGameMusic(BOPSounds.MUSIC_BIOME_ERUPTING_INFERNO)).build())
                .mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }

    public static Biome undergrowth(HolderGetter<PlacedFeature> placedFeatureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter)
    {
        // Mob spawns
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, 2, new MobSpawnSettings.SpawnerData(EntityType.MAGMA_CUBE, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, 9, new MobSpawnSettings.SpawnerData(EntityType.HOGLIN, 3, 4));
        spawnBuilder.addSpawn(MobCategory.CREATURE, 60, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2));

        // Biome features
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        biomeBuilder.addCarver(Carvers.NETHER_CAVE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_FIRE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.PATCH_SOUL_FIRE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE_EXTRA);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.GLOWSTONE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.BROWN_MUSHROOM_NETHER);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, VegetationPlacements.RED_MUSHROOM_NETHER);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED);
        BiomeDefaultFeatures.addNetherDefaultOres(biomeBuilder);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.TREES_UNDERGROWTH);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.FLOWER_UNDERGROWTH);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.NETHER_BRAMBLE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.SPROUTS_UNDERGROWTH);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.DEAD_GRASS_45);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.NETHER_VINES);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false).temperature(2.0F).downfall(0.0F)
                .specialEffects((new BiomeSpecialEffects.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(0x1C2109).skyColor(BOPOverworldBiomes.calculateSkyColor(2.0F)).ambientParticle(new AmbientParticleSettings(ParticleTypes.SPORE_BLOSSOM_AIR, 0.00357F)).ambientLoopSound(SoundEvents.AMBIENT_WARPED_FOREST_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD, 6000, 8, 2.0D)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS, 0.0111D)).backgroundMusic(Musics.createGameMusic(BOPSounds.MUSIC_BIOME_UNDERGROWTH)).build())
                .mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }

    public static Biome visceralHeap(HolderGetter<PlacedFeature> placedFeatureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter)
    {
        // Mob spawns
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.GHAST, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, 100, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIFIED_PIGLIN, 4, 4));
        spawnBuilder.addSpawn(MobCategory.CREATURE, 60, new MobSpawnSettings.SpawnerData(EntityType.STRIDER, 1, 2));

        // Biome features
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        biomeBuilder.addCarver(Carvers.NETHER_CAVE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_OPEN);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, OrePlacements.ORE_MAGMA);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, NetherPlacements.SPRING_CLOSED);
        BiomeDefaultFeatures.addNetherDefaultOres(biomeBuilder);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.BLOOD_LAKE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.BLOOD_SPRING);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.NETHER_BONE_SPINE);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.FLESH_TENDON);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.HANGING_FLESH_TENDONS);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.POROUS_FLESH);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.PUS_BUBBLES);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.EYEBULB);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.HAIR);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false).temperature(2.0F).downfall(0.0F)
                .specialEffects((new BiomeSpecialEffects.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(0x601F18).skyColor(BOPOverworldBiomes.calculateSkyColor(2.0F)).ambientLoopSound(SoundEvents.AMBIENT_NETHER_WASTES_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D)).ambientAdditionsSound(new AmbientAdditionsSettings(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS, 0.0111D)).backgroundMusic(Musics.createGameMusic(BOPSounds.MUSIC_BIOME_VISCERAL_HEAP)).build())
                .mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }

    public static Biome witheredAbyss(HolderGetter<PlacedFeature> placedFeatureGetter, HolderGetter<ConfiguredWorldCarver<?>> carverGetter)
    {
        // Mob spawns
        MobSpawnSettings.Builder spawnBuilder = new MobSpawnSettings.Builder();
        spawnBuilder.addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 4, 4));
        spawnBuilder.addSpawn(MobCategory.MONSTER, 50, new MobSpawnSettings.SpawnerData(EntityType.WITHER_SKELETON, 1, 1));

        // Biome features
        BiomeGenerationSettings.Builder biomeBuilder = new BiomeGenerationSettings.Builder(placedFeatureGetter, carverGetter);
        biomeBuilder.addCarver(Carvers.NETHER_CAVE);
        BiomeDefaultFeatures.addNetherDefaultOres(biomeBuilder);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.OBSIDIAN_SPLATTER);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.BLACKSTONE_SPINES);
        addFeature(biomeBuilder, GenerationStep.Decoration.UNDERGROUND_DECORATION, BOPNetherPlacements.BLACKSTONE_BULBS);

        return new Biome.BiomeBuilder()
                .hasPrecipitation(false).temperature(2.0F).downfall(0.0F)
                .specialEffects((new BiomeSpecialEffects.Builder()).waterColor(4159204).waterFogColor(329011).fogColor(0x0A0711).skyColor(BOPOverworldBiomes.calculateSkyColor(2.0F)).grassColorOverride(0x312C36).foliageColorOverride(0x312C36).ambientLoopSound(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_LOOP).ambientMoodSound(new AmbientMoodSettings(SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD, 6000, 8, 2.0D)).backgroundMusic(Musics.createGameMusic(BOPSounds.MUSIC_BIOME_WITHERED_ABYSS)).build())
                .mobSpawnSettings(spawnBuilder.build()).generationSettings(biomeBuilder.build()).build();
    }
}
