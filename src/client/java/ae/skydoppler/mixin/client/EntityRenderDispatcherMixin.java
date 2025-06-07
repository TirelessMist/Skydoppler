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

    @Unique
    private double squaredDistanceToPlayer;

    @Unique
    private static double distanceToPlayer(Entity entity) {
        return entity.squaredDistanceTo(client.player);
    }

    @Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void onRender(E entity, double x, double y, double z, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        if (client.world == null || client.player == null) return;

        squaredDistanceToPlayer = distanceToPlayer(entity);

        // If the entity is an NPC, add it to the list of NPCs inside PlayerHidingHelper.
        if (PlayerHidingHelper.isPlayerAnNpc(entity)) {
            PlayerHidingHelper.npcPositions.add(entity.getPos());
        }


        //region Glowing
        if (SkydopplerClient.CONFIG.glowingDroppedItems && entity instanceof ItemEntity item) {
            item.setGlowing(true);
        }

        if (SkydopplerClient.CONFIG.glowingPlayers && entity instanceof PlayerEntity && !PlayerHidingHelper.isPlayerAnNpc(entity)) {
            entity.setGlowing(true);
        }
        //endregion

        //region Hiding
        if (SkydopplerClient.isRodCast && shouldHideFishingPlayer(entity)) {
            ci.cancel();
            return;
        }

        if (SkydopplerClient.isRodCast && shouldHideFishingRod(entity)) {
            ci.cancel();
            return;
        }

        if (PlayerHidingHelper.shouldDoHubHiding() && shouldHideHubPlayer(entity)) {
            ci.cancel();
            return;
        }

        if (SkydopplerClient.CONFIG.hidePlayersNearNpc && shouldHidePlayerNearNpc(entity)) {
            ci.cancel();
            return;
        }
        //endregion

    }

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private void onRenderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers, EntityRenderState renderState, Quaternionf rotation, CallbackInfo ci) {

        if (SkydopplerClient.CONFIG.hideThirdPersonFireOverlay) {
            ci.cancel();
        }
    }

    //region Hiding Helper Methods
    @Unique
    private boolean shouldHideFishingPlayer(E entity) {
        int range = SkydopplerClient.CONFIG.hidePlayersWhileFishingRange;
        int rangeSquared = range * range;

        return (entity instanceof PlayerEntity player // check if the entity is a player
                && !player.equals(client.player) // ensure it's not the local player
                && !PlayerHidingHelper.isPlayerAnNpc(player) // check if the player is not an NPC
                && squaredDistanceToPlayer <= rangeSquared); // check if the player is within the specified range
    }

    @Unique
    private boolean shouldHideFishingRod(E entity) {
        return entity instanceof FishingBobberEntity bobber && SkydopplerClient.CONFIG.hideOtherFishingRods
                && bobber.getOwner() != null && !bobber.getOwner().equals(client.player);
    }

    @Unique
    private boolean shouldHideHubPlayer(E entity) {
        int d = SkydopplerClient.CONFIG.hideFarPlayersRange;
        return !PlayerHidingHelper.isPlayerAnNpc(entity) && squaredDistanceToPlayer >= d * d;
    }

    @Unique
    private boolean shouldHidePlayerNearNpc(E entity) {
        if (!(entity instanceof PlayerEntity) || entity.equals(client.player) || PlayerHidingHelper.isPlayerAnNpc(entity)) {
            return false;
        }
        float rangeSquared = SkydopplerClient.CONFIG.hidePlayersNearNpcRange * SkydopplerClient.CONFIG.hidePlayersNearNpcRange;
        return PlayerHidingHelper.npcPositions.stream()
                .anyMatch(pos -> pos.squaredDistanceTo(entity.getPos()) <= rangeSquared);
    }
    //endregion

}