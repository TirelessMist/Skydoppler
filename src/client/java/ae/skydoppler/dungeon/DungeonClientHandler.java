/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package ae.skydoppler.dungeon;

/**
 * @author VGRADN
 */
public class DungeonClientHandler {

    private int currentDungeonFloor;

    public DungeonClientHandler() {
    }

    public DungeonClientHandler(int currentDungeonFloor) {
        this.currentDungeonFloor = currentDungeonFloor;
    }

    public int getCurrentDungeonFloor() {
        return currentDungeonFloor;
    }

    public void setCurrentDungeonFloor(int currentDungeonFloor) {
        this.currentDungeonFloor = currentDungeonFloor;
    }

}
