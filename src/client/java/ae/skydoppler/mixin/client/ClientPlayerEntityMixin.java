package ae.skydoppler.mixin.client;

import ae.skydoppler.behavior.AlwaysSprintState;
import ae.skydoppler.util.BlockingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @Shadow
    protected abstract boolean canSprint();

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void resprint(CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null || AlwaysSprintState.shouldNotDoAlwaysSprint()) return;

        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        if (client.options.forwardKey.isPressed() && !player.isSprinting() && AlwaysSprintState.canSprint(player)) {
            player.setSprinting(true);
        }
    }

    // Prevent sprinting by double-tapping the forward key when blocking
    @Inject(method = "canSprint", at = @At("HEAD"), cancellable = true)
    private void onCanSprint(CallbackInfoReturnable<Boolean> cir) {
        if (BlockingHelper.isBlocking)
            cir.setReturnValue(false);
    }


}
