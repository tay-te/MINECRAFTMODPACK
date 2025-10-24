package com.vastworlds.worldgen;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Provides biome tags - adds our custom biomes to the appropriate Minecraft biome tags
 * This ensures structures, mob spawning, and other features work correctly
 */
public class VastBiomeTagsProvider extends BiomeTagsProvider {

    public VastBiomeTagsProvider(PackOutput output,
                                 CompletableFuture<HolderLookup.Provider> lookupProvider,
                                 @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, VastWorldsMod.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Tag all our biomes as overworld biomes
        tag(BiomeTags.IS_OVERWORLD)
            .add(VastBiomes.MEGA_MOUNTAINS)
            .add(VastBiomes.ENDLESS_PLAINS)
            .add(VastBiomes.DEEP_CANYONS)
            .add(VastBiomes.SKY_PLATEAUS)
            .add(VastBiomes.ANCIENT_FOREST)
            .add(VastBiomes.FROZEN_WASTES)
            .add(VastBiomes.SCORCHED_BADLANDS)
            .add(VastBiomes.MUSHROOM_VALLEYS)
            .add(VastBiomes.CRYSTAL_PEAKS)
            .add(VastBiomes.SUNFLOWER_FIELDS)
            .add(VastBiomes.DARK_FOREST)
            .add(VastBiomes.ALPINE_MEADOWS)
            .add(VastBiomes.VOLCANIC_HIGHLANDS)
            .add(VastBiomes.VAST_OCEAN)
            .add(VastBiomes.COASTAL_CLIFFS);

        // Tag mountain biomes
        tag(BiomeTags.IS_MOUNTAIN)
            .add(VastBiomes.MEGA_MOUNTAINS)
            .add(VastBiomes.CRYSTAL_PEAKS)
            .add(VastBiomes.ALPINE_MEADOWS)
            .add(VastBiomes.SKY_PLATEAUS)
            .add(VastBiomes.VOLCANIC_HIGHLANDS);

        // Tag ocean biomes
        tag(BiomeTags.IS_OCEAN)
            .add(VastBiomes.VAST_OCEAN);

        // Tag beach biomes
        tag(BiomeTags.IS_BEACH)
            .add(VastBiomes.COASTAL_CLIFFS);

        // Tag forest biomes
        tag(BiomeTags.IS_FOREST)
            .add(VastBiomes.ANCIENT_FOREST)
            .add(VastBiomes.DARK_FOREST);

        // Tag hot biomes
        tag(BiomeTags.IS_BADLANDS)
            .add(VastBiomes.SCORCHED_BADLANDS)
            .add(VastBiomes.DEEP_CANYONS)
            .add(VastBiomes.VOLCANIC_HIGHLANDS);

        // Tag cold biomes
        tag(BiomeTags.IS_TAIGA)
            .add(VastBiomes.FROZEN_WASTES);

        // Spawning tags
        tag(BiomeTags.SPAWNS_COLD_VARIANT_FROGS)
            .add(VastBiomes.FROZEN_WASTES)
            .add(VastBiomes.CRYSTAL_PEAKS);

        tag(BiomeTags.SPAWNS_WARM_VARIANT_FROGS)
            .add(VastBiomes.SCORCHED_BADLANDS)
            .add(VastBiomes.DEEP_CANYONS);

        tag(BiomeTags.SPAWNS_SNOW_FOX)
            .add(VastBiomes.FROZEN_WASTES)
            .add(VastBiomes.CRYSTAL_PEAKS)
            .add(VastBiomes.MEGA_MOUNTAINS);

        tag(BiomeTags.SPAWNS_WHITE_RABBITS)
            .add(VastBiomes.FROZEN_WASTES)
            .add(VastBiomes.MEGA_MOUNTAINS);

        tag(BiomeTags.SPAWNS_GOLD_RABBITS)
            .add(VastBiomes.SCORCHED_BADLANDS)
            .add(VastBiomes.DEEP_CANYONS);

        // Allow structures in appropriate biomes
        tag(BiomeTags.HAS_PILLAGER_OUTPOST)
            .add(VastBiomes.ENDLESS_PLAINS)
            .add(VastBiomes.SUNFLOWER_FIELDS);

        tag(BiomeTags.HAS_VILLAGE_PLAINS)
            .add(VastBiomes.ENDLESS_PLAINS)
            .add(VastBiomes.SUNFLOWER_FIELDS);

        tag(BiomeTags.HAS_VILLAGE_SNOWY)
            .add(VastBiomes.FROZEN_WASTES);

        tag(BiomeTags.HAS_MINESHAFT)
            .add(VastBiomes.MEGA_MOUNTAINS)
            .add(VastBiomes.DEEP_CANYONS)
            .add(VastBiomes.SCORCHED_BADLANDS)
            .add(VastBiomes.ANCIENT_FOREST)
            .add(VastBiomes.DARK_FOREST);

        tag(BiomeTags.HAS_STRONGHOLD)
            .add(VastBiomes.ENDLESS_PLAINS)
            .add(VastBiomes.ANCIENT_FOREST)
            .add(VastBiomes.SKY_PLATEAUS);
    }
}
