package com.vastworlds.biome;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class VastWorldsBiomes {

    // Extreme terrain biomes
    public static final ResourceKey<Biome> MEGA_MOUNTAINS = register("mega_mountains");
    public static final ResourceKey<Biome> ENDLESS_PLAINS = register("endless_plains");
    public static final ResourceKey<Biome> DEEP_CANYONS = register("deep_canyons");
    public static final ResourceKey<Biome> SKY_PLATEAUS = register("sky_plateaus");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME,
            ResourceLocation.fromNamespaceAndPath(VastWorldsMod.MOD_ID, name));
    }

    public static void init() {
        VastWorldsMod.LOGGER.info("Registering Vast Worlds biomes...");
    }
}
