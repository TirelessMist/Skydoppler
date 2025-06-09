package ae.skydoppler;

import ae.skydoppler.dungeon.map.CheckMarkType;
import ae.skydoppler.dungeon.map.DoorType;
import ae.skydoppler.dungeon.map.MapTile;
import ae.skydoppler.dungeon.map.RoomType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class HudRenderingEntrypoint implements ClientModInitializer {
    private static final Identifier MAP_LAYER = Identifier.of(Skydoppler.MOD_ID, "dungeon_map_layer");

    public static MapTile[][] dungeonMapTiles = new MapTile[0][0];

    public static float posX = 0; // X-position of the map in pixels
    public static float posY = 0; // Y-position of the map in pixels
    public static float scale = 1; // Scale of the map rendering
    public static float roomSize = 15; // Size of each room tile in pixels
    public static float doorWidth = 2; // Width of the door lines in pixels
    public static float roomGap = 2; // Gap between rooms in pixels
    public static boolean isMapVisible = true;

    private static void render(DrawContext context, RenderTickCounter tickCounter) {
        // If map is not visible or there are no tiles, don't render
        if (!isMapVisible || dungeonMapTiles == null || dungeonMapTiles.length == 0 || dungeonMapTiles[0].length == 0) {
            return;
        }

        // First pass: Find all room UUIDs and their bounds
        Map<Integer, Room> roomMap = findRoomBounds(dungeonMapTiles);

        // Second pass: Draw rooms
        for (Room room : roomMap.values()) {
            // Skip rooms of type NONE
            if (room.roomType == RoomType.NONE) {
                continue;
            }

            // Calculate room dimensions in screen space
            int roomScreenX = (int) (room.minX * (roomSize + roomGap) * scale + posX);
            int roomScreenY = (int) (room.minY * (roomSize + roomGap) * scale + posY);
            int roomScreenWidth = (int) ((room.maxX - room.minX + 1) * roomSize * scale);
            int roomScreenHeight = (int) ((room.maxY - room.minY + 1) * roomSize * scale);

            // Draw room background
            Color roomColor = getRoomColor(room.roomType);
            context.fill(
                    roomScreenX,
                    roomScreenY,
                    roomScreenX + roomScreenWidth,
                    roomScreenY + roomScreenHeight,
                    roomColor.getRGB()
            );

            // Draw checkmark if present
            if (room.checkMarkType != CheckMarkType.NONE) {
                // Calculate center of the actual room (not just top-left tile)
                int checkMarkX = (int) (roomScreenX + roomScreenWidth / 2);
                int checkMarkY = (int) (roomScreenY + roomScreenHeight / 2);

                Color checkMarkColor = getCheckMarkColor(room.checkMarkType);
                int checkMarkSize = (int) (4 * scale); // Reasonable size for checkmark

                // Draw checkmark
                context.fill(
                        checkMarkX - checkMarkSize / 2,
                        checkMarkY - checkMarkSize / 2,
                        checkMarkX + checkMarkSize / 2,
                        checkMarkY + checkMarkSize / 2,
                        checkMarkColor.getRGB()
                );
            }
        }

        // Third pass: Draw doors
        for (int y = 0; y < dungeonMapTiles.length; y++) {
            for (int x = 0; x < dungeonMapTiles[0].length; x++) {
                MapTile tile = dungeonMapTiles[y][x];
                if (tile.getRoomType() == RoomType.NONE) {
                    continue;
                }

                // Calculate tile position in screen space
                float tileScreenX = x * (roomSize + roomGap) * scale + posX;
                float tileScreenY = y * (roomSize + roomGap) * scale + posY;

                // Draw doors for this tile
                drawDoors(context, tile, tileScreenX, tileScreenY);
            }
        }
    }

    /**
     * Finds all rooms in the dungeon map and calculates their bounds
     */
    private static Map<Integer, Room> findRoomBounds(MapTile[][] tiles) {
        Map<Integer, Room> roomMap = new HashMap<>();

        for (int y = 0; y < tiles.length; y++) {
            for (int x = 0; x < tiles[0].length; x++) {
                MapTile tile = tiles[y][x];
                if (tile.getRoomType() == RoomType.NONE) {
                    continue;
                }

                int uuid = tile.getUuid();
                if (!roomMap.containsKey(uuid)) {
                    Room room = new Room(uuid, tile.getRoomType());
                    // Initialize with this tile's position
                    room.minX = x;
                    room.minY = y;
                    room.maxX = x;
                    room.maxY = y;

                    // Determine the most significant checkmark for this room
                    if (tile.getCheckMarkType() != CheckMarkType.NONE) {
                        room.checkMarkType = tile.getCheckMarkType();
                    }

                    roomMap.put(uuid, room);
                } else {
                    // Update existing room bounds
                    Room room = roomMap.get(uuid);
                    room.minX = Math.min(room.minX, x);
                    room.minY = Math.min(room.minY, y);
                    room.maxX = Math.max(room.maxX, x);
                    room.maxY = Math.max(room.maxY, y);

                    // Update checkmark if this tile has one and the current one doesn't
                    if (room.checkMarkType == CheckMarkType.NONE && tile.getCheckMarkType() != CheckMarkType.NONE) {
                        room.checkMarkType = tile.getCheckMarkType();
                    } else if (room.checkMarkType != CheckMarkType.NONE && tile.getCheckMarkType() != CheckMarkType.NONE) {
                        // If both have checkmarks, prefer GREEN over WHITE
                        if (tile.getCheckMarkType() == CheckMarkType.GREEN) {
                            room.checkMarkType = CheckMarkType.GREEN;
                        }
                    }
                }
            }
        }

        return roomMap;
    }

    /**
     * Draws doors for a specific tile
     */
    private static void drawDoors(DrawContext context, MapTile tile, float tileX, float tileY) {
        // Draw top door
        if (tile.getTopDoorType() != DoorType.NONE) {
            Color doorColor = getDoorColor(tile.getTopDoorType());
            float doorX = tileX + roomSize * scale / 2 - doorWidth * scale / 2;
            float doorY = tileY - roomGap * scale;

            context.fill(
                    (int) doorX,
                    (int) doorY,
                    (int) (doorX + doorWidth * scale),
                    (int) (doorY + roomGap * scale),
                    doorColor.getRGB()
            );
        }

        // Draw right door
        if (tile.getRightDoorType() != DoorType.NONE) {
            Color doorColor = getDoorColor(tile.getRightDoorType());
            float doorX = tileX + roomSize * scale;
            float doorY = tileY + roomSize * scale / 2 - doorWidth * scale / 2;

            context.fill(
                    (int) doorX,
                    (int) doorY,
                    (int) (doorX + roomGap * scale),
                    (int) (doorY + doorWidth * scale),
                    doorColor.getRGB()
            );
        }

        // Draw bottom door
        if (tile.getBottomDoorType() != DoorType.NONE) {
            Color doorColor = getDoorColor(tile.getBottomDoorType());
            float doorX = tileX + roomSize * scale / 2 - doorWidth * scale / 2;
            float doorY = tileY + roomSize * scale;

            context.fill(
                    (int) doorX,
                    (int) doorY,
                    (int) (doorX + doorWidth * scale),
                    (int) (doorY + roomGap * scale),
                    doorColor.getRGB()
            );
        }

        // Draw left door
        if (tile.getLeftDoorType() != DoorType.NONE) {
            Color doorColor = getDoorColor(tile.getLeftDoorType());
            float doorX = tileX - roomGap * scale;
            float doorY = tileY + roomSize * scale / 2 - doorWidth * scale / 2;

            context.fill(
                    (int) doorX,
                    (int) doorY,
                    (int) (doorX + roomGap * scale),
                    (int) (doorY + doorWidth * scale),
                    doorColor.getRGB()
            );
        }
    }

    /**
     * Gets the color for a room type
     */
    private static Color getRoomColor(RoomType roomType) {
        return switch (roomType) {
            case NORMAL -> new Color(127, 90, 19);
            case ENTRANCE -> new Color(0, 255, 0);
            case UNKNOWN -> new Color(71, 71, 71);
            case PUZZLE -> new Color(182, 36, 205);
            case TRAP -> new Color(216, 113, 31);
            case MINIBOSS -> new Color(244, 212, 25);
            case BLOOD -> new Color(214, 20, 20);
            case FAIRY -> new Color(228, 90, 209);
            default -> new Color(0, 0, 0);
        };
    }

    /**
     * Gets the color for a door type
     */
    private static Color getDoorColor(DoorType doorType) {
        return switch (doorType) {
            case NORMAL -> new Color(127, 90, 19);  // Normal door color
            case WITHER -> new Color(75, 0, 130);   // Wither door color (purple)
            case BLOOD -> new Color(214, 20, 20);   // Blood door color (red)
            default -> new Color(255, 255, 255);    // Default white (shouldn't happen)
        };
    }

    /**
     * Gets the color for a checkmark type
     */
    private static Color getCheckMarkColor(CheckMarkType checkMarkType) {
        return switch (checkMarkType) {
            case WHITE -> new Color(255, 255, 255);
            case GREEN -> new Color(0, 255, 0);
            default -> new Color(0, 0, 0);
        };
    }

    @Override
    public void onInitializeClient() {
        // Attach our rendering code to before the chat hud layer. Our layer will render right before the chat. The API will take care of z spacing and automatically add 200 after every layer.
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> layeredDrawer.attachLayerBefore(IdentifiedLayer.CHAT, MAP_LAYER, HudRenderingEntrypoint::render));
    }

    /**
     * Helper class to represent a room with its bounds and properties
     */
    private static class Room {
        int uuid;
        RoomType roomType;
        int minX, minY, maxX, maxY;
        CheckMarkType checkMarkType = CheckMarkType.NONE;

        public Room(int uuid, RoomType roomType) {
            this.uuid = uuid;
            this.roomType = roomType;
        }
    }
}