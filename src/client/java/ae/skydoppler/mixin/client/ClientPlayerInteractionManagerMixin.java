package ae.skydoppler.mixin.client;

import ae.skydoppler.api.BlockingAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin {

    @Shadow public abstract void cancelBlockBreaking();

    @Unique
    private boolean isPlayerBlocking() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (player == null) return false;
        return ((BlockingAccessor)player).skydoppler$isBlocking();
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At("HEAD"), cancellable = true)
    private void onUpdateBlockBreakingProgress(net.minecraft.util.math.BlockPos pos, net.minecraft.util.math.Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (isPlayerBlocking()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isCurrentlyBreaking", at = @At("HEAD"), cancellable = true)
    private void onIsCurrentlyBreaking(net.minecraft.util.math.BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (isPlayerBlocking()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "isBreakingBlock", at = @At("HEAD"), cancellable = true)
    private void onIsBreakingBlock(CallbackInfoReturnable<Boolean> cir) {
        if (isPlayerBlocking()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBreak(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/player/PlayerEntity;)Lnet/minecraft/block/BlockState;"), cancellable = true)
    private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (isPlayerBlocking()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "attackBlock", at = @At("HEAD"), cancellable = true)
    private void onAttackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (isPlayerBlocking()) {
            cir.setReturnValue(false);
        }
    }
}