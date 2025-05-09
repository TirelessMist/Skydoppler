package ae.skydoppler.mixin.client;

import ae.skydoppler.fishing.FishingHideState;
import ae.skydoppler.item.DroppedItemGlowingState;
import ae.skydoppler.player_hiding.HideHubPlayersState;
import ae.skydoppler.player_hiding.HidePlayerNearNpc;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.stream.StreamSupport;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin<E extends Entity> {

    @Unique
    private static final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "render(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void onRender(E entity, double x, double y, double z, float tickProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (client.world == null || client.player == null) return;

        if (FishingHideState.rodCastActive && shouldHideFishingEntity(entity)) {
            ci.cancel();
            return;
        }

        if (HideHubPlayersState.shouldHidePlayers() && shouldHideHubPlayer(entity)) {
            ci.cancel();
            return;
        }

        if (HidePlayerNearNpc.hidePlayers && entity instanceof PlayerEntity player && !player.equals(client.player) && isEntityNearNpc(player)) {

            if (!HidePlayerNearNpc.isPlayerAnNpc(player)) { // if the player is not an NPC, cancel the render.

                ci.cancel();
                return;

            }

        }

        if (DroppedItemGlowingState.glowing && entity instanceof ItemEntity item) {
            item.setGlowing(true);
        }
    }

    @Unique
    private boolean shouldHideFishingEntity(E entity) {
        return (entity instanceof PlayerEntity player && !player.equals(client.player)
                && player.squaredDistanceTo(client.player) <= FishingHideState.hideRange * FishingHideState.hideRange)
                || (entity instanceof FishingBobberEntity bobber && bobber.getOwner() != null
                && !bobber.getOwner().equals(client.player)
                && bobber.squaredDistanceTo(client.player) <= FishingHideState.hideRange * FishingHideState.hideRange);
    }

    @Unique
    private boolean shouldHideHubPlayer(E entity) {
        return entity.squaredDistanceTo(client.player) >= HideHubPlayersState.showRange * HideHubPlayersState.showRange;
    }

    @Unique
    private boolean isEntityNearNpc(PlayerEntity entity) {
        return StreamSupport.stream(client.world.getEntities().spliterator(), false)
                .filter(e -> e instanceof PlayerEntity && HidePlayerNearNpc.isPlayerAnNpc((PlayerEntity) e))
                .anyMatch(e -> entity.squaredDistanceTo((PlayerEntity) e) <= HidePlayerNearNpc.hideRange * HidePlayerNearNpc.hideRange);
    }
}