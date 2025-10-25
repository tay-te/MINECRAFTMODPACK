package com.vastworlds.worldgen;

import com.vastworlds.biome.VastWorldsBiomes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

/**
 * BiomesOPlenty-style surface rules with noise-based variation.
 * Creates natural-looking terrain surfaces by combining biome conditions with noise checks.
 */
public class VastWorldsSurfaceRuleData {

    private static final SurfaceRules.RuleSource AIR = makeStateRule(Blocks.AIR);
    private static final SurfaceRules.RuleSource BEDROCK = makeStateRule(Blocks.BEDROCK);
    private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
    private static final SurfaceRules.RuleSource DEEPSLATE = makeStateRule(Blocks.DEEPSLATE);

    // Grass and dirt
    private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
    private static final SurfaceRules.RuleSource COARSE_DIRT = makeStateRule(Blocks.COARSE_DIRT);

    // Snow
    private static final SurfaceRules.RuleSource SNOW_BLOCK = makeStateRule(Blocks.SNOW_BLOCK);
    private static final SurfaceRules.RuleSource POWDER_SNOW = makeStateRule(Blocks.POWDER_SNOW);
    private static final SurfaceRules.RuleSource PACKED_ICE = makeStateRule(Blocks.PACKED_ICE);

    // Desert/Canyon blocks
    private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
    private static final SurfaceRules.RuleSource RED_SANDSTONE = makeStateRule(Blocks.RED_SANDSTONE);
    private static final SurfaceRules.RuleSource TERRACOTTA = makeStateRule(Blocks.TERRACOTTA);
    private static final SurfaceRules.RuleSource ORANGE_TERRACOTTA = makeStateRule(Blocks.ORANGE_TERRACOTTA);
    private static final SurfaceRules.RuleSource BROWN_TERRACOTTA = makeStateRule(Blocks.BROWN_TERRACOTTA);

    // Plateau blocks
    private static final SurfaceRules.RuleSource MOSS_BLOCK = makeStateRule(Blocks.MOSS_BLOCK);
    private static final SurfaceRules.RuleSource CALCITE = makeStateRule(Blocks.CALCITE);

    /**
     * Creates the main surface rule for Vast Worlds biomes.
     * This combines bedrock generation, biome-specific rules, and noise variation.
     */
    public static SurfaceRules.RuleSource makeRules() {
        // Condition sources for water and surface checks
        SurfaceRules.ConditionSource isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
        SurfaceRules.ConditionSource sixBelowWater = SurfaceRules.waterStartCheck(-6, -1);

        // Surface check (on solid ground vs underground)
        SurfaceRules.ConditionSource isOnSurface = SurfaceRules.ON_FLOOR;

        return SurfaceRules.sequence(
            // Bedrock at the bottom (Y: 0-5)
            SurfaceRules.ifTrue(
                SurfaceRules.verticalGradient("minecraft:bedrock_floor",
                    VerticalAnchor.bottom(),
                    VerticalAnchor.aboveBottom(5)),
                BEDROCK
            ),

            // Deepslate below Y=0
            SurfaceRules.ifTrue(
                SurfaceRules.yBlockCheck(VerticalAnchor.absolute(0), -1),
                DEEPSLATE
            ),

            // MEGA MOUNTAINS biome-specific rules
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(VastWorldsBiomes.MEGA_MOUNTAINS),
                SurfaceRules.sequence(
                    // Steep slopes show exposed stone (noise-based)
                    SurfaceRules.ifTrue(
                        surfaceNoiseAbove(1.8D),
                        STONE
                    ),
                    // High elevations use packed ice
                    SurfaceRules.ifTrue(
                        SurfaceRules.yBlockCheck(VerticalAnchor.absolute(200), 0),
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(isOnSurface, PACKED_ICE),
                            SurfaceRules.ifTrue(surfaceNoiseAbove(0.8D), SNOW_BLOCK)
                        )
                    ),
                    // Snow layers on surface
                    SurfaceRules.ifTrue(
                        isOnSurface,
                        SurfaceRules.sequence(
                            SurfaceRules.ifTrue(surfaceNoiseAbove(1.2D), POWDER_SNOW),
                            SNOW_BLOCK
                        )
                    ),
                    // Underground: stone
                    STONE
                )
            ),

