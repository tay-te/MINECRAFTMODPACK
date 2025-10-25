package com.vastworlds.datagen;

import com.vastworlds.biome.VastBiomes;
import com.vastworlds.biome.VastWorldsBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;

import java.util.concurrent.CompletableFuture;

/**
 * Provides biome data for data generation
 */
public class VastWorldsBiomeProvider extends FabricDynamicRegistryProvider {

    public VastWorldsBiomeProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider registries, Entries entries) {
        // Add all our biomes to be generated
        entries.addAll(registries.lookupOrThrow(Registries.BIOME));
    }

    @Override
    public String getName() {
        return "Vast Worlds Biomes";
    }

    /**
     * Bootstrap method that registers biomes during data generation
     * NOTE: Disabled - we're using JSON biome definitions instead of programmatic generation
     */
    public static void bootstrap(net.minecraft.data.worldgen.BootstrapContext<net.minecraft.world.level.biome.Biome> context) {
        // Biomes are defined via JSON files in resources/data/vastworlds/worldgen/biome/
        // This avoids feature ordering conflicts
    }
}
