package ae.skydoppler.dungeon.map;

import java.awt.*;

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

    public Color getColor() {
        return switch (this) {
            case NORMAL -> new Color(136, 96, 20, 255); // White
            case ENTRANCE -> new Color(0, 255, 0, 255); // Green
            case UNKNOWN -> new Color(0, 0, 0, 0); // Yellow
            case PUZZLE -> new Color(163, 28, 218, 255); // Blue
            case TRAP -> new Color(216, 113, 31, 255); // Red
            case MINIBOSS -> new Color(244, 212, 25, 255); // Purple
            case BLOOD -> new Color(214, 20, 20, 255); // Dark Red
            case FAIRY -> new Color(228, 90, 209, 255); // Light Blue
            default -> new Color(0, 0, 0, 255); // Black
        };
    }
}
