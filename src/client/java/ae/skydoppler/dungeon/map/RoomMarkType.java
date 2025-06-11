package ae.skydoppler.dungeon.map;

/**
 * Enum representing different types of room marks in a dungeon map.
 * For example, checkmarks or 'x's.
 */
public enum RoomMarkType {
    NONE,
    WHITE_CHECKMARK(34),
    GREEN_CHECKMARK(30),
    RED_X(18);

    private final int value;

    RoomMarkType() {
        this.value = 0; // Default value for NONE
    }

    RoomMarkType(int value) {
        this.value = value;
    }

    public static RoomMarkType fromValue(int value) {
        for (RoomMarkType type : values()) {
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
