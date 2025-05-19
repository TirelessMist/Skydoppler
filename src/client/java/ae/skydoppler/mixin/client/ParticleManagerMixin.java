package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.player_hiding.HideHubPlayersState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        if (HideHubPlayersState.shouldHidePlayers() && client.player != null) {

            double distanceSq = client.player.squaredDistanceTo(x, y, z);

            int d = SkydopplerClient.CONFIG.hideFarPlayersModeDistance;

            if (distanceSq >= d * d) {

                cir.setReturnValue(null); // cancel the particle if it is outside the range
            }

        } else if (parameters.getType() == ParticleTypes.EXPLOSION && SkydopplerClient.hideExplosionParticle) {

            cir.setReturnValue(null); // cancel the particle if it is an explosion particle

        } else if (parameters.getType() == ParticleTypes.FIREWORK && client.player != null) {
        double dx = x - client.player.getX();
        double dy = y - (client.player.getY() + client.player.getEyeHeight(client.player.getPose()));
        double dz = z - client.player.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance > 0) {
            // Normalize the direction vector to the particle
            double dirX = dx / distance;
            double dirY = dy / distance;
            double dirZ = dz / distance;

            // Get the player's look vector
            double lookX = client.player.getRotationVec(1.0F).x;
            double lookY = client.player.getRotationVec(1.0F).y;
            double lookZ = client.player.getRotationVec(1.0F).z;

            // Calculate the dot product and clamp it to avoid precision errors
            double dotProduct = Math.max(-1.0, Math.min(1.0, dirX * lookX + dirY * lookY + dirZ * lookZ));
            double angle = Math.acos(dotProduct) * (180.0 / Math.PI);

            // Check if the angle is within a couple of degrees
            if (angle <= 2.5) {
                cir.setReturnValue(null); // cancel the particle if the player is aiming at it
            }
        }
    }
    }


}
