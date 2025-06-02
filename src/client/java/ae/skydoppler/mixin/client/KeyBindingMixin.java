package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.behavior.AlwaysSprintState;
import ae.skydoppler.util.BlockingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

    @Inject(method = "setPressed", at = @At("HEAD"), cancellable = true)
    private void onSetPressed(boolean pressed, CallbackInfo ci) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        if ((Object) this == client.options.useKey && SkydopplerClient.CONFIG.do1_8Mode) {
            if (SkydopplerClient.debugModeEnabled)
                System.out.println("Blocking: " + pressed);
            BlockingHelper.setBlocking(pressed);
        }

        // If the player is trying to attack or sprint while blocking (and 1.8 mode is enabled), cancel the action
        if (((Object) this == client.options.attackKey || (Object) this == client.options.sprintKey) && SkydopplerClient.CONFIG.do1_8Mode && BlockingHelper.isBlocking) {
            ci.cancel();
            return;
        }

        // Yarn mapping: client.options.keyForward represents the forward key binding
        if ((Object) this == client.options.forwardKey && pressed && AlwaysSprintState.canSprint(client.player)) {
            // This code runs immediately when the forward key is pressed.

            if (BlockingHelper.isBlocking) return; // If blocking, do not sprint

            if (AlwaysSprintState.shouldNotDoAlwaysSprint()) return;

            if (!client.player.isSprinting()) {
                client.player.setSprinting(true);
            }
        }


    }


}
