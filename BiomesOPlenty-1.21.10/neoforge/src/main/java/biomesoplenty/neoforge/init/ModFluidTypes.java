/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.neoforge.init;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.neoforge.core.BiomesOPlentyNeoForge;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.PathType;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Supplier;

public class ModFluidTypes
{
    public static Holder<FluidType> BLOOD_TYPE;
    public static Holder<FluidType> LIQUID_NULL_TYPE;

    public static void setup()
    {
        registerFluids();
    }

    public static void registerFluids()
    {
        BLOOD_TYPE = registerFluidType(() -> new FluidType(FluidType.Properties.create()
                        .descriptionId("block.biomesoplenty.blood")
                        .fallDistanceModifier(0F)
                        .canExtinguish(true)
                        .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                        .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                        .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                        .density(3000)
                        .viscosity(6000))
        {
            @Override
            public PathType getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog)
            {
                return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
            }
        }, "blood");

        LIQUID_NULL_TYPE = registerFluidType(() -> new FluidType(FluidType.Properties.create()
                .descriptionId("block.biomesoplenty.liquid_null")
                .fallDistanceModifier(0F)
                .canExtinguish(false)
                .sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL)
                .sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY)
                .sound(SoundActions.FLUID_VAPORIZE, SoundEvents.FIRE_EXTINGUISH)
                .density(3000)
                .viscosity(6000))
        {
            @Override
            public PathType getBlockPathType(FluidState state, BlockGetter level, BlockPos pos, @Nullable Mob mob, boolean canFluidLog)
            {
                return canFluidLog ? super.getBlockPathType(state, level, pos, mob, true) : null;
            }
        }, "liquid_null");
    }

    public static DeferredHolder<FluidType, FluidType> registerFluidType(Supplier<FluidType> fluidSupplier, String name)
    {
        return BiomesOPlentyNeoForge.FORGE_FLUID_REGISTER.register(name, fluidSupplier);
    }

    public static void registerFluidInteractions()
    {
        for (Map.Entry<ResourceKey<FluidType>, FluidType> fluidType : NeoForgeRegistries.FLUID_TYPES.entrySet())
        {
            if (fluidType.getValue() != NeoForgeMod.EMPTY_TYPE.value() && fluidType.getValue() != ModFluidTypes.BLOOD_TYPE.value())
            {
                FluidInteractionRegistry.addInteraction(fluidType.getValue(), new FluidInteractionRegistry.InteractionInformation(
                        ModFluidTypes.BLOOD_TYPE.value(),
                        fluidState -> fluidState.isSource() ? BOPBlocks.FLESH.defaultBlockState() : BOPBlocks.POROUS_FLESH.defaultBlockState()
                ));
            }
            if (fluidType.getValue() != NeoForgeMod.EMPTY_TYPE.value() && fluidType.getValue() != ModFluidTypes.LIQUID_NULL_TYPE.value())
            {
                FluidInteractionRegistry.addInteraction(fluidType.getValue(), new FluidInteractionRegistry.InteractionInformation(
                        ModFluidTypes.LIQUID_NULL_TYPE.value(), BOPBlocks.NULL_BLOCK.defaultBlockState()));
            }
        }
    }
}
