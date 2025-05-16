/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ae.skydoppler.dungeon.map;

import java.awt.Point;
import java.util.List;

import ae.skydoppler.dungeon.room_detection.MapParser;
import ae.skydoppler.dungeon.room_detection.MapReassembler;

/**
 *
 * @author VGRADN
 */
public class MapHandler {

    private MapReassembler.Tile[][] map;

    public MapHandler() {
        map = MapReassembler.reassembleMap(MapParser.parseMapFromSlot());
    }

    public MapReassembler.Tile[][] getMap() {
        return map;
    }

    // This should be called every time the map is updated.
    public void updateMap() {
        map = MapReassembler.reassembleMap(MapParser.parseMapFromSlot());
    }

    public static class BloodPath {

        public static List<Point> getBlackDoorLocationsInOrder() {
        
            // Should return a list of (X, Z) world locations of the same top-left corners of the black doors.
            return null; // Placeholder.
        }

    }
}
