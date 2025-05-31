package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.config.SkydopplerConfig;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {

    @Inject(method = "renderFirstPersonItem(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/" + "minecraft/util/Hand;FLnet/minecraft/item/ItemStack;FLnet/minecraft/client/util/math/" + "MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    public void onRenderHeldItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        SkydopplerConfig config = SkydopplerClient.CONFIG;
        if (hand == Hand.MAIN_HAND) {
            float rotX = config.heldItemRendererConfig.rotX;
            float rotY = config.heldItemRendererConfig.rotY;
            float rotZ = config.heldItemRendererConfig.rotZ;
            float posX = config.heldItemRendererConfig.posX;
            float posY = config.heldItemRendererConfig.posY;
            float posZ = config.heldItemRendererConfig.posZ;

            float scale = config.heldItemRendererConfig.scale;

            matrices.translate(posX, posY, posZ);

            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotX));
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotY));
            matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotZ));
            matrices.scale(scale, scale, scale);
        }
    }

    /*@Inject(method = "renderArm", at = @At("HEAD"))
    private void onRenderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm, CallbackInfo ci) {

        float rotX = 0;
        float rotY = 0;
        float rotZ = 0;
        float posX = 0;
        float posY = 0;
        float posZ = 0;

        float scale = 0.5f;
        matrices.translate(posX, posY, posZ);

        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(rotX));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(rotY));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotZ));
        matrices.scale(scale, scale, scale);

    }*/

    @ModifyExpressionValue(method = "updateHeldItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"))
    public float attackCooldown(float original) {
        SkydopplerConfig config = SkydopplerClient.CONFIG;
        if (config.heldItemRendererConfig.disableModernSwing) {
            return 1f;
        } else {
            return original;
        }
    }

    @Inject(method = "shouldSkipHandAnimationOnSwap", at = @At("HEAD"), cancellable = true)
    private void onShouldSkipHandAnimationOnSwap(ItemStack from, ItemStack _to, CallbackInfoReturnable<Boolean> cir) {
        if (SkydopplerClient.CONFIG.heldItemRendererConfig.disableSwapAnimation) {
            cir.setReturnValue(!(from.isOf(Items.AIR) || _to.isOf(Items.AIR)));
        }
    }

    @Inject(method = "applyEatOrDrinkTransformation", at = @At("HEAD"), cancellable = true)
    public void onDrink(MatrixStack matrices, float tickProgress, Arm arm, ItemStack stack, PlayerEntity player, CallbackInfo ci) {
        if (SkydopplerClient.CONFIG.heldItemRendererConfig.disableSwapAnimation) {
            ci.cancel();
        }
    }

}
