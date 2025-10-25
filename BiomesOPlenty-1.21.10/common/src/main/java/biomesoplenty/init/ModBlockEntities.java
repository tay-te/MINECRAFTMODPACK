/*******************************************************************************
 * Copyright 2024, the Glitchfiend Team.
 * All rights reserved.
 ******************************************************************************/
package biomesoplenty.init;

import biomesoplenty.api.block.BOPBlockEntities;
import biomesoplenty.block.HangingSignBlockEntityBOP;
import biomesoplenty.block.entity.AnomalyBlockEntity;
import biomesoplenty.block.entity.SignBlockEntityBOP;
import biomesoplenty.core.BiomesOPlenty;
import glitchcore.util.BlockHelper;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;
import java.util.function.BiConsumer;

import static biomesoplenty.api.block.BOPBlocks.*;

public class ModBlockEntities
{
    public static void registerBlockEntities(BiConsumer<ResourceLocation, BlockEntityType<?>> func)
    {
        BOPBlockEntities.SIGN = register(func, "sign", SignBlockEntityBOP::new, Set.of(
                ORIGIN_OAK_SIGN, FIR_SIGN, PINE_SIGN, MAPLE_SIGN, REDWOOD_SIGN, MAHOGANY_SIGN, JACARANDA_SIGN, PALM_SIGN, WILLOW_SIGN, DEAD_SIGN, MAGIC_SIGN, UMBRAN_SIGN, HELLBARK_SIGN, EMPYREAL_SIGN,
                        ORIGIN_OAK_WALL_SIGN, FIR_WALL_SIGN, PINE_WALL_SIGN, MAPLE_WALL_SIGN, REDWOOD_WALL_SIGN, MAHOGANY_WALL_SIGN, JACARANDA_WALL_SIGN, PALM_WALL_SIGN, WILLOW_WALL_SIGN, DEAD_WALL_SIGN, MAGIC_WALL_SIGN, UMBRAN_WALL_SIGN, HELLBARK_WALL_SIGN, EMPYREAL_WALL_SIGN));

        BOPBlockEntities.HANGING_SIGN = register(func, "hanging_sign", HangingSignBlockEntityBOP::new, Set.of(
                ORIGIN_OAK_HANGING_SIGN, FIR_HANGING_SIGN, PINE_HANGING_SIGN, MAPLE_HANGING_SIGN, REDWOOD_HANGING_SIGN, MAHOGANY_HANGING_SIGN, JACARANDA_HANGING_SIGN, PALM_HANGING_SIGN, WILLOW_HANGING_SIGN, DEAD_HANGING_SIGN, MAGIC_HANGING_SIGN, UMBRAN_HANGING_SIGN, HELLBARK_HANGING_SIGN, EMPYREAL_HANGING_SIGN,
                        ORIGIN_OAK_WALL_HANGING_SIGN, FIR_WALL_HANGING_SIGN, PINE_WALL_HANGING_SIGN, MAPLE_WALL_HANGING_SIGN, REDWOOD_WALL_HANGING_SIGN, MAHOGANY_WALL_HANGING_SIGN, JACARANDA_WALL_HANGING_SIGN, PALM_WALL_HANGING_SIGN, WILLOW_WALL_HANGING_SIGN, DEAD_WALL_HANGING_SIGN, MAGIC_WALL_HANGING_SIGN, UMBRAN_WALL_HANGING_SIGN, HELLBARK_WALL_HANGING_SIGN, EMPYREAL_WALL_HANGING_SIGN));

        BOPBlockEntities.ANOMALY = register(func, "anomaly", AnomalyBlockEntity::new, Set.of(ANOMALY));

        BlockHelper.addBlockEntityBlocks(BlockEntityType.SHELF, ORIGIN_OAK_SHELF, FIR_SHELF, PINE_SHELF, MAPLE_SHELF, REDWOOD_SHELF, MAHOGANY_SHELF, JACARANDA_SHELF, PALM_SHELF, WILLOW_SHELF, DEAD_SHELF, MAGIC_SHELF, UMBRAN_SHELF, HELLBARK_SHELF, EMPYREAL_SHELF);
    }

    private static <T extends BlockEntity> BlockEntityType<?> register(BiConsumer<ResourceLocation, BlockEntityType<?>> func, String name, BlockEntityType.BlockEntitySupplier<T> supplier, Set<Block> blocks)
    {
        var type = new BlockEntityType(supplier, blocks);
        func.accept(ResourceLocation.fromNamespaceAndPath(BiomesOPlenty.MOD_ID, name), type);
        return type;
    }
}
