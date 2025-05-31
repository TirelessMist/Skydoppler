package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    // Modify the hand swing duration by the multiplier
    @ModifyExpressionValue(
        method = "getHandSwingDuration()I",
        at = @At(value = "CONSTANT", args = "intValue=6") // 6 is the default swing duration for players
    )
    private int modifyHandSwingDuration(int original) {
        return Math.max(1, (int)(original / SkydopplerClient.CONFIG.heldItemRendererConfig.swingSpeedMultiplier));
    }
}

