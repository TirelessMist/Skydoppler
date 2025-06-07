package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.player_hiding.PlayerHidingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin<E extends Entity> {

    @Unique
    private static final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void onRender(E entity, double x, double y, double z, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (client.world == null || client.player == null) return;

        double distSq = entity.squaredDistanceTo(client.player);

        if (PlayerHidingHelper.isPlayerAnNpc(entity)) {
            PlayerHidingHelper.npcPositions.add(entity.getPos());
        }

        if (SkydopplerClient.CONFIG.glowingDroppedItems && entity instanceof ItemEntity) {
            entity.setGlowing(true);
        }
        if (SkydopplerClient.CONFIG.glowingPlayers && entity instanceof PlayerEntity && !PlayerHidingHelper.isPlayerAnNpc(entity)) {
            entity.setGlowing(true);
        }

        if (SkydopplerClient.isRodCast) {
            if (entity instanceof PlayerEntity player
                    && !player.equals(client.player)
                    && !PlayerHidingHelper.isPlayerAnNpc(player)
                    && distSq <= SkydopplerClient.CONFIG.hidePlayersWhileFishingRange * SkydopplerClient.CONFIG.hidePlayersWhileFishingRange) {
                ci.cancel();
                return;
            }
            if (entity instanceof FishingBobberEntity bobber
                    && SkydopplerClient.CONFIG.hideOtherFishingRods
                    && bobber.getOwner() != null
                    && !bobber.getOwner().equals(client.player)) {
                ci.cancel();
                return;
            }
        }

        if (PlayerHidingHelper.shouldDoHubHiding()
                && !PlayerHidingHelper.isPlayerAnNpc(entity)
                && distSq >= SkydopplerClient.CONFIG.hideFarPlayersRange * SkydopplerClient.CONFIG.hideFarPlayersRange) {
            ci.cancel();
            return;
        }

        if (SkydopplerClient.CONFIG.hidePlayersNearNpc
                && entity instanceof PlayerEntity
                && !entity.equals(client.player)
                && !PlayerHidingHelper.isPlayerAnNpc(entity)) {
            float rangeSq = SkydopplerClient.CONFIG.hidePlayersNearNpcRange * SkydopplerClient.CONFIG.hidePlayersNearNpcRange;
            if (PlayerHidingHelper.npcPositions.stream().anyMatch(pos -> pos.squaredDistanceTo(entity.getPos()) <= rangeSq)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private void onRenderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, EntityRenderState renderState, Quaternionf rotation, CallbackInfo ci) {
        if (SkydopplerClient.CONFIG.hideThirdPersonFireOverlay) ci.cancel();
    }
}
