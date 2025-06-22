package ae.skydoppler.mixin.client;

import ae.skydoppler.api.BlockingAccessor;
import ae.skydoppler.behavior.AlwaysSprintState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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

    }

    @Override
    public boolean skydoppler$isBlocking() {
        return this.isBlocking;
    }

    @Override
    public void skydoppler$setBlocking(boolean blocking) {
        this.isBlocking = blocking;
    }
}
