package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.skyblock_locations.SkyblockLocationEnum;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {

    @Inject(method = "disconnect", at = @At("HEAD"))
    private void onDisconnect(CallbackInfo ci) {
        if (SkydopplerClient.debugModeEnabled)
            System.out.println("OOOOOOOOOOOOOOOO====---- [ClientWorldMixin] Disconnecting from the world ----OOOOOOOOOOOOOOOO");
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(ClientPlayNetworkHandler networkHandler, ClientWorld.Properties properties, RegistryKey registryRef, RegistryEntry dimensionType, int loadDistance, int simulationDistance, WorldRenderer worldRenderer, boolean debugWorld, long seed, int seaLevel, CallbackInfo ci) {
        if (SkydopplerClient.debugModeEnabled)
            System.out.println("OOOOOOOOOOOOOOOO====---- [ClientWorldMixin] Initializing world ----OOOOOOOOOOOOOOOO");

        if (SkydopplerClient.isPlayingSkyblock && SkydopplerClient.CONFIG.doTransferCooldownFinishedAlert) {
            SkydopplerClient.startIslandWarpTimer();

            SkyblockLocationEnum currentIsland = SkyblockLocationEnum.NONE;
            Enum<?> currentZone = SkyblockLocationEnum.NONE.getZonesForIsland()[0];
            Enum<?> currentRegion = null;
        }


    }
}
