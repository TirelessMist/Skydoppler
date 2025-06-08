package ae.skydoppler.dungeon.map;

public enum DoorType {
    NONE(0),
    NORMAL(63),
    WITHER(119),
    BLOOD(18);

    private final int value;

    DoorType(int value) {
        this.value = value;
    }

    public static DoorType fromValue(byte value) {
        for (DoorType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return NONE; // Default to NONE if no match found
    }

    public byte getValue() {
        return (byte) value;
    }
}
