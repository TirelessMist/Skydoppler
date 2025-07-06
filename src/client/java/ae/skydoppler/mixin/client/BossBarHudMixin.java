package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class BossBarHudMixin {

    @Inject(method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V", at = @At("HEAD"), cancellable = true)
    private void onRenderBossBar(DrawContext context, int x, int y, BossBar bossBar, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.mainConfig.general.visualSettings.vanillaHudConfig.shouldHideWitherBossbarsInSkyblock && bossBar.shouldDarkenSky()) {
            bossBar.setName(Text.empty());
            ci.cancel();
        }

    }


}
