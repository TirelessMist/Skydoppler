package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.api.BlockingAccessor;
import ae.skydoppler.behavior.AlwaysSprintState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {

    @Shadow public abstract void setPressed(boolean pressed);

    @Inject(method = "setPressed", at = @At("HEAD"), cancellable = true)
    private void onSetPressed(boolean pressed, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        KeyBinding thisBinding = (KeyBinding) (Object) this;
        BlockingAccessor playerAccessor = (BlockingAccessor)client.player;
        boolean isBlocking = playerAccessor.skydoppler$isBlocking();

        // Handle sword blocking
        if (thisBinding == client.options.useKey &&
                SkydopplerClient.CONFIG.oldVersionParityConfig.doSwordBlocking &&
                client.player.getMainHandStack().getItem().getTranslationKey().contains("sword")) {
            // If the player is using a sword and the use key is pressed/released, toggle blocking state
            playerAccessor.skydoppler$setBlocking(pressed);
        }

        // Prevent sprinting while blocking
        if (thisBinding == client.options.sprintKey && isBlocking) {
            ci.cancel();
            return;
        }

        // Handle always sprint
        if (thisBinding == client.options.forwardKey && pressed &&
                !isBlocking &&
                !AlwaysSprintState.shouldNotDoAlwaysSprint() &&
                AlwaysSprintState.canSprint(client.player) &&
                !client.player.isSprinting()) {
            client.player.setSprinting(true);
        }
    }
}
