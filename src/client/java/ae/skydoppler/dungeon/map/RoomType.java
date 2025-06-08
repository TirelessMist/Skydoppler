package ae.skydoppler.dungeon.map;

public enum RoomType {
    NONE(0),
    NORMAL(63),
    ENTRANCE(30),
    UNKNOWN(85),
    PUZZLE(66),
    TRAP(62),
    MINIBOSS(74),
    BLOOD(18),
    FAIRY(82);

    private final int value;

    RoomType(int value) {
        this.value = value;
    }

    public static RoomType fromValue(byte value) {
        for (RoomType type : values()) {
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
