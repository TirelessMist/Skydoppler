package ae.skydoppler.mixin.client;

import ae.skydoppler.player_hiding.HideHubPlayersState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntitySpawnMixin {

    @Unique
    MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "onSpawnPacket", at = @At("HEAD"), cancellable = true)
    private void onSpawnPacket(EntitySpawnS2CPacket packet, CallbackInfo ci) {

        Entity entity = (Entity) (Object) this;
        if (HideHubPlayersState.shouldHidePlayers()) hidePlayers(entity, ci);

    }

    @Unique
    private void hidePlayers(Entity entity, CallbackInfo ci) {
        double distanceSq = entity.squaredDistanceTo(client.player);

        if (distanceSq >= HideHubPlayersState.showRange * HideHubPlayersState.showRange) {

            ci.cancel();
            return;
        }
    }

}
