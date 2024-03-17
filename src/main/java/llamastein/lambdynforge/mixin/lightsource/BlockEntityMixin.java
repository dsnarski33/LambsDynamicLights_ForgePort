/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package llamastein.lambdynforge.mixin.lightsource;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import llamastein.lambdynforge.DynamicLightSource;
import llamastein.lambdynforge.LambDynLightsForge;
import llamastein.lambdynforge.api.DynamicLightHandlers;
import llamastein.lambdynforge.config.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements DynamicLightSource {
	@Final @Shadow protected BlockPos worldPosition;
	@Shadow @Nullable protected Level level;
	@Shadow protected boolean remove;

	@Unique private int luminance = 0;
	@Unique	private int lastLuminance = 0;
	@Unique	private long lastUpdate = 0;
	@Unique	private final LongOpenHashSet lambdynlights$trackedLitChunkPos = new LongOpenHashSet();

	@Override public double getDynamicLightX() { return this.worldPosition.getX() + 0.5; }
	@Override public double getDynamicLightY() { return this.worldPosition.getY() + 0.5; }
	@Override public double getDynamicLightZ() { return this.worldPosition.getZ() + 0.5; }
	@Override public Level getDynamicLightWorld() {	return this.level; }

	@Inject(method = "setRemoved", at = @At("TAIL"))
	private void onRemoved(CallbackInfo ci) { this.setDynamicLightEnabled(false); }

	@Override
	public void resetDynamicLight() { this.lastLuminance = 0; }

	@Override
	public void dynamicLightTick() {
		// We do not want to update the entity on the server.
		if (this.level == null || !this.level.isClientSide)
			return;
		if (!this.remove) {
			this.luminance = DynamicLightHandlers.getLuminanceFrom((BlockEntity)(Object) this);
			LambDynLightsForge.updateTracking(this);

			if (!this.isDynamicLightEnabled()) {
				this.lastLuminance = 0;
			}
		}
	}

	@Override
	public int getLuminance() {	return this.luminance; }

	@Override
	public boolean shouldUpdateDynamicLight() {
		int mode = Config.CLIENT.dynamicLightMode.get();
		if(mode < 0) return false;
		long currentTime = System.currentTimeMillis();
		if(currentTime < this.lastUpdate + mode)
			return false;
		this.lastUpdate = currentTime;
		return true;
	}

	@Override
	public boolean lambdynlights$updateDynamicLight(@NotNull LevelRenderer renderer) {
		if (!this.shouldUpdateDynamicLight())
			return false;

		int luminance = this.getLuminance();

		if (luminance != this.lastLuminance) {
			this.lastLuminance = luminance;

			if (this.lambdynlights$trackedLitChunkPos.isEmpty()) {
				var chunkPos = new BlockPos.MutableBlockPos(Mth.floorDiv(this.worldPosition.getX(), 16),
						Mth.floorDiv(this.worldPosition.getY(), 16),
						Mth.floorDiv(this.worldPosition.getZ(), 16));

				LambDynLightsForge.updateTrackedChunks(chunkPos, null, this.lambdynlights$trackedLitChunkPos);

				var directionX = (this.worldPosition.getX() & 15) >= 8 ? Direction.EAST : Direction.WEST;
				var directionY = (this.worldPosition.getY() & 15) >= 8 ? Direction.UP : Direction.DOWN;
				var directionZ = (this.worldPosition.getZ() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;

				for (int i = 0; i < 7; i++) {
					if (i % 4 == 0) {
						chunkPos.move(directionX); // X
					} else if (i % 4 == 1) {
						chunkPos.move(directionZ); // XZ
					} else if (i % 4 == 2) {
						chunkPos.move(directionX.getOpposite()); // Z
					} else {
						chunkPos.move(directionZ.getOpposite()); // origin
						chunkPos.move(directionY); // Y
					}
					LambDynLightsForge.updateTrackedChunks(chunkPos, null, this.lambdynlights$trackedLitChunkPos);
				}
			}

			// Schedules the rebuild of chunks.
			this.lambdynlights$scheduleTrackedChunksRebuild(renderer);
			return true;
		}
		return false;
	}

	@Override
	public void lambdynlights$scheduleTrackedChunksRebuild(@NotNull LevelRenderer renderer) {
		if (this.level == Minecraft.getInstance().level)
			for (long pos : this.lambdynlights$trackedLitChunkPos) {
				LambDynLightsForge.scheduleChunkRebuild(renderer, pos);
			}
	}
}
