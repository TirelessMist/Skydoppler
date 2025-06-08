package ae.skydoppler.dungeon.map;

import java.awt.*;

public class DungeonTileMapConstructor {

    /**
     * Constructs a 2D array of MapTile objects from the provided map pixel data.
     *
     * @param mapPixels A 2D byte array representing the map pixels.
     * @return A modular grid of MapTiles.
     */
    public static MapTile[][] constructMap(byte[][] mapPixels) {

        if (mapPixels == null || mapPixels.length == 0 || mapPixels[0].length == 0) {
            return null; // Return an empty map if input is invalid
        }

        Point entranceRoomPosition = determineEntranceRoomPosition(mapPixels); // Store the entrance room position
        if (entranceRoomPosition == null) {
            return null; // Return an empty map if no entrance room is found
        }

        int roomTileSideLength = determineEntranceRoomSize(mapPixels, entranceRoomPosition); // Store the room side length
        int checkMarkOffset = roomTileSideLength / 2; // Store the check mark offset from the top-left corner of the room, which is the center of the room
        int distanceFromEdge = getDistanceFromNearestEdge(mapPixels, entranceRoomPosition); // Store the distance from the edge
        int DISTANCE_BETWEEN_ROOMS = 4; // Distance between rooms in pixels (always 4 pixels)
        int doorOffset = roomTileSideLength / 2; // Store the horizontal distance from the top-left corner of the room to the same x coordinate of the top door

        int gridSize = mapPixels.length / roomTileSideLength;
        if (distanceFromEdge >= roomTileSideLength) {
            gridSize += 2; // Increase grid size if distance from edge is greater than or equal to room side length. This is to ensure that the grid can accommodate puzzle rooms that are at the edge of the map on some floors.
            distanceFromEdge -= roomTileSideLength; // Adjust distance from edge to account for the new grid size
        }

        MapTile[][] dungeonMap = new MapTile[gridSize][gridSize]; // Initialize the dungeon map

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {

                int roomPosY = row * roomTileSideLength + distanceFromEdge + (row * DISTANCE_BETWEEN_ROOMS);
                int roomPosX = col * roomTileSideLength + distanceFromEdge + (col * DISTANCE_BETWEEN_ROOMS);

                RoomType roomType = getRoomType(mapPixels[roomPosY][roomPosX]);
                CheckMarkType checkMarkType = getCheckMarkType(mapPixels[roomPosY + checkMarkOffset][roomPosX + checkMarkOffset]);

                DoorType topDoorType = getDoorType(mapPixels[roomPosY - 1][roomPosX + doorOffset]);
                DoorType rightDoorType = getDoorType(mapPixels[roomPosY + doorOffset][roomPosX + roomTileSideLength]);
                DoorType bottomDoorType = getDoorType(mapPixels[roomPosY + roomTileSideLength][roomPosX + doorOffset]);
                DoorType leftDoorType = getDoorType(mapPixels[roomPosY + doorOffset][roomPosX - 1]);

                dungeonMap[row][col] = new MapTile(roomType, topDoorType, rightDoorType, bottomDoorType, leftDoorType, checkMarkType);

            }
        }

        return dungeonMap;
    }

    //region Helper Methods
    private static Point determineEntranceRoomPosition(byte[][] mapPixels) {

        for (int y = 0; y < mapPixels.length; y++) {
            for (int x = 0; x < mapPixels[y].length; x++) {
                if (mapPixels[y][x] == RoomType.ENTRANCE.getValue()) {
                    return new Point(x, y); // Return the position of the entrance room
                }
            }
        }

        return null; // Return null if no entrance room is found
    }

    private static int determineEntranceRoomSize(byte[][] mapPixels, Point entranceRoomPosition) {
        int sideLength = 0;

        for (int y = entranceRoomPosition.y; y < mapPixels.length; y++) {
            if (mapPixels[y][entranceRoomPosition.x] == RoomType.ENTRANCE.getValue()) {
                sideLength++;
            } else {
                break; // Stop when we hit a non-entrance room
            }
        }

        return sideLength;
    }

    private static int getDistanceFromNearestEdge(byte[][] mapPixels, Point entranceRoomPosition) {

        int distance = 0;
        int quadrant = determineEntranceRoomQuadrant(mapPixels, entranceRoomPosition);

        distance = switch (quadrant) {
            case 1 -> // Top-left
                    Math.min(entranceRoomPosition.x, entranceRoomPosition.y);
            case 2 -> // Top-right
                    Math.min(mapPixels[0].length - entranceRoomPosition.x - 1, entranceRoomPosition.y);
            case 3 -> // Bottom-left
                    Math.min(entranceRoomPosition.x, mapPixels.length - entranceRoomPosition.y - 1);
            case 4 -> // Bottom-right
                    Math.min(mapPixels[0].length - entranceRoomPosition.x - 1, mapPixels.length - entranceRoomPosition.y - 1);
            default -> distance;
        };

        return distance;
    }

    private static int determineEntranceRoomQuadrant(byte[][] mapPixels, Point entranceRoomPosition) {
        int quadrant = 0;

        if (entranceRoomPosition.x < mapPixels[0].length / 2 && entranceRoomPosition.y < mapPixels.length / 2) {
            quadrant = 1; // Top-left
        } else if (entranceRoomPosition.x >= mapPixels[0].length / 2 && entranceRoomPosition.y < mapPixels.length / 2) {
            quadrant = 2; // Top-right
        } else if (entranceRoomPosition.x < mapPixels[0].length / 2 && entranceRoomPosition.y >= mapPixels.length / 2) {
            quadrant = 3; // Bottom-left
        } else if (entranceRoomPosition.x >= mapPixels[0].length / 2 && entranceRoomPosition.y >= mapPixels.length / 2) {
            quadrant = 4; // Bottom-right
        }

        return quadrant;
    }
    //endregion

    //region Type Conversion Methods
    private static RoomType getRoomType(byte pixelValue) {
        if (RoomType.fromValue(pixelValue) == null) {
            return RoomType.NONE; // Default to NONE if no match found
        }
        return RoomType.fromValue(pixelValue);
    }

    private static DoorType getDoorType(byte pixelValue) {
        if (DoorType.fromValue(pixelValue) == null) {
            return DoorType.NONE; // Default to NONE if no match found
        }
        return DoorType.fromValue(pixelValue);
    }

    private static CheckMarkType getCheckMarkType(byte pixelValue) {
        if (pixelValue == CheckMarkType.WHITE.getValue()) {
            return CheckMarkType.WHITE;
        } else if (pixelValue == CheckMarkType.GREEN.getValue()) {
            return CheckMarkType.GREEN;
        } else {
            return CheckMarkType.NONE; // Default to NONE if no match found
        }
    }
    //endregion

}
