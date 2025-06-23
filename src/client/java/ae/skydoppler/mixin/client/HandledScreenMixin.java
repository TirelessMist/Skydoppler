package ae.skydoppler.mixin.client;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.skyblock.SlotLockingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin<T extends ScreenHandler> extends Screen {

    @Unique
    private static final Identifier LOCK_TEXTURE = Identifier.of("skydoppler", "textures/slot-lock.png");

    @Shadow
    protected int x;

    @Shadow
    protected int y;

    @Shadow
    protected T handler;

    protected HandledScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected abstract Slot getSlotAt(double x, double y);

    // Renders the lock icon on locked slots
    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void renderSlotLock(DrawContext context, Slot slot, CallbackInfo ci) {
        // Skip rendering lock icons in storage UIs if configured to do so
        if (SlotLockingHelper.isStorageScreen(this)) {
            return;
        }

        if (slot.inventory == MinecraftClient.getInstance().player.getInventory()) {
            int slotIndex = slot.getIndex();
            if (slotIndex >= 0 && slotIndex < 36) {
                boolean[] lockedSlots = SlotLockingHelper.getLockedSlotsArray();
                if (lockedSlots[slotIndex]) {
                    // Draw the lock texture over the slot
                    context.drawTexture(RenderLayer::getGuiTexturedOverlay, LOCK_TEXTURE, slot.x, slot.y, 0, 0, 16, 16, 16, 16);
                }
            }
        }
    }

    // Handle the key press to toggle slot locking and prevent swapping with locked slots
    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        MinecraftClient client = MinecraftClient.getInstance();

        // If we're in a storage UI, completely bypass all slot locking functionality
        if (SlotLockingHelper.isStorageScreen(this)) {
            return;
        }

        // Always allow ESC key (GLFW_KEY_ESCAPE) and inventory key to work
        if (keyCode == GLFW.GLFW_KEY_ESCAPE ||
                (client.options.inventoryKey != null && client.options.inventoryKey.matchesKey(keyCode, scanCode))) {
            return;
        }

        double mouseX = client.mouse.getX() * (double) client.getWindow().getScaledWidth() / (double) client.getWindow().getWidth();
        double mouseY = client.mouse.getY() * (double) client.getWindow().getScaledHeight() / (double) client.getWindow().getHeight();
        Slot hoveredSlot = this.getSlotAt(mouseX, mouseY);

        // Always allow toggling locked state regardless of screen
        if (keyCode == SkydopplerClient.lockSlotKey && hoveredSlot != null &&
                hoveredSlot.inventory == client.player.getInventory()) {
            int slotIndex = hoveredSlot.getIndex();
            if (slotIndex >= 0 && slotIndex < 36) {
                SlotLockingHelper.toggleSlotLock(slotIndex);
                cir.setReturnValue(true);
                return;
            }
        }

        // Check if hovered slot is locked (and not toggling lock)
        if (hoveredSlot != null && hoveredSlot.inventory == client.player.getInventory()) {
            int slotIndex = hoveredSlot.getIndex();
            if (slotIndex >= 0 && slotIndex < 36) {
                if (SlotLockingHelper.isSlotLocked(slotIndex) && keyCode != SkydopplerClient.lockSlotKey) {
                    cir.setReturnValue(true); // Cancel any key press on locked slots
                    return;
                }
            }
        }

        // Check for hotbar key presses using the actual keybindings
        for (int i = 0; i < 9; i++) {
            KeyBinding hotbarBinding = client.options.hotbarKeys[i];
            if (hotbarBinding.matchesKey(keyCode, scanCode)) {
                // Check if the corresponding hotbar slot is locked
                if (SlotLockingHelper.isSlotLocked(i)) {
                    // Cancel the key press to prevent swapping with a locked hotbar slot
                    cir.setReturnValue(true);
                    return;
                }
                break; // Found the matching hotbar key, no need to check others
            }
        }
    }

    @Unique
    private void playLockedSlotSound() {
        if (SkydopplerClient.CONFIG.slotLockingToggleVolume > 0) {
            MinecraftClient.getInstance().player.playSoundToPlayer(
                    SoundEvents.BLOCK_NOTE_BLOCK_BASS.value(),
                    SoundCategory.MASTER,
                    SkydopplerClient.CONFIG.slotLockingToggleVolume,
                    0.8f
            );
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        // If we're in a storage UI, bypass all slot locking functionality
        if (SlotLockingHelper.isStorageScreen(this)) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        Slot hoveredSlot = this.getSlotAt(mouseX, mouseY);

        if (hoveredSlot != null && hoveredSlot.inventory == client.player.getInventory()) {
            int slotIndex = hoveredSlot.getIndex();
            if (slotIndex >= 0 && slotIndex < 36 && SlotLockingHelper.isSlotLocked(slotIndex)) {
                // Play error sound if trying to interact with locked slot
                playLockedSlotSound();
                // Prevent interaction with locked slots
                cir.setReturnValue(true);
                return;
            }
        }
    }

    // Prevent item pickup from locked slots
    @Inject(method = "onMouseClick(Lnet/minecraft/screen/slot/Slot;Lnet/minecraft/screen/slot/SlotActionType;)V", at = @At("HEAD"), cancellable = true)
    private void onSlotClick(Slot slot, SlotActionType actionType, CallbackInfo ci) {
        // If we're in a storage UI, bypass all slot locking functionality
        if (SlotLockingHelper.isStorageScreen(this)) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();

        if (slot != null && slot.inventory == client.player.getInventory()) {
            int slotIndex = slot.getIndex();
            if (slotIndex >= 0 && slotIndex < 36 && SlotLockingHelper.isSlotLocked(slotIndex)) {
                // Play error sound if trying to interact with locked slot
                playLockedSlotSound();
                // Prevent interaction with locked slots
                ci.cancel();
            }
        }
    }
}
