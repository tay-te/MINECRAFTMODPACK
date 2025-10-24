package com.vastworlds.worldgen;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Main data provider for worldgen - generates all biomes, density functions, noise settings, etc.
 * This replaces manual JSON writing with type-safe Java code.
 */
public class VastWorldgenProvider extends DatapackBuiltinEntriesProvider {

    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
        .add(Registries.BIOME, VastBiomes::bootstrap)
        .add(Registries.DENSITY_FUNCTION, VastDensityFunctions::bootstrap)
        .add(Registries.NOISE_SETTINGS, VastNoiseSettings::bootstrap)
        .add(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST, VastMultiNoise::bootstrap);

    public VastWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(VastWorldsMod.MODID));
    }
}
