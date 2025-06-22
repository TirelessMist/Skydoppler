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
public abstract class EntityRenderDispatcherMixin {

    @Unique
    private static final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true)
    private <E extends Entity> void onRender(E entity, double x, double y, double z, float tickProgress,
                                             MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                             int light, CallbackInfo ci) {
        if (entity == null || client.world == null || client.player == null) return;

        // Handle NPC detection
        if (entity instanceof PlayerEntity && PlayerHidingHelper.isPlayerAnNpc(entity)) {
            PlayerHidingHelper.npcPositions.add(entity.getPos());
        }

        // Apply glowing effects
        applyGlowingEffects(entity);

        // Check if entity should be hidden
        if (shouldHideEntity(entity)) {
            ci.cancel();
        }
    }

    @Unique
    private <E extends Entity> void applyGlowingEffects(E entity) {
        if (entity instanceof ItemEntity && SkydopplerClient.CONFIG.glowingDroppedItems) {
            entity.setGlowing(true);
        } else if (entity instanceof PlayerEntity player
                && SkydopplerClient.CONFIG.glowingPlayers
                && !PlayerHidingHelper.isPlayerAnNpc(player)) {
            entity.setGlowing(true);
        }
    }

    @Unique
    private <E extends Entity> boolean shouldHideEntity(E entity) {
        if (client.player == null) return false;

        double distSq = entity.squaredDistanceTo(client.player);
        boolean isFishing = client.player.fishHook != null;

        // Fishing-related entity hiding
        if (isFishing) {
            // Hide other players while fishing
            if (entity instanceof PlayerEntity player
                    && SkydopplerClient.CONFIG.hidePlayersWhileFishing
                    && !player.equals(client.player)
                    && !PlayerHidingHelper.isPlayerAnNpc(player)) {
                float rangeSq = SkydopplerClient.CONFIG.hidePlayersWhileFishingRange *
                        SkydopplerClient.CONFIG.hidePlayersWhileFishingRange;
                if (distSq <= rangeSq) {
                    return true;
                }
            }

            // Hide other fishing rods
            if (entity instanceof FishingBobberEntity bobber
                    && SkydopplerClient.CONFIG.hideOtherFishingRods
                    && bobber.getOwner() != null
                    && !bobber.getOwner().equals(client.player)) {
                return true;
            }
        }

        // Hub player hiding for distant players
        if (PlayerHidingHelper.shouldDoHubHiding()
                && !PlayerHidingHelper.isPlayerAnNpc(entity)) {
            float hideFarRange = SkydopplerClient.CONFIG.hideFarPlayersRange;
            if (distSq >= hideFarRange * hideFarRange) {
                return true;
            }
        }

        // Hide players near NPCs
        if (entity instanceof PlayerEntity
                && SkydopplerClient.CONFIG.hidePlayersNearNpc
                && !entity.equals(client.player)
                && !PlayerHidingHelper.isPlayerAnNpc(entity)) {
            float rangeSq = SkydopplerClient.CONFIG.hidePlayersNearNpcRange *
                    SkydopplerClient.CONFIG.hidePlayersNearNpcRange;

            // Use anyMatch for better performance than stream().anyMatch()
            for (var npcPos : PlayerHidingHelper.npcPositions) {
                if (npcPos.squaredDistanceTo(entity.getPos()) <= rangeSq) {
                    return true;
                }
            }
        }

        return false;
    }

    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private void onRenderFire(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                              EntityRenderState renderState, Quaternionf rotation, CallbackInfo ci) {
        if (SkydopplerClient.CONFIG.hideThirdPersonFireOverlay) {
            ci.cancel();
        }
    }
}