/*
 * Sourced from TerraFirmaCraft reference, see https://github.com/TerraFirmaCraft/TerraFirmaCraft or https://www.curseforge.com/minecraft/mc-mods/terrafirmacraft
 *
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package llamastein.lambdynforge.config;

import llamastein.lambdynforge.ExplosiveLightingMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.*;
import java.util.function.Function;

import static llamastein.lambdynforge.LambDynLightsForge.MOD_ID;

/**
 * Client Config
 * - not synced, only loaded client side
 * - only use for PURELY AESTHETIC options
 */
public class ClientConfig
{
    public final ForgeConfigSpec.IntValue dynamicLightMode;
    public final ForgeConfigSpec.BooleanValue allowEntitiesLightSource, allowBlockEntitiesLightSource, allowSelfLightSource, allowWaterSensitiveCheck;
    public final ForgeConfigSpec.EnumValue<ExplosiveLightingMode> tntLightingMode, creeperLightingMode;
    private final ForgeConfigSpec.ConfigValue<List<? extends String>> entitiesList, blockEntitiesList;

    private final Set<String> entities = new HashSet<>(), blockEntities = new HashSet<>();

    ClientConfig(ConfigBuilder builder)
    {
        dynamicLightMode = builder.comment("Dynamic Lights update frequency, 0 will disable it").define("dynamicLightMode", 50, 0, 500);
        allowEntitiesLightSource = builder.comment("Enables entities dynamic lighting. [Note: Players are always active]").define("allowEntitiesLightSource", true);
        allowBlockEntitiesLightSource = builder.comment("Enables block entities dynamic lighting").define("allowBlockEntitiesLightSource", true);
        allowSelfLightSource = builder.comment("Enables first-person dynamic lighting. It's recommended to disable this if you're using a shader that has their own dynamic lighting").define("allowSelfLightSource", true);
        allowWaterSensitiveCheck = builder.comment("Enables the water-sensitive light sources check. This means that some items will not emit light while being submerged in water").define("allowWaterSensitiveCheck", true);
        tntLightingMode = builder.comment("Sets the TNT dynamic lighting mode: " + ExplosiveLightingMode.getDesc()).define("tntLightingMode", ExplosiveLightingMode.SIMPLE);
        creeperLightingMode = builder.comment("Sets the Creeper dynamic lighting mode: " + ExplosiveLightingMode.getDesc()).define("creeperLightingMode", ExplosiveLightingMode.SIMPLE);
        entitiesList = builder.comment("List of entities that is populated by the mod. Should not have to modify directly").define("entitiesList", new ArrayList<>(), s -> true); //todo validate!
        blockEntitiesList = builder.comment("List of block entities that is populated by the mod. Should not have to modify directly").define("blockEntitiesList", new ArrayList<>(), s -> true); //todo validate!
    }

    public void init() {
        entities.addAll(entitiesList.get());
        blockEntities.addAll(blockEntitiesList.get());
    }

    public boolean handleBlockEntity(String id) {
        if(blockEntities.contains(id))
            return true;
        if(true) { // todo: should this be a config entry, is there one? to determine the initial true/false of this entry?
            blockEntities.add(id);
            // noinspection unchecked -> update config with value found
            ((List<String>)blockEntitiesList.get()).add(id);
            blockEntitiesList.save();
            return true;
        }
        return false;
    }

    public boolean handleEntity(String id) {
        if(entities.contains(id))
            return true;
        if(true) { // todo: should this be a config entry, is there one? to determine the initial true/false of this entry?
            entities.add(id);
            // noinspection unchecked -> update config with value found
            ((List<String>)entitiesList.get()).add(id);
            entitiesList.save();
            return true;
        }
        return false;
    }

    public boolean isDynamicLightsEnabled() {
        return dynamicLightMode.get() >= 0;
    }
}