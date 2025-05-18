package ae.skydoppler.mixin.client;

import ae.skydoppler.dungeon.map.DungeonMapHandler;
import ae.skydoppler.dungeon.room_detection.MapParser;
import ae.skydoppler.dungeon.room_detection.MapReassembler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.MapRenderState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(MapState.class)
public class MapStateMixin {

    @Inject(method = "update", at = @At("TAIL"))
    private void onUpdate(PlayerEntity player, ItemStack stack, CallbackInfo ci) {

        MapState mapState = FilledMapItem.getMapState(stack, MinecraftClient.getInstance().world);
        // Update the map overlay
        byte[][] mapPixels = MapParser.parseMap(mapState);
        if (mapPixels == null) {
            System.out.println("Map pixels are null.");
            return;
        }
        MapReassembler.Tile[][] mapTiles = MapReassembler.reassembleMap(mapPixels);
        if (mapTiles == null) {
            System.out.println("Map tiles are null.");
            return;
        }
        DungeonMapHandler.updateMap(mapTiles);

    }

}
