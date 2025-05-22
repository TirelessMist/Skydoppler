package ae.skydoppler.mixin.client;

import ae.skydoppler.behavior.AlwaysSprintState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow
    protected abstract boolean canSprint();

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void resprint(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        if (!AlwaysSprintState.shouldDoAlwaysSprint()) return;

        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;

        // If the forward key is held...
        if (client.options.forwardKey.isPressed()) {
            // ...and the player is not sprinting but now meets sprint conditions,
            // then re-enable sprinting.
            if (!player.isSprinting() && AlwaysSprintState.canSprint(player)) {
                player.setSprinting(true);
            }
        }
    }



}
