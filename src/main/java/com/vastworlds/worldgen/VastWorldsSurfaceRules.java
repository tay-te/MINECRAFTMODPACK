package com.vastworlds.worldgen;

import com.vastworlds.VastWorldsMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.SurfaceRuleManager;

/**
 * Registers surface rules with TerraBlender.
 * This ensures our custom surface rules are applied during world generation.
 */
public class VastWorldsSurfaceRules {

    /**
     * Registers the Vast Worlds surface rules with TerraBlender.
     * Call this during mod initialization.
     */
    public static void registerSurfaceRules() {
        SurfaceRuleManager.addSurfaceRules(
            SurfaceRuleManager.RuleCategory.OVERWORLD,
            VastWorldsMod.MOD_ID,
            VastWorldsSurfaceRuleData.makeRules()
        );

        VastWorldsMod.LOGGER.info("Registered Vast Worlds surface rules");
    }
}
