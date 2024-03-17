/*
 * Sourced from TerraFirmaCraft reference, see https://github.com/TerraFirmaCraft/TerraFirmaCraft or https://www.curseforge.com/minecraft/mc-mods/terrafirmacraft
 *
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package llamastein.lambdynforge.common;

import net.minecraft.resources.ResourceLocation;

import static llamastein.lambdynforge.LambDynLightsForge.MOD_ID;

public class Helpers
{
    public static ResourceLocation identifier(String id)
    {
        return new ResourceLocation(MOD_ID, id);
    }

}
