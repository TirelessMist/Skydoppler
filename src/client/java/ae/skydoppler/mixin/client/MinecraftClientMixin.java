package ae.skydoppler.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static ae.skydoppler.Skydoppler.LOGGER;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {


    // Shadow the currentScreen field so we can examine what the previous screen was
    @Shadow
    @Nullable
    public Screen currentScreen;

    /**
     * Inject into the setScreen method, which is called whenever a new screen
     * is set (including opening or closing the inventory).
     *
     * The signature here is: void setScreen(@Nullable Screen newScreen)
     */
    @Inject(method = "setScreen", at = @At("HEAD"))
    private void onSetScreen(@Nullable Screen newScreen, CallbackInfo ci) {
        // If the new screen is an InventoryScreen, the inventory is being opened.
        if (newScreen instanceof InventoryScreen) {
            LOGGER.info("Inventory opened.");
        }
        // If the new screen is null and the previously active screen was an InventoryScreen,
        // then the inventory is being closed.
        else if (newScreen == null) {
            LOGGER.info("Inventory closed.");
        }
    }


}
