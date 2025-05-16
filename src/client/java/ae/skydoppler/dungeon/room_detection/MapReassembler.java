package ae.skydoppler.dungeon.room_detection;

import ae.skydoppler.SkydopplerClient;

import java.awt.*;

public class MapReassembler {

    private static int borderWidth;
    private static int borderHeight; // TODO: Set this to the correct value, hardcoded depending on the current dungeon floor (just like borderWidth is set).

    private static int tileLength;

    public static Tile[][] reassembleMap(byte[][] mapPixels) {

        Tile[][] newMap;

        // set map grid size based on the current dungeon floor from scoreboard
        switch (SkydopplerClient.currentDungeonFloor) {
            case 0: {
                newMap = new Tile[7][7];
                borderWidth = 22;
                tileLength = 18;
                break;
            }
            case 1: {
                newMap = new Tile[7][9];
                borderWidth = 22;
                borderHeight = 11;
                tileLength = 18;
                break;
            }
            case 2:
            case 3: {
                newMap = new Tile[9][9];
                borderWidth = 11;
                borderHeight = 11;
                tileLength = 18;
                break;
            }
            case 4:
            case 5: {
                newMap = new Tile[11][9];
                borderWidth = 5;
                borderHeight = 5;
                tileLength = 16;
                break;
            }
            case 6:
            case 7: {
                newMap = new Tile[11][11];
                borderWidth = 5;
                tileLength = 16;
                break;
            }
            default: {
                newMap = new Tile[0][0]; // Later, add actual calculation for the map size, as a fallback in case scoreboard dungeon floor detection doesn't work.
            }
        }

        for (int row = 0; row < newMap.length; row++) {
            for (int col = 0; col < newMap[0].length; col++) {

                newMap[row][col] = new Tile();
                newMap[row][col].setTileType(getRoomTileAtPixel(mapPixels, new Point(col * tileLength + borderWidth, row * tileLength + borderHeight)));

            }
        }
        return newMap;
    }

    private static TileType getRoomTileAtPixel(byte[][] mapPixels, Point pos) {

        for (int row = ; row < mapPixels.length; row++) {
            for (int col = 0; col < mapPixels[0].length; col++) {

                switch (mapPixels[row][col]) {

                    case 0:
                        return TileType.EMPTY;
                    case 30:
                        return TileType.ENTRANCE_ROOM;
                    case 63:
                        return TileType.NORMAL_ROOM;
                    case 85:
                        return TileType.UNEXPLORED_ROOM;
                    case 66:
                        return TileType.PUZZLE_ROOM;
                    case 74:
                        return TileType.MINIBOSS_ROOM;
                    case 62:
                        return TileType.TRAP_ROOM;
                    case 82:
                        return TileType.FAIRY_ROOM;
                    case 18:
                        return TileType.BLOOD_ROOM;

                }

            }
        }
        return null;
    }

    private static TileType getDoorTileAtPixel(byte[][] mapPixels, Point pos) {

        for (int row = 0; row < mapPixels.length; row++) {
            for (int col = 0; col < mapPixels[0].length; col++) {

                switch (mapPixels[row][col]) {

                    case 0:
                        return TileType.EMPTY;
                    case 63:
                        return TileType.NORMAL_DOOR;
                    case 119:
                        return TileType.WITHER_DOOR;
                    case 18:
                        return TileType.BLOOD_DOOR;

                }

            }
        }
        return null;
    }

    private static boolean checkForDoor(byte[][] mapPixels, Point pos) {
        // This should be called by a function that checks for doors for a specific tile.
        return false; // Placeholder.
    }

    public enum TileType {
        EMPTY,
        ENTRANCE_ROOM,
        NORMAL_ROOM,
        FAIRY_ROOM,
        UNEXPLORED_ROOM,
        MINIBOSS_ROOM,
        PUZZLE_ROOM,
        TRAP_ROOM,
        BLOOD_ROOM,
        NORMAL_DOOR,
        WITHER_DOOR,
        BLOOD_DOOR
    }

    public enum CheckType {
        NONE,
        WHITE,
        GREEN
    }

    public static class Tile {
        private TileType tileType;
        private CheckType checkType;

        public Tile() {
            this.tileType = TileType.EMPTY;
            this.checkType = CheckType.NONE;
        }

        public Tile(TileType tileType, CheckType checkType) {
            this.tileType = tileType;
            this.checkType = checkType;
        }

        public TileType getTileType() {
            return tileType;
        }

        public void setTileType(TileType tileType) {
            this.tileType = tileType;
        }

        public CheckType getCheckType() {
            return checkType;
        }

        public void setCheckType(CheckType checkType) {
            this.checkType = checkType;
        }
    }

}
