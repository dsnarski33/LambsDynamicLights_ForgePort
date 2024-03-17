/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package llamastein.lambdynforge.api.item;

import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import llamastein.lambdynforge.LambDynLightsForge;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Represents an item light sources manager.
 *
 * @author LambdAurora
 * @version 2.3.2
 * @since 1.3.0
 */
public final class ItemLightSources {
	private static /*final*/ Map<Item, ItemLightSource> ITEM_LIGHT_SOURCES = new Reference2ObjectOpenHashMap<>();
	private static final Map<Item, ItemLightSource> STATIC_ITEM_LIGHT_SOURCES = new Reference2ObjectOpenHashMap<>();

	private ItemLightSources() {
		throw new UnsupportedOperationException("ItemLightSources only contains static definitions.");
	}

	/**
	 * Loads the item light source data from resource pack.
	 *
	 * @param resourceManager The resource manager.
	 */
	public static Map<Item, ItemLightSource> load(ResourceManager resourceManager, String location) {
		Reference2ObjectOpenHashMap<Item, ItemLightSource> sources = new Reference2ObjectOpenHashMap<>();
		resourceManager.listResources(location, s -> s.getPath().endsWith(".json")).forEach((resourceLocation, resource) -> load(sources, resourceLocation, resource));
		sources.putAll(STATIC_ITEM_LIGHT_SOURCES);
		return sources;
	}


	private static void load(Map<Item, ItemLightSource> itemLightSources, ResourceLocation resourceId, Resource resource) {
		var id = new ResourceLocation(resourceId.getNamespace(), resourceId.getPath().replace(".json", ""));
		try (var reader = new InputStreamReader(resource.open())) {
			var json = JsonParser.parseReader(reader).getAsJsonObject();

			ItemLightSource.fromJson(id, json).ifPresent(data -> {
				if (!STATIC_ITEM_LIGHT_SOURCES.containsKey(data.item()))
					register(itemLightSources, data);
			});
		} catch (IOException | IllegalStateException e) {
			LambDynLightsForge.LOGGER.warn("Failed to load item light source \"" + id + "\".");
		}
	}

	public static void applyItemLightSources(Map<Item, ItemLightSource> itemLightSources) {
		ITEM_LIGHT_SOURCES = itemLightSources;
	}

	/**
	 * Registers an item light source data.
	 *
	 * @param data The item light source data.
	 */
	private static void register(Map<Item, ItemLightSource> itemLightSources, ItemLightSource data) {
		ItemLightSource source = itemLightSources.get(data.item());
		if(source != null) {
			LambDynLightsForge.LOGGER.warn("Failed to register item light source \"" + data.id() + "\", duplicates item \""
					+ ForgeRegistries.ITEMS.getKey(data.item()) + "\" found in \"" + source.id() + "\".");
			return;
		}
		itemLightSources.put(data.item(), data);
	}

	/**
	 * Registers an item light source data.
	 *
	 * @param data the item light source data
	 */
	public static void registerItemLightSource(ItemLightSource data) {
		ItemLightSource source = STATIC_ITEM_LIGHT_SOURCES.get(data.item());
		if (source != null) {
			LambDynLightsForge.LOGGER.warn("Failed to register item light source \"" + data.id() + "\", duplicates item \""
					+ ForgeRegistries.ITEMS.getKey(data.item()) + "\" found in \"" + source.id() + "\".");
			return;
		}
		STATIC_ITEM_LIGHT_SOURCES.put(data.item(), data);
	}

	/**
	 * Returns the luminance of the item in the stack.
	 *
	 * @param stack the item stack
	 * @param submergedInWater {@code true} if the stack is submerged in water, else {@code false}
	 * @return a luminance value
	 */
	public static int getLuminance(ItemStack stack, boolean submergedInWater) {
		var data = ITEM_LIGHT_SOURCES.get(stack.getItem());
		if (data != null) {
			return data.getLuminance(stack, submergedInWater);
		} else if (stack.getItem() instanceof BlockItem blockItem)
			return ItemLightSource.BlockItemLightSource.getLuminance(stack, blockItem.getBlock().defaultBlockState());
		else return 0;
	}
}