            // DEEP CANYONS biome-specific rules
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(VastWorldsBiomes.DEEP_CANYONS),
                SurfaceRules.sequence(
                    // Noise-based terracotta variation for natural canyon walls
                    SurfaceRules.ifTrue(
                        surfaceNoiseAbove(2.5D),
                        ORANGE_TERRACOTTA
                    ),
                    SurfaceRules.ifTrue(
                        surfaceNoiseAbove(1.5D),
                        BROWN_TERRACOTTA
                    ),
                    SurfaceRules.ifTrue(
                        surfaceNoiseAbove(0.8D),
                        TERRACOTTA
                    ),
                    // Surface layer
                    SurfaceRules.ifTrue(
                        isOnSurface,
                        RED_SAND
                    ),
                    // Underground layers (6 blocks deep)
                    SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(6, false, 0, CaveSurface.FLOOR),
                        RED_SANDSTONE
                    ),
                    // Default: red sandstone
                    RED_SANDSTONE
                )
            ),

            // SKY PLATEAUS biome-specific rules
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(VastWorldsBiomes.SKY_PLATEAUS),
                SurfaceRules.sequence(
                    // Cliff faces show calcite (noise-based)
                    SurfaceRules.ifTrue(
                        surfaceNoiseAbove(2.0D),
                        CALCITE
                    ),
                    // Moss patches on surface (adds variety)
                    SurfaceRules.ifTrue(
                        SurfaceRules.isBiome(VastWorldsBiomes.SKY_PLATEAUS),
                        SurfaceRules.ifTrue(
                            surfaceNoiseAbove(1.5D),
                            SurfaceRules.ifTrue(isOnSurface, MOSS_BLOCK)
                        )
                    ),
                    // Surface layer
                    SurfaceRules.ifTrue(
                        isOnSurface,
                        GRASS_BLOCK
                    ),
                    // Underground layers (4 blocks deep)
                    SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(4, false, 0, CaveSurface.FLOOR),
                        DIRT
                    ),
                    // Default: stone
                    STONE
                )
            ),

            // ENDLESS PLAINS biome-specific rules
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(VastWorldsBiomes.ENDLESS_PLAINS),
                SurfaceRules.sequence(
                    // Occasional coarse dirt patches (adds variety)
                    SurfaceRules.ifTrue(
                        surfaceNoiseAbove(1.2D),
                        SurfaceRules.ifTrue(isOnSurface, COARSE_DIRT)
                    ),
                    // Surface layer
                    SurfaceRules.ifTrue(
                        isOnSurface,
                        GRASS_BLOCK
                    ),
                    // Underground layers (4 blocks deep)
                    SurfaceRules.ifTrue(
                        SurfaceRules.stoneDepthCheck(4, false, 0, CaveSurface.FLOOR),
                        DIRT
                    ),
                    // Default: stone
                    STONE
                )
            ),

            // Default rule (fallback)
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(isOnSurface, GRASS_BLOCK),
                SurfaceRules.ifTrue(
                    SurfaceRules.stoneDepthCheck(4, false, 0, CaveSurface.FLOOR),
                    DIRT
                ),
                STONE
            )
        );
    }

    /**
     * Creates a noise condition that checks if surface noise is above a threshold.
     * This creates natural variation in surface blocks.
     */
    private static SurfaceRules.ConditionSource surfaceNoiseAbove(double threshold) {
        return SurfaceRules.noiseCondition(
            Noises.SURFACE,  // Use Minecraft's built-in surface noise
            threshold / 8.25D
        );
    }

    /**
     * Helper method to create a simple block state rule.
     */
    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
