/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package llamastein.lambdynforge.mixin.lightsource;

import llamastein.lambdynforge.DynamicLightSource;
import llamastein.lambdynforge.LambDynLightsForge;
import llamastein.lambdynforge.api.DynamicLightHandlers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Player.class)
public abstract class PlayerEntityMixin extends LivingEntity implements DynamicLightSource {
	@Shadow
	public abstract boolean isSpectator();

	@Unique
	protected int lambdynlights$luminance;
	@Unique
	private Level lambdynlights$lastWorld;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public void dynamicLightTick() {
		if (!DynamicLightHandlers.canLightUp(this)) {
			this.lambdynlights$luminance = 0;
			return;
		}

		if (this.isOnFire() || this.isCurrentlyGlowing()) {
			this.lambdynlights$luminance = 15;
		} else {
			this.lambdynlights$luminance = Math.max(
					DynamicLightHandlers.getLuminanceFrom(this),
					LambDynLightsForge.getLivingEntityLuminanceFromItems(this)
			);
		}

		if (this.isSpectator())
			this.lambdynlights$luminance = 0;

		if (this.lambdynlights$lastWorld != this.level()) {
			this.lambdynlights$lastWorld = this.level();
			this.lambdynlights$luminance = 0;
		}
	}

	@Override
	public int getLuminance() {
		return this.lambdynlights$luminance;
	}
}
