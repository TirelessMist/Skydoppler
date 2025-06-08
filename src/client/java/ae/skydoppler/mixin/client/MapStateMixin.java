package ae.skydoppler.mixin.client;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapState.class)
public class MapStateMixin {

    @Inject(method = "update", at = @At("TAIL"))
    private void onUpdate(PlayerEntity player, ItemStack stack, CallbackInfo ci) {

        /*MapState mapState = FilledMapItem.getMapState(stack, MinecraftClient.getInstance().world);
        // Update the map overlay
        byte[][] mapPixels = MapParser.parseMap(mapState);
        if (mapPixels == null) {
            System.out.println("Map pixels are null.");
            return;
        }
        if (mapTiles == null) {
            System.out.println("Map tiles are null.");
            return;
        }
        DungeonMapHandler.updateMap(mapTiles);*/

    }

}
