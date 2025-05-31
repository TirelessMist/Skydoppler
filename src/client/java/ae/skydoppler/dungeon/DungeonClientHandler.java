/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ae.skydoppler.dungeon;

import ae.skydoppler.SkydopplerClient;
import ae.skydoppler.dungeon.map.DungeonMapHandler;

/**
 *
 * @author VGRADN
 */
public class DungeonClientHandler {

    private DungeonMapHandler dungeonMapHandler;
    private int currentDungeonFloor;

    public DungeonClientHandler() {
        }

        public DungeonClientHandler(DungeonMapHandler dungeonMapHandler, int currentDungeonFloor) {
            this.dungeonMapHandler = dungeonMapHandler;
            this.currentDungeonFloor = currentDungeonFloor;
        }

        public DungeonMapHandler getDungeonMapHandler() {
            return dungeonMapHandler;
        }

        public void setDungeonMapHandler(DungeonMapHandler dungeonMapHandler) {
            this.dungeonMapHandler = dungeonMapHandler;
        }

        public int getCurrentDungeonFloor() {
            return currentDungeonFloor;
        }

        public void setCurrentDungeonFloor(int currentDungeonFloor) {
            this.currentDungeonFloor = currentDungeonFloor;
        }

}
