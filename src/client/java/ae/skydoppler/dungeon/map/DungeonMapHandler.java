/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ae.skydoppler.dungeon.map;

import ae.skydoppler.dungeon.room_detection.MapParser;
import ae.skydoppler.dungeon.room_detection.MapReassembler;
import net.minecraft.item.map.MapState;

/**
 *
 * @author VGRADN
 */
public static class DungeonMapHandler {

    public static MapReassembler.Tile[][] map = null;

    public static boolean useRawMap = false; // Set to true if you want to use the raw map data instead of the parsed map. Faster.

    // This should be called every time the map is updated.
    public static void updateMap(MapReassembler.Tile[][] mapTiles) {

        map = mapTiles;
    }

    /*public static class BloodPath {

        public static List<Point> getBlackDoorLocationsInOrder() {
        
            // Should return a list of (X, Z) world locations of the same top-left corners of the black doors.
            return null; // Placeholder.
        }

    }*/
}
