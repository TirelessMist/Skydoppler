package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {

    @ModifyExpressionValue(method = "update", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F", ordinal = 1))
    private float doFullbright(float original) {
        if (SkydopplerClient.CONFIG.doFullbright)
            return 1500;
        return original;
    }

}
