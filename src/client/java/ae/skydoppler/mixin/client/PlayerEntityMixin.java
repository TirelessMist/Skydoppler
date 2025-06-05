package ae.skydoppler.mixin.client;

import ae.skydoppler.api.BlockingAccessor;
import ae.skydoppler.old_version_parity.OneEightModeHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    @Unique
    private boolean isPlayerBlocking() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return false;
        return ((BlockingAccessor) player).skydoppler$isBlocking();
    }

    @Inject(method = "attack" + "(Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void onAttack(Entity target, CallbackInfo ci) {
        // Prevent attacking if the player is blocking
        if (isPlayerBlocking()) {
            ci.cancel();
        }
    }

}
