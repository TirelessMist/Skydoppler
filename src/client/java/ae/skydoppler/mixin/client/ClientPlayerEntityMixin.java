package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.api.BlockingAccessor;
import ae.skydoppler.behavior.AlwaysSprintState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements BlockingAccessor {

    @Shadow
    @Final
    protected MinecraftClient client;
    @Unique
    private boolean isBlocking;

    @Shadow
    protected abstract boolean canSprint();

    @Inject(method = "tickMovement",
            at = @At("HEAD"))
    private void resprint(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || AlwaysSprintState.shouldNotDoAlwaysSprint()) return;

        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (client.options.forwardKey.isPressed() && !player.isSprinting() && AlwaysSprintState.canSprint(player) && !isBlocking) {
            player.setSprinting(true);
        }
    }

    // Prevent sprinting by double-tapping the forward key while blocking
    @Inject(method = "canSprint",
            at = @At("HEAD"), cancellable = true)
    private void onCanSprint(CallbackInfoReturnable<Boolean> cir) {
        if (isBlocking)
            cir.setReturnValue(false);
    }

    @Inject(method = "swingHand",
            at = @At("HEAD"), cancellable = true)
    private void onSwingHand(Hand hand, CallbackInfo ci) {
        if (isBlocking) {
            if (!SkydopplerClient.CONFIG.oldVersionParityConfig.do1_7Animations) {
                ci.cancel();
            }
        }
    }

    @Override
    public boolean skydoppler$isBlocking() {
        return this.isBlocking;
    }

    @Override
    public void skydoppler$setBlocking(boolean blocking) {
        this.isBlocking = blocking;
        if (blocking) {
            if (client.player != null) {
                // Stop sprinting if the player is blocking
                client.player.setSprinting(false);

                ClientPlayerInteractionManager interactionManager = client.interactionManager;
                if (interactionManager != null) {
                    // Force reset current breaking
                    interactionManager.cancelBlockBreaking();

                    // Reset attack cooldown
                    client.player.resetLastAttackedTicks();

                    // Cancel the current attack sequence
                    if (client.options.attackKey.isPressed()) {
                        interactionManager.cancelBlockBreaking();
                    }
                }
            }
        }
    }
}
