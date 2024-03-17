/*
 * Sourced from TerraFirmaCraft reference, see https://github.com/TerraFirmaCraft/TerraFirmaCraft or https://www.curseforge.com/minecraft/mc-mods/terrafirmacraft
 *
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package llamastein.lambdynforge.common;

import llamastein.lambdynforge.LambDynLightsForge;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class ForgeEvents
{
    public static void init()
    {

        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ForgeEvents::onRenderLevelStageEvent);
        bus.addListener(ForgeEvents::onLogin);
    }

    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) { }

    public static void onRenderLevelStageEvent(RenderLevelStageEvent event) {
        if(event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            Minecraft.getInstance().getProfiler().popPush("dynamic_lighting");
            LambDynLightsForge.INSTANCE.updateAll(event.getLevelRenderer());
        }
    }
}
