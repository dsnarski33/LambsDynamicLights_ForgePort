/*
 * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
 *
 * This file is part of LambDynamicLights.
 *
 * Licensed under the MIT license. For more information,
 * see the LICENSE file.
 */

package llamastein.lambdynforge.mixin;

import llamastein.lambdynforge.LambDynLightsForge;
import llamastein.lambdynforge.accessor.WorldRendererAccessor;
import llamastein.lambdynforge.config.Config;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LevelRenderer.class, priority = 900)
public abstract class CommonWorldRendererMixin implements WorldRendererAccessor {
	@Invoker("setSectionDirty") @Override
	public abstract void lambdynlights$scheduleChunkRebuild(int x, int y, int z, boolean important);

	@Inject(method = "getLightColor(Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I", at = @At("TAIL"), cancellable = true)
	private static void onGetLightmapCoordinates(BlockAndTintGetter pLevel, BlockState pState, BlockPos pPos, CallbackInfoReturnable<Integer> cir) {
		if (!pLevel.getBlockState(pPos).isSolidRender(pLevel, pPos) && Config.CLIENT.isDynamicLightsEnabled())
			cir.setReturnValue(LambDynLightsForge.INSTANCE.getLightmapWithDynamicLight(pPos, cir.getReturnValue()));
	}
}
