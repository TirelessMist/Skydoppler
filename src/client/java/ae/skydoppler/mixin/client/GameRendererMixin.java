package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getNightVisionStrength", at = @At("HEAD"), cancellable = true)
    private static void onGetNightVisionStrength(LivingEntity entity, float tickProgress, CallbackInfoReturnable<Float> cir) {

        if (SkydopplerClient.CONFIG.hideNightVisionEffect) {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "renderHand", at = @At("HEAD"))
    private void onRenderHand(Camera camera, float tickProgress, Matrix4f positionMatrix, CallbackInfo ci) {

        // Example transformation values (replace with your own logic or config)
        float handOffsetX = 0.2f;
        float handOffsetY = -0.1f;
        float handOffsetZ = -0.3f;
        float handPitch = 45.0f; // degrees
        float handYaw = 15.0f;   // degrees
        float handRoll = -50.0f;   // degrees
        float handScale = 0.5f;

        // Apply transformations to the hand's positionMatrix
        positionMatrix.translate(handOffsetX, handOffsetY, handOffsetZ);
        positionMatrix.rotateXYZ(
                (float) Math.toRadians(handPitch),
                (float) Math.toRadians(handYaw),
                (float) Math.toRadians(handRoll)
        );
        positionMatrix.scale(handScale);
    }

}
