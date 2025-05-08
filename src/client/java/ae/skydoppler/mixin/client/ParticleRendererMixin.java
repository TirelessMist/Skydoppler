package ae.skydoppler.mixin.client;

import ae.skydoppler.player_hiding.HideHubPlayersState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class ParticleRendererMixin {

    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        if (HideHubPlayersState.shouldHidePlayers() && client.player != null) {

            double distanceSq = client.player.squaredDistanceTo(x, y, z);

            if (distanceSq >= HideHubPlayersState.showRange * HideHubPlayersState.showRange) {

                cir.cancel();
            }

        }
    }


}
