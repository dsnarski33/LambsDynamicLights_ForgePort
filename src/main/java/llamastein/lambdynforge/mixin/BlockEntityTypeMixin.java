/*
 * Copyright © 2021 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package llamastein.lambdynforge.mixin;

import llamastein.lambdynforge.accessor.DynamicLightHandlerHolder;
import llamastein.lambdynforge.api.DynamicLightHandler;
import llamastein.lambdynforge.config.Config;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BlockEntityType.class)
public class BlockEntityTypeMixin<T extends BlockEntity> implements DynamicLightHandlerHolder<T> {
	@Unique private DynamicLightHandler<T> lambdynlights$lightHandler;
	@Unique	private Boolean lambdynlights_settingValue = null;
	@Unique	private String lambdynlights_settingKey = null;

	@Override
	public @Nullable DynamicLightHandler<T> lambdynlights$getDynamicLightHandler() {
		return this.lambdynlights$lightHandler;
	}

	@Override
	public void lambdynlights$setDynamicLightHandler(DynamicLightHandler<T> handler) {
		this.lambdynlights$lightHandler = handler;
	}

	@Override
	public Boolean lambdynlights$getSetting() {
		if (this.lambdynlights_settingKey == null) {
			var self = (BlockEntityType<?>) (Object) this;
			var id = ForgeRegistries.BLOCK_ENTITY_TYPES.getKey(self);
			if (id == null) {
				return null;
			}

			lambdynlights_settingKey = "light_sources.settings.block_entities."
					+ id.getNamespace() + '.' + id.getPath().replace('/', '.');
			lambdynlights_settingValue = Config.CLIENT.handleBlockEntity(lambdynlights_settingKey);
		}
		return lambdynlights_settingValue;
	}
}
