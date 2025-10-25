/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.worldgen.feature.misc;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.util.SimpleBlockPredicate;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

public class FallenLogFeature extends Feature<NoneFeatureConfiguration>
{
    protected SimpleBlockPredicate placeOn = (world, pos) -> world.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK || world.getBlockState(pos).getBlock() == Blocks.COARSE_DIRT;
    protected SimpleBlockPredicate replace = (world, pos) -> TreeFeature.isAirOrLeaves(world, pos) || world.getBlockState(pos).getBlock() instanceof VegetationBlock;

    public FallenLogFeature(Codec<NoneFeatureConfiguration> deserializer)
    {
        super(deserializer);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext)
    {
        WorldGenLevel world = featurePlaceContext.level();
        ChunkGenerator chunkGenerator = featurePlaceContext.chunkGenerator();
        RandomSource rand = featurePlaceContext.random();
        BlockPos startPos = featurePlaceContext.origin();
        NoneFeatureConfiguration config = featurePlaceContext.config();
        while (startPos.getY() >= world.getMinY()+1 && this.replace.matches(world, startPos)) {
            startPos = startPos.below();
        }

        int length = 4 + rand.nextInt(4);
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(rand);

        int groundCheck = 0;
        boolean startConnected = false;
        boolean endConnected = false;

        int groundRequired;
        if (length % 2 == 0)
        {
            groundRequired = length - 1;
        }
        else
        {
            groundRequired = length - 2;
        }

        for (int i = 0; i < length; i++)
        {
            if (this.placeOn.matches(world, startPos.relative(direction, i)))
            {
                if (i <= 1) { startConnected = true; }
                if (i >= length-2) { endConnected = true; }

                groundCheck++;
            }
        }

        if (groundCheck < groundRequired)
        {
            if (!startConnected)
            {
                return false;
            }
            if (!endConnected)
            {
                return false;
            }
        }

        if (!this.checkSpace(world, startPos.above(), direction, length))
        {
            // Abandon if there isn't enough room
            return false;
        }

        BlockPos pos = startPos.above();

        for (int i = 0; i < length; i++)
        {
            this.setBlock(world, pos.relative(direction, i), Blocks.OAK_LOG.defaultBlockState().setValue(RotatedPillarBlock.AXIS, direction.getAxis()));

            BlockState blockAbove = world.getBlockState(pos.above().relative(direction, i));
            if (blockAbove.isAir() || blockAbove.getBlock() instanceof VegetationBlock)
            {
                if (rand.nextInt(4) == 0)
                {
                    this.setBlock(world, pos.above().relative(direction, i), BOPBlocks.TOADSTOOL.defaultBlockState());
                }
                else
                {
                    this.setBlock(world, pos.above().relative(direction, i), Blocks.MOSS_CARPET.defaultBlockState());
                }
            }

            BlockState blockBelow = world.getBlockState(pos.below().relative(direction, i));
            if (blockBelow.isAir() || blockBelow.getFluidState().is(Fluids.WATER) || blockBelow.getBlock() instanceof VegetationBlock)
            {
                this.setBlock(world, pos.below().relative(direction, i), Blocks.HANGING_ROOTS.defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(world.isWaterAt(pos.below().relative(direction, i)))));
            }
            if (blockBelow.is(BlockTags.DIRT))
            {
                super.setBlock(world, pos.below().relative(direction, i), Blocks.ROOTED_DIRT.defaultBlockState());
            }
        }

        return true;
    }

    public boolean setBlock(WorldGenLevel world, BlockPos pos, BlockState state)
    {
        if (this.replace.matches(world, pos))
        {
            super.setBlock(world, pos, state);
            return true;
        }
        else if (world.getBlockState(pos).getFluidState().is(Fluids.WATER) && state.getBlock() == Blocks.HANGING_ROOTS)
        {
            super.setBlock(world, pos, state);
            return true;
        }

        return false;
    }

    public boolean checkSpace(WorldGenLevel world, BlockPos pos, Direction direction, int length)
    {
        for (int y = 0; y <= 2; y++)
        {
            for (int i = 0; i < length; i++)
            {
                BlockPos pos1 = pos.above(y).relative(direction, i);
                if (pos1.getY() >= 255 || !this.replace.matches(world, pos1))
                {
                    return false;
                }
            }
        }
        return true;
    }
}
