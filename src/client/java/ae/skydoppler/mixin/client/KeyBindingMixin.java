package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.api.BlockingAccessor;
import ae.skydoppler.behavior.AlwaysSprintState;
import ae.skydoppler.config.SkydopplerConfig;
import ae.skydoppler.config.chat_matcher_config.ChatMatchConfigScreen;
import ae.skydoppler.skyblock.SlotLockingHelper;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public abstract class KeyBindingMixin {

    @Inject(method = "setPressed", at = @At("HEAD"))
    private void onSetPressed(boolean pressed, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        KeyBinding thisBinding = (KeyBinding) (Object) this;
        BlockingAccessor playerAccessor = (BlockingAccessor) client.player;
        boolean isBlocking = playerAccessor.skydoppler$isBlocking();

        // Handle sword blocking
        if (thisBinding == client.options.useKey &&
                SkydopplerClient.CONFIG.oldVersionParityConfig.doSwordBlocking &&
                client.player.getMainHandStack().getItem().getTranslationKey().contains("sword")) {
            // If the player is using a sword and the use key is pressed/released, toggle blocking state
            playerAccessor.skydoppler$setBlocking(pressed);
            return;
        }

        // Handle always sprint
        if (thisBinding == client.options.forwardKey && pressed &&
                !AlwaysSprintState.shouldNotDoAlwaysSprint() &&
                AlwaysSprintState.canSprint(client.player) &&
                !client.player.isSprinting()) {
            client.player.setSprinting(true);
            return;
        }

        // Handle debug key
        if (thisBinding == SkydopplerClient.debugKey && SkydopplerClient.debugModeEnabled && pressed) {
            // Add debug functionality here if needed
            System.out.println("Debug key pressed!");
            client.setScreen(ChatMatchConfigScreen.buildConfigScreen(SkydopplerClient.CONFIG, null));
            return;
        }
    }

    @ModifyReturnValue(method = "wasPressed", at = @At("RETURN"))
    private boolean onWasPressed(boolean original) {
        // If the original result is false, no need to check anything else
        if (!original) return false;

        KeyBinding thisBinding = (KeyBinding) (Object) this;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return original;

        // Handle dropping items with locked slots
        if (thisBinding == client.options.dropKey &&
                SkydopplerClient.CONFIG.doSlotLocking &&
                SlotLockingHelper.isSlotLocked(client.player.getInventory().getSelectedSlot())) {
            SlotLockingHelper.playLockedSlotSound(true);
            return false;
        }

        return original;
    }
}
