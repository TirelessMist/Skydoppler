package ae.skydoppler.mixin.client;

import ae.skydoppler.world_rendering.WorldRenderingState;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void onApplyFog(Camera camera, BackgroundRenderer.FogType fogType, Vector4f color, float viewDistance, boolean thickenFog, float tickProgress, CallbackInfoReturnable<Fog> cir) {

        if (fogType != BackgroundRenderer.FogType.FOG_SKY && WorldRenderingState.shouldHideFog)
            cir.setReturnValue(Fog.DUMMY);

    }


}
