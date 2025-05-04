package ae.skydoppler.mixin.client;

import ae.skydoppler.HideHubPlayersState;
import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.fishing.FishingHideState;
import ae.skydoppler.item.DroppedItemGlowingState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin<E extends Entity> {

    private static MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void onRender(E entity, double x, double y, double z, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {

        if (client.world == null || client.player == null) {
            return;
        }

        // fishing hider functionality
        if (FishingHideState.rodCastActive) {

            if (entity instanceof PlayerEntity player) {

                if (!player.equals(client.player)) {

                    double distanceSq = player.squaredDistanceTo(client.player);

                    if (distanceSq <= FishingHideState.hideRange * FishingHideState.hideRange) {

                        ci.cancel();
                        return;
                    }
                }
            }

            if (entity instanceof FishingBobberEntity bobber) {

                if (bobber.getOwner() != null && !bobber.getOwner().equals(client.player)) {

                    double distanceSq = bobber.squaredDistanceTo(client.player);

                    if (distanceSq <= FishingHideState.hideRange * FishingHideState.hideRange) {

                        ci.cancel();
                        return;
                    }
                }
            }

        }

        if (HideHubPlayersState.hidePlayersInListOfLocations.contains(SkydopplerClient.playerDataStruct.getSkyblockLocation())) {
            // if the player is in a location where the players should be hidden

            double distanceSq = entity.squaredDistanceTo(client.player);

            if (distanceSq >= HideHubPlayersState.showRange * HideHubPlayersState.showRange) {

                ci.cancel();
                return;
            }

        }

        if (DroppedItemGlowingState.glowing && entity instanceof ItemEntity item) { // glowing dropped items. the item's glow color is set by Hypixel to the rarity color of the item, so we don't need to decide the color.

            item.setGlowing(true);

        }
    }
}