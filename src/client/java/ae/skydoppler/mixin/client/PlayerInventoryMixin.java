package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.api.BlockingAccessor;
import ae.skydoppler.skyblock.SlotLockingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInventory.class)
public class PlayerInventoryMixin {

    @Inject(method = "setStack", at = @At("HEAD"))
    private void onSetStack(int slot, ItemStack stack, CallbackInfo ci) {


    }

    @Inject(method = "setSelectedSlot", at = @At("HEAD"))
    private void onSetSelectedSlot(int slot, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null || client.player == null) return;

        ItemStack stack = client.player.getInventory().getStack(slot);
        boolean isSword = stack.getItem().getTranslationKey().contains("sword");
        boolean isUsingItem = client.options.useKey.isPressed();

        BlockingAccessor playerAccessor = (BlockingAccessor)client.player;
        boolean isBlocking = playerAccessor.skydoppler$isBlocking();

        if (!isSword && isBlocking) {
            playerAccessor.skydoppler$setBlocking(false);
        } else if (isSword && !isBlocking && isUsingItem) {
            playerAccessor.skydoppler$setBlocking(true);
        }
    }

}
