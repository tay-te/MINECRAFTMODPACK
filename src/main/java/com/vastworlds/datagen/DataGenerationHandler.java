package com.vastworlds.datagen;

import com.vastworlds.VastWorldsMod;
import com.vastworlds.biome.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Data generation handler following BiomesOPlenty's pattern.
 * Generates JSON files from programmatic biome definitions.
 */
@EventBusSubscriber(modid = VastWorldsMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerationHandler {

    /**
     * Registry set builder for all datagen registries.
     * Following BiomesOPlenty's structure.
     */
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, ModBiomes::bootstrapBiomes);

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // Generate datapack built-in entries (biomes, features, etc.)
        generator.addProvider(event.includeServer(),
            new DatapackBuiltinEntriesProvider(output, lookupProvider, BUILDER, Set.of(VastWorldsMod.MOD_ID)));

        VastWorldsMod.LOGGER.info("Registered Vast Worlds datagen providers");
    }
}
