/*******************************************************************************
 * Copyright 2022, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.block;

import biomesoplenty.api.block.BOPBlocks;
import biomesoplenty.block.properties.ConnectedProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class StringyCobwebBlock extends Block
{
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<ConnectedProperty> CONNECTED = EnumProperty.create("connected", ConnectedProperty.class);
    protected static final VoxelShape SHAPE_X = Block.box(0.0D, 0.0D, 7.0D, 16.0D, 16.0D, 9.0D);
    protected static final VoxelShape SHAPE_Z = Block.box(7.0D, 0.0D, 0.0D, 9.0D, 16.0D, 16.0D);

    public StringyCobwebBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CONNECTED, ConnectedProperty.MIDDLE));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTED);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_)
    {
        switch((Direction)p_220053_1_.getValue(FACING))
        {
            case EAST:
            case WEST:
                return SHAPE_X;
            case NORTH:
            case SOUTH:
            default:
                return SHAPE_Z;
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess tickAccess, BlockPos currentPos, Direction facing, BlockPos facingPos, BlockState facingState, RandomSource randomSource)
    {
        return !state.canSurvive(level, currentPos) ? Blocks.AIR.defaultBlockState() : state;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        Direction direction = state.getValue(FACING);
        BlockPos abovePos = pos.above();
        BlockPos nextStringPos = pos.relative(direction).above();
        BlockPos prevStringPos = pos.relative(direction.getOpposite()).below();
        BlockPos belowPos = pos.below();

        BlockState aboveState = level.getBlockState(abovePos);
        BlockState nextStringState = level.getBlockState(nextStringPos);
        BlockState prevStringState = level.getBlockState(prevStringPos);
        BlockState belowState = level.getBlockState(belowPos);

        if ((aboveState.isFaceSturdy(level, abovePos, Direction.DOWN) || nextStringState.getBlock() == BOPBlocks.STRINGY_COBWEB) && (belowState.isFaceSturdy(level, belowPos, Direction.UP) || prevStringState.getBlock() == BOPBlocks.STRINGY_COBWEB))
            return true;

        return false;
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean p_394545_)
    {
        Direction direction = state.getValue(FACING);
        BlockPos abovePos = pos.relative(direction).above();
        BlockPos belowPos = pos.relative(direction.getOpposite()).below();
        BlockState aboveState = level.getBlockState(abovePos);
        BlockState belowState = level.getBlockState(belowPos);

        if (aboveState.getBlock() == BOPBlocks.STRINGY_COBWEB)
            level.destroyBlock(abovePos, false);

        if (belowState.getBlock() == BOPBlocks.STRINGY_COBWEB)
            level.destroyBlock(belowPos, false);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState blockstate = super.getStateForPlacement(context);
        Level LevelReader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction[] adirection = context.getNearestLookingDirections();

        for (Direction direction : adirection)
        {
            if (direction.getAxis().isHorizontal())
            {
                blockstate = blockstate.setValue(FACING, direction.getOpposite());
                if (blockstate.canSurvive(LevelReader, blockpos))
                {
                    return blockstate;
                }
            }
        }

        return null;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean b)
    {
        entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.75D, 1.0D, 0.75D));
    }
}
