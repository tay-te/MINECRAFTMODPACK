package com.vastworlds.biome;

import com.mojang.datafixers.util.Pair;
import com.vastworlds.VastWorldsMod;
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
 * This integrates our custom biomes into the world generation system
 * by mapping them to climate parameters.
 */
public class VastWorldsRegion extends Region {
    public static final ResourceLocation LOCATION = ResourceLocation.fromNamespaceAndPath(
            VastWorldsMod.MOD_ID, "overworld");

    public VastWorldsRegion(int weight) {
        super(LOCATION, RegionType.OVERWORLD, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // Use our biome builder to add all the biomes with their climate parameters
        (new VastWorldsBiomeBuilder()).addBiomes(registry, mapper);
    }
}
