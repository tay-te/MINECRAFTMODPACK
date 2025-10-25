/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.worldgen.feature.misc;

import biomesoplenty.api.block.BOPBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SparseDuneGrassFeature extends Feature<NoneFeatureConfiguration>
{
    public SparseDuneGrassFeature(Codec<NoneFeatureConfiguration> deserializer)
    {
        super(deserializer);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContext)
    {
        WorldGenLevel world = featurePlaceContext.level();
        ChunkGenerator chunkGenerator = featurePlaceContext.chunkGenerator();
        RandomSource rand = featurePlaceContext.random();
        BlockPos pos = featurePlaceContext.origin();
        NoneFeatureConfiguration config = featurePlaceContext.config();
        int i = 0;

        for(int j = 0; j < 128; ++j)
        {
            BlockPos blockpos = pos.offset(rand.nextInt(4) - rand.nextInt(4), rand.nextInt(2) - rand.nextInt(2), rand.nextInt(4) - rand.nextInt(4));
            if (world.getBlockState(blockpos).isAir())
            {
                if (world.getBlockState(blockpos.below()).getBlock() == BOPBlocks.ORANGE_SAND)
                {
                    world.setBlock(blockpos, BOPBlocks.DUNE_GRASS.defaultBlockState(), 2);
                }
                else if (world.getBlockState(blockpos.below()).getBlock() == BOPBlocks.ORANGE_SANDSTONE)
                {
                    if (rand.nextInt(4) == 0)
                    {
                        world.setBlock(blockpos, BOPBlocks.SPROUT.defaultBlockState(), 2);
                    }
                }

                ++i;
            }
        }

        return i > 0;
    }
}
