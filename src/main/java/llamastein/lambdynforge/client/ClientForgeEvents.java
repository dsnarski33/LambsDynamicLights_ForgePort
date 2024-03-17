/*
 * Sourced from TerraFirmaCraft reference, see https://github.com/TerraFirmaCraft/TerraFirmaCraft or https://www.curseforge.com/minecraft/mc-mods/terrafirmacraft
 *
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package llamastein.lambdynforge.client;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;

public class ClientForgeEvents
{
    public static void init()
    {
        final IEventBus bus = MinecraftForge.EVENT_BUS;

        //bus.addListener(TFCExpClientForgeEvents::onTooltip);
    }

//    private static void onTooltip(ItemTooltipEvent event)
//    {
//    }
}
