package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.old_version_parity.OneEightModeHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    // Modify the hand swing duration by the multiplier
    @ModifyExpressionValue(method = "getHandSwingDuration()I", at = @At(value = "CONSTANT", args = "intValue=6")) // 6 is the default swing duration for players
    private int modifyHandSwingDuration(int original) {
        return Math.max(1, (int) (original / SkydopplerClient.CONFIG.heldItemRendererConfig.swingSpeedMultiplier));
    }

    // 1.8 Mode: Override crouch (sneak) dimensions for correct height
    @Inject(method = "getDimensions", at = @At("HEAD"), cancellable = true)
    private void modifyCrouchDimensions(net.minecraft.entity.EntityPose pose, CallbackInfoReturnable<net.minecraft.entity.EntityDimensions> cir) {
        if ((Object)this instanceof PlayerEntity && pose == net.minecraft.entity.EntityPose.CROUCHING && SkydopplerClient.CONFIG.oldVersionParityConfig.doOldCrouchHeight) {
            // Avoid recursion: get width from STANDING pose
            float width = ((LivingEntity)(Object)this).getDimensions(net.minecraft.entity.EntityPose.STANDING).width();
            float height = OneEightModeHelper.getCrouchHeight();
            cir.setReturnValue(net.minecraft.entity.EntityDimensions.changing(width, height));
        }
    }
}
