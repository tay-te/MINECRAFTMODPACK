package com.vastworlds.worldgen;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;

import static net.minecraft.world.level.levelgen.DensityFunctions.*;

public class VastNoiseSettings {

    public static final ResourceKey<NoiseGeneratorSettings> VAST_OVERWORLD = register("vast_overworld");

    private static ResourceKey<NoiseGeneratorSettings> register(String name) {
        return ResourceKey.create(Registries.NOISE_SETTINGS,
            ResourceLocation.fromNamespaceAndPath(VastWorldsMod.MODID, name));
    }

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);

        context.register(VAST_OVERWORLD, vastOverworld(densityFunctions));
    }

    private static NoiseGeneratorSettings vastOverworld(HolderGetter<DensityFunction> densityFunctions) {
        return new NoiseGeneratorSettings(
            // Noise settings: defines the world height and generation resolution
            new NoiseSettings(
                -64,    // min_y
                384,    // height (320 - (-64) = 384)
                1,      // size_horizontal (1 = 4 blocks per cell)
                2       // size_vertical (2 = 8 blocks per cell)
            ),
            // Default block (underground)
            Blocks.STONE.defaultBlockState(),
            // Default fluid (seas and lakes)
            Blocks.WATER.defaultBlockState(),
            // Noise router: defines how noise functions affect terrain
            createNoiseRouter(densityFunctions),
            // Surface rules: defines what blocks appear on the surface
            createSurfaceRules(),
            // Spawn target (affects mob spawning)
            // Empty list = use biome-defined spawning
            java.util.List.of(),
            // Sea level
            63,
            // Disable mob generation
            false,
            // Aquifers enabled (underground water)
            true,
            // Ore veins enabled
            true,
            // Legacy random source
            false
        );
    }

    private static NoiseRouter createNoiseRouter(HolderGetter<DensityFunction> functions) {
        // Get our custom density functions
        DensityFunction continents = functions.getOrThrow(VastDensityFunctions.CONTINENTS);
        DensityFunction erosion = functions.getOrThrow(VastDensityFunctions.EROSION);
        DensityFunction ridges = functions.getOrThrow(VastDensityFunctions.RIDGES);
        DensityFunction slopedCheese = functions.getOrThrow(VastDensityFunctions.SLOPED_CHEESE);
        DensityFunction depth = functions.getOrThrow(VastDensityFunctions.DEPTH);

        // Create temperature and vegetation noise for biome placement
        // These use vanilla-style parameters but can be customized
        DensityFunction temperature = shiftedNoise(
            continents,
            constant(0.0),
            constant(0.0),
            0.25
        );

        DensityFunction vegetation = shiftedNoise(
            erosion,
            constant(0.0),
            constant(0.0),
            0.25
        );

        // Barrier: prevents floating islands (usually 0)
        DensityFunction barrier = constant(0.0);

        // Fluid level: controls where water appears
        DensityFunction fluidLevelFloodedness = constant(-1.0);
        DensityFunction fluidLevelSpread = constant(0.0);

        // Lava: controls lava lakes
        DensityFunction lava = constant(0.0);

        // Vein toggle and gap: for ore veins
        DensityFunction veinToggle = constant(0.0);
        DensityFunction veinRidged = constant(0.0);
        DensityFunction veinGap = constant(0.0);

        return new NoiseRouter(
            barrier,
            fluidLevelFloodedness,
            fluidLevelSpread,
            lava,
            temperature,
            vegetation,
            continents,
            erosion,
            depth,
            ridges,
            slopedCheese,  // initial_density_without_jaggedness
            squeeze(slopedCheese),  // final_density
            veinToggle,
            veinRidged,
            veinGap
        );
    }

    private static SurfaceRules.RuleSource createSurfaceRules() {
        // Surface rules define what blocks appear on the surface based on biome and conditions

        // Define block states we'll use
        BlockState GRASS_BLOCK = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState DIRT = Blocks.DIRT.defaultBlockState();
        BlockState STONE = Blocks.STONE.defaultBlockState();
        BlockState SAND = Blocks.SAND.defaultBlockState();
        BlockState SANDSTONE = Blocks.SANDSTONE.defaultBlockState();
        BlockState RED_SAND = Blocks.RED_SAND.defaultBlockState();
        BlockState RED_SANDSTONE = Blocks.RED_SANDSTONE.defaultBlockState();
        BlockState SNOW_BLOCK = Blocks.SNOW_BLOCK.defaultBlockState();
        BlockState PACKED_ICE = Blocks.PACKED_ICE.defaultBlockState();
        BlockState GRAVEL = Blocks.GRAVEL.defaultBlockState();
        BlockState MYCELIUM = Blocks.MYCELIUM.defaultBlockState();
        BlockState BASALT = Blocks.BASALT.defaultBlockState();
        BlockState BLACKSTONE = Blocks.BLACKSTONE.defaultBlockState();

        // Conditions
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource isOnSurface = SurfaceRules.ON_FLOOR;
        SurfaceRules.ConditionSource isUnderwater = SurfaceRules.not(isAtOrAboveWaterLevel);
        SurfaceRules.ConditionSource isCold = SurfaceRules.temperature().below(-0.4F);
        SurfaceRules.ConditionSource isHot = SurfaceRules.temperature().above(1.5F);

        // Biome-specific rules
        SurfaceRules.RuleSource megaMountainsRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.MEGA_MOUNTAINS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(SNOW_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(STONE))
            )
        );

        SurfaceRules.RuleSource crystalPeaksRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.CRYSTAL_PEAKS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(SNOW_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(PACKED_ICE))
            )
        );

        SurfaceRules.RuleSource frozenWastesRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.FROZEN_WASTES),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(SNOW_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(DIRT))
            )
        );

        SurfaceRules.RuleSource plainsRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.ENDLESS_PLAINS, VastBiomes.SUNFLOWER_FIELDS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(GRASS_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(DIRT))
            )
        );

        SurfaceRules.RuleSource forestRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.ANCIENT_FOREST, VastBiomes.DARK_FOREST),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(GRASS_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(DIRT))
            )
        );

        SurfaceRules.RuleSource canyonRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.DEEP_CANYONS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(RED_SAND)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(RED_SANDSTONE))
            )
        );

        SurfaceRules.RuleSource badlandsRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.SCORCHED_BADLANDS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(RED_SAND)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(RED_SANDSTONE))
            )
        );

        SurfaceRules.RuleSource volcanicRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.VOLCANIC_HIGHLANDS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(BASALT)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(BLACKSTONE))
            )
        );

        SurfaceRules.RuleSource mushroomRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.MUSHROOM_VALLEYS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(MYCELIUM)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(DIRT))
            )
        );

        SurfaceRules.RuleSource plateauRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.SKY_PLATEAUS, VastBiomes.ALPINE_MEADOWS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(GRASS_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(DIRT))
            )
        );

        SurfaceRules.RuleSource oceanRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.VAST_OCEAN),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, SurfaceRules.state(GRAVEL)),
                SurfaceRules.state(STONE)
            )
        );

        SurfaceRules.RuleSource coastalRule = SurfaceRules.ifTrue(
            SurfaceRules.isBiome(VastBiomes.COASTAL_CLIFFS),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(GRASS_BLOCK)),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                    SurfaceRules.state(STONE))
            )
        );

        // Combine all rules in order (first match wins)
        return SurfaceRules.sequence(
            // Special temperature-based overrides
            SurfaceRules.ifTrue(isCold,
                SurfaceRules.sequence(
                    crystalPeaksRule,
                    frozenWastesRule,
                    megaMountainsRule
                )
            ),
            SurfaceRules.ifTrue(isHot,
                SurfaceRules.sequence(
                    volcanicRule,
                    canyonRule,
                    badlandsRule
                )
            ),
            // Biome-specific rules
            oceanRule,
            coastalRule,
            mushroomRule,
            plateauRule,
            forestRule,
            plainsRule,
            // Default fallback
            SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(GRASS_BLOCK)),
            SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 3, SurfaceRules.CaveSurface.FLOOR),
                SurfaceRules.state(DIRT)),
            SurfaceRules.state(STONE)
        );
    }

    /**
     * Helper method for shifted noise (simplified version)
     */
    private static DensityFunction shiftedNoise(DensityFunction base, DensityFunction x, DensityFunction z, double scale) {
        return add(mul(constant(scale), base), add(x, z));
    }
}
