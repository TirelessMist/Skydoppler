package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.player_hiding.HideHubPlayersState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

    @Unique
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
        at = @At("HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        
        if (client.player == null || parameters.getType() == null || parameters.getType() == null) {
            return; // Skip if the player or particle type is null
        }

        // Handle far player hiding
        if (SkydopplerClient.CONFIG.doFarPlayerHiding && HideHubPlayersState.shouldDoHubHiding()) {
            double distanceSq = client.player.squaredDistanceTo(x, y, z);
            int maxDistance = SkydopplerClient.CONFIG.hideFarPlayersRange;

            if (distanceSq >= maxDistance * maxDistance) {
                cir.setReturnValue(null); // Cancel particles outside the range
                return;
            }
        }

        /*// Handle explosion particles
        if (parameters.getType() == ParticleTypes.EXPLOSION && SkydopplerClient.CONFIG.hideExplosionParticle) {
            cir.setReturnValue(null); // Cancel explosion particles
            return;
        }

        // Handle firework particles
        if (SkydopplerClient.CONFIG.hideMageBeams && parameters.getType() == ParticleTypes.FIREWORK) {
            if (isPlayerAimingAtParticle(x, y, z)) {
                cir.setReturnValue(null); // Cancel firework particles if the player is aiming at them
            }
        }*/
    }

    @Unique
    private boolean isPlayerAimingAtParticle(double x, double y, double z) {
        double dx = x - client.player.getX();
        double dy = y - (client.player.getY() + client.player.getEyeHeight(client.player.getPose()));
        double dz = z - client.player.getZ();
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (distance == 0) {
            return false; // Avoid division by zero
        }

        // Normalize direction vector
        double dirX = dx / distance;
        double dirY = dy / distance;
        double dirZ = dz / distance;

        // Get player's look vector
        var lookVec = client.player.getRotationVec(1.0F);
        double dotProduct = dirX * lookVec.x + dirY * lookVec.y + dirZ * lookVec.z;

        // Calculate angle and check if it's within 2.5 degrees
        double angle = Math.acos(Math.max(-1.0, Math.min(1.0, dotProduct))) * (180.0 / Math.PI);
        return angle <= 2.5;
    }
}