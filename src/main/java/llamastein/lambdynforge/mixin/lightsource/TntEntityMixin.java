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
import llamastein.lambdynforge.ExplosiveLightingMode;
import llamastein.lambdynforge.LambDynLightsForge;
import llamastein.lambdynforge.api.DynamicLightHandlers;
import llamastein.lambdynforge.config.Config;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrimedTnt.class)
public abstract class TntEntityMixin extends Entity implements DynamicLightSource {
	@Shadow public abstract int getFuse();

	@Unique private int startFuseTimer = 80;
	@Unique	private int lambdynlights$luminance;

	public TntEntityMixin(EntityType<?> type, Level world) {
		super(type, world);
	}

	@Inject(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/level/Level;)V", at = @At("TAIL"))
	private void onNew(EntityType<? extends PrimedTnt> entityType, Level world, CallbackInfo ci) {
		this.startFuseTimer = this.getFuse();
	}

	@Inject(method = "tick", at = @At("TAIL"))
	private void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (this.level().isClientSide) {
			if (!Config.CLIENT.tntLightingMode.get().isEnabled())
				return;

			if (this.isRemoved()) {
				this.setDynamicLightEnabled(false);
			} else {
				if (!Config.CLIENT.allowEntitiesLightSource.get() || !DynamicLightHandlers.canLightUp(this))
					this.resetDynamicLight();
				else
					this.dynamicLightTick();
				LambDynLightsForge.updateTracking(this);
			}
		}
	}

	@Override
	public void dynamicLightTick() {
		if (this.isOnFire()) {
			this.lambdynlights$luminance = 15;
		} else {
			ExplosiveLightingMode lightingMode = Config.CLIENT.tntLightingMode.get();
			if (lightingMode == ExplosiveLightingMode.FANCY) {
				var fuse = this.getFuse() / this.startFuseTimer;
				this.lambdynlights$luminance = (int) (-(fuse * fuse) * 10.0) + 10;
			} else {
				this.lambdynlights$luminance = 10;
			}
		}
	}

	@Override
	public int getLuminance() {
		return this.lambdynlights$luminance;
	}
}
