package com.vastworlds.datagen;

import com.vastworlds.VastWorldsMod;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;

/**
 * Data generation entry point for Vast Worlds
 * This generates biome JSON files automatically
 */
public class VastWorldsDatagen implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        // Register biome provider for data generation
        pack.addProvider(VastWorldsBiomeProvider::new);

        VastWorldsMod.LOGGER.info("Vast Worlds data generation initialized");
    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        // Register our biomes for data generation
        registryBuilder.add(Registries.BIOME, VastWorldsBiomeProvider::bootstrap);
    }
}
