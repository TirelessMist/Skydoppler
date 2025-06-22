package ae.skydoppler.skyblock;

import ae.skydoppler.SkydopplerClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.ArrayList;
import java.util.List;

public class SlotLockingHelper {

    private static boolean[] lockedSlotsArray = new boolean[36];
    private static List<Integer> lockedSlots = new ArrayList<>();

    /**
     * Checks if a screen is a storage UI where slot locking should be disabled
     *
     * @param screen The screen to check
     * @return true if the screen is a storage UI and doSlotLockingInStorageUi is true
     */
    public static boolean isStorageScreen(Screen screen) {
        if (!SkydopplerClient.CONFIG.doSlotLockingInStorageUi) {
            return false;
        }

        if (screen == null) {
            return false;
        }

        String title = screen.getTitle().getString().toLowerCase();
        return title.contains("ender chest") ||
                title.contains("backpack") ||
                title.equals("storage") ||
                title.equals("chest");
    }

    public static boolean isSlotLocked(int slot) {
        if (slot < 0 || slot >= lockedSlotsArray.length) return false;
        return lockedSlotsArray[slot];
    }

    public static void toggleSlotLock(int slot) {
        if (slot < 0 || slot >= lockedSlotsArray.length) return;

        if (lockedSlots == null) {
            lockedSlots = new ArrayList<>();
        }

        if (lockedSlotsArray[slot]) {
            lockedSlots.remove(Integer.valueOf(slot));
            lockedSlotsArray[slot] = false;
        } else {
            lockedSlots.add(slot);
            lockedSlotsArray[slot] = true;
        }

        // Play sound effect if the config allows it
        if (SkydopplerClient.CONFIG.doSlotLocking && SkydopplerClient.CONFIG.slotLockingToggleVolume > 0) {
            MinecraftClient.getInstance().player.playSoundToPlayer(SoundEvents.BLOCK_LEVER_CLICK, SoundCategory.MASTER, SkydopplerClient.CONFIG.slotLockingToggleVolume, 1.2f);
        }
    }

    public static List<Integer> getLockedSlots() {
        if (lockedSlots == null) {
            lockedSlots = new ArrayList<>();
        }
        return lockedSlots;
    }

    public static void setLockedSlots(List<Integer> lockedSlots) {
        SlotLockingHelper.lockedSlots = lockedSlots;
        if (lockedSlotsArray == null) {
            lockedSlotsArray = new boolean[36];
        }

        for (int i = 0; i < lockedSlotsArray.length; i++) {
            lockedSlotsArray[i] = lockedSlots.contains(i);
        }
    }

    public static boolean[] getLockedSlotsArray() {
        if (lockedSlotsArray == null) {
            lockedSlotsArray = new boolean[36];
        }
        return lockedSlotsArray;
    }

    public static void setLockedSlotsArray(boolean[] lockedSlotsArray) {
        if (lockedSlotsArray.length != 36) {
            // Convert array if sizes don't match
            boolean[] newArray = new boolean[36];
            for (int i = 0; i < Math.min(lockedSlotsArray.length, 36); i++) {
                newArray[i] = lockedSlotsArray[i];
            }
            SlotLockingHelper.lockedSlotsArray = newArray;
        } else {
            SlotLockingHelper.lockedSlotsArray = lockedSlotsArray;
        }

        if (lockedSlots == null) {
            lockedSlots = new ArrayList<>();
        }

        lockedSlots.clear();
        for (int i = 0; i < lockedSlotsArray.length; i++) {
            if (lockedSlotsArray[i]) {
                lockedSlots.add(i);
            }
        }
    }
}