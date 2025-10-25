package com.vastworlds.worldgen;

import com.mojang.datafixers.util.Pair;
import com.vastworlds.VastWorldsMod;
import com.vastworlds.biome.VastWorldsBiomeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

/**
 * TerraBlender Region for Vast Worlds biomes.
 * This class integrates our custom biomes into the world generation system.
 */
public class VastWorldsRegion extends Region {

    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(
        VastWorldsMod.MOD_ID, "overworld"
    );

    /**
     * Creates a Vast Worlds region with the specified weight.
     * Higher weight means more presence in the world.
     *
     * @param weight The region weight (recommended: 3-5 for significant presence)
     */
    public VastWorldsRegion(int weight) {
        super(LOCATION, RegionType.OVERWORLD, weight);
    }

    /**
     * Adds biomes to the world using the VastWorldsBiomeBuilder.
     * Called by TerraBlender during world generation setup.
     */
    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Use our custom biome builder to add biomes with climate parameters
        new VastWorldsBiomeBuilder().addBiomes(registry, mapper);
    }
}
