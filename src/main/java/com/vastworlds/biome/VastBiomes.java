package com.vastworlds.biome;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

/**
 * Registry keys for all Vast Worlds custom biomes
 */
public class VastBiomes {
    // Custom biomes - we'll start with a few examples
    public static final ResourceKey<Biome> MEGA_PLAINS = register("mega_plains");
    public static final ResourceKey<Biome> TOWERING_HILLS = register("towering_hills");
    public static final ResourceKey<Biome> DEEP_VALLEY = register("deep_valley");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(
            Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(VastWorldsMod.MOD_ID, name)
        );
    }

    /**
     * Called during mod initialization to ensure class is loaded
     */
    public static void init() {
        VastWorldsMod.LOGGER.info("Registering Vast Worlds biome keys");
    }
}
