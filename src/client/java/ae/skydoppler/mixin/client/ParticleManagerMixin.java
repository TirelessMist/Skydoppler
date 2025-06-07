package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.player_hiding.PlayerHidingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
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
            at = @At(value = "HEAD"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {

        // Ensure parameters and particle type are not null
        if (parameters == null || parameters.getType() == null || client.player == null) {
            return; // Skip processing if any critical value is null
        }

        ParticleType<?> particleType = parameters.getType();

        // Handle far player hiding
        if (SkydopplerClient.CONFIG.doFarPlayerHiding && PlayerHidingHelper.shouldDoHubHiding()) {
            double distanceSq = client.player.squaredDistanceTo(x, y, z);
            int maxDistance = SkydopplerClient.CONFIG.hideFarPlayersRange;

            if (distanceSq >= maxDistance * maxDistance) {
                cir.setReturnValue(null); // Cancel particles outside the distance
                return;
            }
        }

        // Handle explosion particles
        if (particleType == ParticleTypes.EXPLOSION && SkydopplerClient.CONFIG.hideExplosionParticle) {
            cir.setReturnValue(null); // Cancel explosion particles
            return;
        }

        // Handle firework particles
        if (particleType == ParticleTypes.FIREWORK && SkydopplerClient.CONFIG.hideMageBeams) {
            if (isPlayerAimingAtParticle(x, y, z)) {
                cir.setReturnValue(null); // Cancel firework particles if the player is aiming at them
            }
        }
    }

    @Unique
    private boolean isPlayerAimingAtParticle(double x, double y, double z) {

        if (client.player == null) return false;

        // Player eye position
        double px = client.player.getX();
        double py = client.player.getEyeY();
        double pz = client.player.getZ();

        // Player look vector (normalized)
        double lookX = client.player.getRotationVec(1.0F).x;
        double lookY = client.player.getRotationVec(1.0F).y;
        double lookZ = client.player.getRotationVec(1.0F).z;

        // Vector from player to particle
        double dx = x - px;
        double dy = y - py;
        double dz = z - pz;

        // Project particle vector onto look vector
        double dot = dx * lookX + dy * lookY + dz * lookZ;
        if (dot < -10 || dot > 50) return false; // Behind 10 blocks and within 50 blocks in front

        // Closest point on aim line to particle
        double closestX = px + lookX * dot;
        double closestY = py + lookY * dot;
        double closestZ = pz + lookZ * dot;

        // Distance from particle to aim line
        double distSq = (x - closestX) * (x - closestX) + (y - closestY) * (y - closestY) + (z - closestZ) * (z - closestZ);

        return distSq <= 49.0; // 7 blocks radius squared
    }
}