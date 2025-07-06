package ae.skydoppler.config.main_config.categories;

public class Inventory {

    public SlotLocking slotLocking = new SlotLocking();

    public static class SlotLocking {
        public boolean enabled = true;
        public boolean doSlotLockingInStorageUi = false;
        public float slotLockingToggleVolume = 1.0f; // Volume for the slot locking toggle sound
    }

}
