package ae.skydoppler.mixin.client;

import ae.skydoppler.behavior.AlwaysSprintState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {

    @Inject(method = "setPressed", at = @At("HEAD"))
    private void onMoveForward(boolean pressed, CallbackInfo ci) {

        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        if (!AlwaysSprintState.shouldDoAlwaysSprint()) return;

        // Yarn mapping: client.options.keyForward represents the forward key binding
        if ((Object) this == client.options.forwardKey && pressed && AlwaysSprintState.canSprint(client.player)) {
            // This code runs immediately when the forward key is pressed.
            if (!client.player.isSprinting()) {
                client.player.setSprinting(true);
            }
        }


    }


}
