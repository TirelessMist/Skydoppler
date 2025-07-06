package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void onRenderArmor(DrawContext context, PlayerEntity player, int i, int j, int k, int x, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHideHungerArmorBubbles)
            ci.cancel();
    }

    @Inject(method = "renderStatusEffectOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderStatusEffectOverlay(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHideStatusEffectOverlay)
            ci.cancel();
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void onRenderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHideHungerArmorBubbles)
            ci.cancel();
    }

    @Inject(method = "renderAirBubbles", at = @At("HEAD"), cancellable = true)
    private void onRenderAirBubbles(DrawContext context, PlayerEntity player, int heartCount, int top, int left, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHideHungerArmorBubbles)
            ci.cancel();
    }

    @Inject(method = "renderHeldItemTooltip", at = @At("HEAD"), cancellable = true)
    private void onRenderHeldItemTooltip(DrawContext context, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHideHeldItemTooltip)
            ci.cancel();
    }

    @Inject(method = "renderHealthBar", at = @At("HEAD"), cancellable = true)
    private void onRenderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHideHealthBar)
            ci.cancel();
    }

    @Inject(method = "renderMountHealth", at = @At("HEAD"), cancellable = true)
    private void onRenderMountHealth(DrawContext context, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHideMountHealth)
            ci.cancel();
    }

    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void onRenderPortalOverlay(DrawContext context, float tickDelta, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHidePortalOverlay)
            ci.cancel();
    }

}
