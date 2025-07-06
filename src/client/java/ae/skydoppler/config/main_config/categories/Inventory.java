package ae.skydoppler.config.main_config.categories;

import ae.skydoppler.config.main_config.MainConfigCategory;
import net.minecraft.text.Text;

public class Inventory extends MainConfigCategory {

    public SlotLocking slotLocking = new SlotLocking();

    public Inventory() {
        super("inventory", Text.translatable("config.ae.skydoppler.main_config.category.inventory"), 3);
    }

    public static class SlotLocking {
        public boolean enabled = true;
        public boolean doSlotLockingInStorageUi = false;
        public float slotLockingToggleVolume = 1.0f; // Volume for the slot locking toggle sound
    }
}
