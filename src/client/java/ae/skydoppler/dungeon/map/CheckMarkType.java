package ae.skydoppler.dungeon.map;

public enum CheckMarkType {
    NONE,
    WHITE(34),
    GREEN(30);

    private final int value;

    CheckMarkType() {
        this.value = 0; // Default value for NONE
    }

    CheckMarkType(int value) {
        this.value = value;
    }

    public static CheckMarkType fromValue(int value) {
        for (CheckMarkType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return NONE; // Default to NONE if no match found
    }

    public int getValue() {
        return value;
    }
}
