package ae.skydoppler.dungeon.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DungeonTileMapConstructorTest {
    public static void main(String[] args) {
        String filePath = "src/client/java/ae/skydoppler/dungeon/map/testmaps/floor1.txt";
        byte[][] mapPixels = readMapPixelsFromFile(filePath);
        if (mapPixels == null) {
            System.out.println("Failed to read map pixels from file.");
            return;
        }
        MapTile[][] tileGrid = DungeonTileMapConstructor.constructMap(mapPixels);
        System.out.println("Modular tile grid size: " + tileGrid.length + " x " + tileGrid[0].length);
        printTileGridSummary(tileGrid);
    }

    public static byte[][] readMapPixelsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            java.util.List<byte[]> rows = new java.util.ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                byte[] row = new byte[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    row[i] = Byte.parseByte(tokens[i]);
                }
                rows.add(row);
            }
            return rows.toArray(new byte[rows.size()][]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void printTileGridSummary(MapTile[][] tileGrid) {
        for (int y = 0; y < tileGrid.length; y++) {
            for (int x = 0; x < tileGrid[0].length; x++) {
                MapTile tile = tileGrid[y][x];
                if (tile.getRoomType() != RoomType.NONE) {
                    System.out.print(tile.getRoomType());
                } else {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
}

