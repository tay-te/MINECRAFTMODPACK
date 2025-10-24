package com.vastworlds.worldgen.

;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Noises;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import static net.minecraft.world.level.levelgen.DensityFunctions.*;

public class VastDensityFunctions {

    // Density Function Keys
    public static final ResourceKey<DensityFunction> BASE_3D_NOISE = register("base_3d_noise");
    public static final ResourceKey<DensityFunction> CONTINENTS = register("continents");
    public static final ResourceKey<DensityFunction> EROSION = register("erosion");
    public static final ResourceKey<DensityFunction> RIDGES = register("ridges");
    public static final ResourceKey<DensityFunction> OFFSET = register("offset");
    public static final ResourceKey<DensityFunction> FACTOR = register("factor");
    public static final ResourceKey<DensityFunction> JAGGEDNESS = register("jaggedness");
    public static final ResourceKey<DensityFunction> DEPTH = register("depth");
    public static final ResourceKey<DensityFunction> SLOPED_CHEESE = register("sloped_cheese");
    public static final ResourceKey<DensityFunction> CONTINENTS_LARGE = register("continents_large");
    public static final ResourceKey<DensityFunction> EROSION_LARGE = register("erosion_large");

    private static ResourceKey<DensityFunction> register(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION,
            ResourceLocation.fromNamespaceAndPath(VastWorldsMod.MODID, name));
    }

    public static void bootstrap(BootstrapContext<DensityFunction> context) {
        HolderGetter<DensityFunction> functions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);

        // Register all density functions with improved terrain generation
        context.register(BASE_3D_NOISE, base3dNoise(noises));
        context.register(CONTINENTS, continents(noises));
        context.register(EROSION, erosion(noises));
        context.register(RIDGES, ridges(noises));
        context.register(OFFSET, offset(functions, noises));
        context.register(FACTOR, factor(functions, noises));
        context.register(JAGGEDNESS, jaggedness(noises));
        context.register(DEPTH, depth(functions));
        context.register(SLOPED_CHEESE, slopedCheese(functions, noises));
        context.register(CONTINENTS_LARGE, continentsLarge(noises));
        context.register(EROSION_LARGE, erosionLarge(noises));
    }

    /**
     * Base 3D noise for terrain generation - creates the fundamental terrain shape
     */
    private static DensityFunction base3dNoise(HolderGetter<NormalNoise.NoiseParameters> noises) {
        // Use 3D noise at a large scale for vast, smooth terrain
        return add(
            constant(0.1),
            mul(
                constant(1.5),
                noise(noises.getOrThrow(Noises.JAGGED), 1500.0, 0.0)
            )
        );
    }

    /**
     * Continentalness - controls land vs ocean, larger scale = bigger continents
     */
    private static DensityFunction continents(HolderGetter<NormalNoise.NoiseParameters> noises) {
        // Scale increased to 0.3 for larger landmasses
        return flatCache(
            shiftedNoise2d(
                noises.getOrThrow(Noises.CONTINENTALNESS),
                noises.getOrThrow(Noises.SHIFT_X),
                noises.getOrThrow(Noises.SHIFT_Z),
                0.3,
                0.0
            )
        );
    }

    /**
     * Large-scale continentalness for mega regions
     */
    private static DensityFunction continentsLarge(HolderGetter<NormalNoise.NoiseParameters> noises) {
        return flatCache(
            shiftedNoise2d(
                noises.getOrThrow(Noises.CONTINENTALNESS),
                noises.getOrThrow(Noises.SHIFT_X),
                noises.getOrThrow(Noises.SHIFT_Z),
                0.15,  // Even larger scale
                0.0
            )
        );
    }

    /**
     * Erosion - controls terrain smoothness/jaggedness
     */
    private static DensityFunction erosion(HolderGetter<NormalNoise.NoiseParameters> noises) {
        // Scale 0.25 creates varied erosion patterns
        return flatCache(
            shiftedNoise2d(
                noises.getOrThrow(Noises.EROSION),
                noises.getOrThrow(Noises.SHIFT_X),
                noises.getOrThrow(Noises.SHIFT_Z),
                0.25,
                0.0
            )
        );
    }

    /**
     * Large-scale erosion for dramatic features
     */
    private static DensityFunction erosionLarge(HolderGetter<NormalNoise.NoiseParameters> noises) {
        return flatCache(
            shiftedNoise2d(
                noises.getOrThrow(Noises.EROSION),
                noises.getOrThrow(Noises.SHIFT_X),
                noises.getOrThrow(Noises.SHIFT_Z),
                0.12,  // Larger scale erosion features
                0.0
            )
        );
    }

    /**
     * Ridges - creates mountain ridges and peaks
     */
    private static DensityFunction ridges(HolderGetter<NormalNoise.NoiseParameters> noises) {
        // Use ridges noise for dramatic mountain features
        return flatCache(
            shiftedNoise2d(
                noises.getOrThrow(Noises.RIDGE),
                noises.getOrThrow(Noises.SHIFT_X),
                noises.getOrThrow(Noises.SHIFT_Z),
                0.25,
                0.0
            )
        );
    }

    /**
     * Jaggedness - adds detail and roughness to terrain
     */
    private static DensityFunction jaggedness(HolderGetter<NormalNoise.NoiseParameters> noises) {
        // Amplified jaggedness for dramatic terrain
        return mul(
            constant(1.8),
            noise(noises.getOrThrow(Noises.JAGGED), 1500.0, 0.0)
        );
    }

    /**
     * Offset - vertical offset that creates height variation
     */
    private static DensityFunction offset(HolderGetter<DensityFunction> functions,
                                         HolderGetter<NormalNoise.NoiseParameters> noises) {
        DensityFunction continents = functions.getOrThrow(CONTINENTS);
        DensityFunction erosion = functions.getOrThrow(EROSION);
        DensityFunction ridges = functions.getOrThrow(RIDGES);

        // Complex offset calculation for varied height
        // Higher continentalness = higher base elevation
        // Erosion modulates the effect
        // Ridges add extra height to mountain areas
        return add(
            mul(constant(0.5), continents),
            add(
                mul(constant(-0.3), erosion),
                mul(constant(0.4), ridges)
            )
        );
    }

    /**
     * Factor - scales the terrain height
     */
    private static DensityFunction factor(HolderGetter<DensityFunction> functions,
                                         HolderGetter<NormalNoise.NoiseParameters> noises) {
        DensityFunction erosion = functions.getOrThrow(EROSION);
        DensityFunction ridges = functions.getOrThrow(RIDGES);

        // Factor amplification: more dramatic in mountainous areas
        // Base factor of 1.2, increased by ridges, reduced by erosion
        return add(
            constant(1.2),
            add(
                mul(constant(0.6), ridges),
                mul(constant(-0.2), erosion)
            )
        );
    }

    /**
     * Depth - base terrain height before modifications
     */
    private static DensityFunction depth(HolderGetter<DensityFunction> functions) {
        DensityFunction continents = functions.getOrThrow(CONTINENTS);
        DensityFunction erosion = functions.getOrThrow(EROSION);

        // Depth calculation: continents provide base, erosion carves
        return add(
            mul(constant(1.5), continents),
            mul(constant(-0.5), erosion)
        );
    }

    /**
     * Sloped cheese - the final density function that determines terrain shape
     * This is the heart of terrain generation
     */
    private static DensityFunction slopedCheese(HolderGetter<DensityFunction> functions,
                                               HolderGetter<NormalNoise.NoiseParameters> noises) {
        DensityFunction offset = functions.getOrThrow(OFFSET);
        DensityFunction factor = functions.getOrThrow(FACTOR);
        DensityFunction jaggedness = functions.getOrThrow(JAGGEDNESS);
        DensityFunction depth = functions.getOrThrow(DEPTH);
        DensityFunction base3d = functions.getOrThrow(BASE_3D_NOISE);

        // Complex terrain formula:
        // 1. Start with base 3D noise
        // 2. Add jaggedness for detail
        // 3. Multiply by factor for amplification
        // 4. Add depth for base elevation
        // 5. Apply offset for final height adjustment
        DensityFunction terrain = add(
            add(base3d, jaggedness),
            mul(
                factor,
                add(depth, offset)
            )
        );

        // Apply Y-gradient to ensure terrain generates properly at different heights
        // Negative at bottom (solid), positive at top (air)
        DensityFunction yGradient = yClampedGradient(-64, 320, 1.0, -1.0);

        // Combine terrain with Y-gradient
        return add(terrain, yGradient);
    }

    /**
     * Helper method to create shifted 2D noise
     */
    private static DensityFunction shiftedNoise2d(NormalNoise.NoiseParameters noise,
                                                  NormalNoise.NoiseParameters shiftX,
                                                  NormalNoise.NoiseParameters shiftZ,
                                                  double scale,
                                                  double yScale) {
        return DensityFunctions.flatCache(
            DensityFunctions.shiftedNoise2d(
                DensityFunctions.shiftA(noise),
                DensityFunctions.shiftB(noise),
                scale,
                DensityFunctions.noise(shiftX),
                DensityFunctions.noise(shiftZ)
            )
        );
    }
}
