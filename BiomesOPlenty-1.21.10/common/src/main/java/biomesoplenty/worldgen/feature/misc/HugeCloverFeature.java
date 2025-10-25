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
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class HugeCloverFeature extends Feature<NoneFeatureConfiguration>
{
    protected SimpleBlockPredicate placeOn = (world, pos) -> world.getBlockState(pos).is(BlockTags.DIRT);
    protected SimpleBlockPredicate replace = (world, pos) -> TreeFeature.isAirOrLeaves(world, pos) || world.getBlockState(pos).getBlock() instanceof VegetationBlock;

    public HugeCloverFeature(Codec<NoneFeatureConfiguration> deserializer)
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
        while (startPos.getY() >= world.getMinY()+1 && this.replace.matches(world, startPos)) {startPos = startPos.below();}

        if (!this.placeOn.matches(world, startPos))
        {
            // Abandon if we can't place the tree on this block
            return false;
        }

        if (!this.checkSpace(world, startPos.above()))
        {
            // Abandon if there isn't enough room
            return false;
        }

        BlockPos pos = startPos.above();

        this.setBlock(world, pos, BOPBlocks.HUGE_CLOVER_PETAL.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
        this.setBlock(world, pos.south(), BOPBlocks.HUGE_CLOVER_PETAL.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.WEST));
        this.setBlock(world, pos.east(), BOPBlocks.HUGE_CLOVER_PETAL.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.EAST));
        this.setBlock(world, pos.south().east(), BOPBlocks.HUGE_CLOVER_PETAL.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, Direction.SOUTH));

        this.setBlock(world, pos.above(), Blocks.AIR.defaultBlockState());
        this.setBlock(world, pos.south().above(), Blocks.AIR.defaultBlockState());
        this.setBlock(world, pos.east().above(), Blocks.AIR.defaultBlockState());
        this.setBlock(world, pos.south().east().above(), Blocks.AIR.defaultBlockState());

        return true;
    }

    public boolean setBlock(WorldGenLevel world, BlockPos pos, BlockState state)
    {
        if (this.replace.matches(world, pos))
        {
            super.setBlock(world, pos, state);
            return true;
        }
        return false;
    }

    public boolean checkSpace(WorldGenLevel world, BlockPos pos)
    {
        for (int x = 0; x <= 1; x++)
        {
            for (int z = 0; z <= 1; z++)
            {
                BlockPos pos1 = pos.offset(x, 0, z);
                if (pos1.getY() >= 255 || !this.replace.matches(world, pos1) || !this.placeOn.matches(world, pos1.below()))
                {
                    return false;
                }
            }
        }

        return true;
    }
}
