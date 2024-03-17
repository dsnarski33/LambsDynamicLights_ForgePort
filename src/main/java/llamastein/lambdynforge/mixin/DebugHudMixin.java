///*
// * Copyright © 2020 LambdAurora <email@lambdaurora.dev>
// *
// * This file is part of LambDynamicLights.
// *
// * Licensed under the MIT license. For more information,
// * see the LICENSE file.
// */
//
//package llamastein.lambdynforge.mixin;
//
//import llamastein.lambdynforge.LambDynLightsForge;
//import llamastein.lambdynforge.config.Config;
//import net.minecraft.client.gui.components.DebugScreenOverlay;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//
//import java.util.List;
//
///**
// * Adds a debug string for dynamic light sources tracking and updates.
// *
// * @author LambdAurora
// * @version 1.3.2
// * @since 1.3.2
// */
//@Mixin(DebugScreenOverlay.class)
//public class DebugHudMixin {
//	@Inject(method = "getGameInformation", at = @At("RETURN"))
//	private void onGetLeftText(CallbackInfoReturnable<List<String>> cir) {
//		var list = cir.getReturnValue();
//		var ldl = LambDynLightsForge.INSTANCE;
//		var builder = new StringBuilder("Dynamic Light Sources: ");
//		builder.append(ldl.getLightSourcesCount())
//				.append(" (U: ")
//				.append(ldl.getLastUpdateCount());
//
//		if (!Config.CLIENT.isDynamicLightsEnabled()) {
//			builder.append(" ; ");
//			builder.append("Formatting.RED");
//			builder.append("Disabled");
//			builder.append("Formatting.RESET");
//		}
//
//		builder.append(')');
//		list.add(builder.toString());
//	}
//}
