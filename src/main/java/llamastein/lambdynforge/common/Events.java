/*
 * Sourced from TerraFirmaCraft reference, see https://github.com/TerraFirmaCraft/TerraFirmaCraft or https://www.curseforge.com/minecraft/mc-mods/terrafirmacraft
 *
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package llamastein.lambdynforge.common;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;

import java.io.IOException;
import java.nio.file.Path;

import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import llamastein.lambdynforge.LambDynLightsForge;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.NotNull;

public class Events
{
    public static void init()
    {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(Events::onPackFinder);
    }

    public static void onPackFinder(AddPackFindersEvent event)
    {
        try
        {
            if (event.getPackType() == PackType.CLIENT_RESOURCES)
            {
                final IModFile modFile = ModList.get().getModFileById(LambDynLightsForge.MOD_ID).getFile();
                final Path resourcePath = modFile.getFilePath();
                try (PathPackResources pack = new PathPackResources(modFile.getFileName() + ":overload", true, resourcePath){

                    private final IModFile file = ModList.get().getModFileById(LambDynLightsForge.MOD_ID).getFile();

                    @NotNull
                    @Override
                    protected Path resolve(String @NotNull ... paths)
                    {
                        return file.findResource(paths);
                    }
                })
                {
                    final PackMetadataSection metadata = pack.getMetadataSection(PackMetadataSection.TYPE);
                    if (metadata != null)
                    {
                        LambDynLightsForge.LOGGER.info("Injecting {} override pack", LambDynLightsForge.MOD_ID);
                        event.addRepositorySource(consumer ->
                            consumer.accept(Pack.readMetaAndCreate(LambDynLightsForge.MOD_ID + "_data", Component.literal("lambdynforge Resources"), true, id -> pack, PackType.CLIENT_RESOURCES, Pack.Position.TOP, PackSource.BUILT_IN))
                        );
                    }
                }

            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
