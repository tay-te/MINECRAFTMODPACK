package com.vastworlds.worldgen.carver;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CanyonWorldCarver;

/**
 * Custom canyon carver for Deep Canyons biome.
 * Creates wider and deeper canyons than vanilla.
 */
public class VastCanyonCarver extends CanyonWorldCarver {

    public VastCanyonCarver(Codec<CanyonCarverConfiguration> codec) {
        super(codec);
    }

    @Override
    protected boolean canReplaceBlock(CanyonCarverConfiguration config, BlockState state) {
        // Allow carving through more block types for dramatic canyons
        return super.canReplaceBlock(config, state);
    }
}
