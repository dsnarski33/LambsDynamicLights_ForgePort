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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T extends Entity> implements DynamicLightHandlerHolder<T> {

	@Unique	private DynamicLightHandler<T> lambdynlights$lightHandler;
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
			var self = (EntityType<?>) (Object) this;
			var id = ForgeRegistries.ENTITY_TYPES.getKey(self);
			if (id.getNamespace().equals("minecraft") && id.getPath().equals("pig") && self != EntityType.PIG) {
				return null;
			}

			lambdynlights_settingKey = "light_sources.settings.entities."
					+ id.getNamespace() + '.' + id.getPath().replace('/', '.');
			lambdynlights_settingValue = Config.CLIENT.handleEntity(lambdynlights_settingKey);
		}
		return this.lambdynlights_settingValue;
	}
}
