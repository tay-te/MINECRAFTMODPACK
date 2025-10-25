/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.block;

import biomesoplenty.api.block.BOPBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class HangingCobwebBottomBlock extends HangingStrandBottomBlock {

    public HangingCobwebBottomBlock(Properties p_i241195_1_) {
        super(p_i241195_1_);
    }

    @Override
    protected Block getBodyBlock() {
        return BOPBlocks.HANGING_COBWEB_STRAND;
    }

    @Override
    public boolean canSurvive(BlockState p_196260_1_, LevelReader p_196260_2_, BlockPos p_196260_3_) {
        BlockPos blockpos = p_196260_3_.relative(this.growthDirection.getOpposite());
        BlockState blockstate = p_196260_2_.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        if (!this.canAttachTo(blockstate)) {
            return false;
        } else {
            return block == this.getHeadBlock() || block == this.getBodyBlock() || blockstate.isFaceSturdy(p_196260_2_, blockpos, Direction.DOWN);
        }
    }

    @Override
    protected void entityInside(BlockState state, Level p_154264_, BlockPos p_154265_, Entity entity, InsideBlockEffectApplier p_405853_, boolean b)
    {
        entity.makeStuckInBlock(state, new Vec3(0.75D, (double)0.5F, 0.75D));
    }
}