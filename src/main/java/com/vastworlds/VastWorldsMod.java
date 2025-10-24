package com.vastworlds;

import com.vastworlds.worldgen.VastBiomeTagsProvider;
import com.vastworlds.worldgen.VastWorldgenProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(VastWorldsMod.MODID)
public class VastWorldsMod {
    public static final String MODID = "vastworlds";
    public static final String MOD_ID = MODID;  // Backwards compatibility
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    // Biome Registry Keys
    public static final ResourceKey<Biome> MEGA_MOUNTAINS = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "mega_mountains"));
    public static final ResourceKey<Biome> ENDLESS_PLAINS = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "endless_plains"));
    public static final ResourceKey<Biome> DEEP_CANYONS = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "deep_canyons"));
    public static final ResourceKey<Biome> SKY_PLATEAUS = ResourceKey.create(Registries.BIOME,
        ResourceLocation.fromNamespaceAndPath(MOD_ID, "sky_plateaus"));

    public VastWorldsMod(IEventBus modEventBus) {
        // Register data generation
        modEventBus.addListener(this::onGatherData);

        LOGGER.info("========================================");
        LOGGER.info("Initializing Vast Worlds Mod");
        LOGGER.info("========================================");
        LOGGER.info("Custom biomes: 15 UNIQUE BIOMES");
        LOGGER.info("  Mountains: Mega Mountains, Crystal Peaks, Alpine Meadows, Sky Plateaus");
        LOGGER.info("  Plains: Endless Plains, Sunflower Fields");
        LOGGER.info("  Forests: Ancient Forest, Dark Forest");
        LOGGER.info("  Cold: Frozen Wastes");
        LOGGER.info("  Hot: Deep Canyons, Scorched Badlands, Volcanic Highlands");
        LOGGER.info("  Special: Mushroom Valleys");
        LOGGER.info("  Water: Vast Ocean, Coastal Cliffs");
        LOGGER.info("========================================");
        LOGGER.info("Terrain generation: ADVANCED MULTI-DATAPACK SYSTEM");
        LOGGER.info("Vanilla biomes: COMPLETELY REMOVED");
        LOGGER.info("Density functions: SOPHISTICATED & DRAMATIC");
        LOGGER.info("Scale: VAST - Large biomes, big elevation changes");
        LOGGER.info("========================================");
        LOGGER.info("Vast Worlds initialized successfully!");
    }

    private void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        var lookupProvider = event.getLookupProvider();
        var existingFileHelper = event.getExistingFileHelper();

        // Add worldgen data provider (generates biomes, density functions, noise settings)
        generator.addProvider(event.includeServer(),
            new VastWorldgenProvider(output, lookupProvider));

        // Add biome tags provider (tags our biomes for proper functionality)
        generator.addProvider(event.includeServer(),
            new VastBiomeTagsProvider(output, lookupProvider, existingFileHelper));

        LOGGER.info("Data generation providers registered!");
    }
}
