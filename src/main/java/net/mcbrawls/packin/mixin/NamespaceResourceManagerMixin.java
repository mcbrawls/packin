package net.mcbrawls.packin.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resource.NamespaceResourceManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NamespaceResourceManager.class)
public abstract class NamespaceResourceManagerMixin {
	// Enables PackinResourceLoader to suck up mcmeta files. Will break if data packs ever use mcmeta, help.
	@Inject(method = "isMcmeta", at = @At("HEAD"), cancellable = true)
	private static void onMcmetaPut(Identifier id, CallbackInfoReturnable<Boolean> cir) {
		EnvType type = FabricLoader.getInstance().getEnvironmentType();
		if (type == EnvType.SERVER) {
			cir.setReturnValue(false);
		}
	}
}
