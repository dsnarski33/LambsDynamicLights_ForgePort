package llamastein.lambdynforge;

import llamastein.lambdynforge.api.item.ItemLightSources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.Item;
import llamastein.lambdynforge.api.item.ItemLightSource;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Consumer;

public class ItemLightSourcesReloadListener extends SimplePreparableReloadListener<Map<Item, ItemLightSource>>
{
    private final String resourcePath;
    private final Consumer<Map<Item, ItemLightSource>> consumer;

    public ItemLightSourcesReloadListener(Consumer<Map<Item, ItemLightSource>> consumer, String resourcePath)
    {
        this.resourcePath = resourcePath;
        this.consumer = consumer;
    }

    @Override
    protected @NotNull Map<Item, ItemLightSource> prepare(@NotNull ResourceManager resourceManagerIn, @NotNull ProfilerFiller profilerIn)
    {
        return ItemLightSources.load(resourceManagerIn, resourcePath);
    }

    @Override
    protected void apply(@NotNull Map<Item, ItemLightSource> objectIn, @NotNull ResourceManager resourceManagerIn, @NotNull ProfilerFiller profilerIn)
    {
        consumer.accept(objectIn);
    }
}
