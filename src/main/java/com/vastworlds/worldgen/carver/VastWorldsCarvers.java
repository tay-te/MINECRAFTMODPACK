package com.vastworlds.worldgen.carver;

import com.vastworlds.VastWorldsMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.carver.CanyonCarverConfiguration;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class VastWorldsCarvers {
    public static final DeferredRegister<WorldCarver<?>> CARVERS =
            DeferredRegister.create(Registries.CARVER, VastWorldsMod.MOD_ID);

    public static final DeferredHolder<WorldCarver<?>, VastCanyonCarver> VAST_CANYON =
            CARVERS.register("vast_canyon", () -> new VastCanyonCarver(CanyonCarverConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        CARVERS.register(eventBus);
    }
}
