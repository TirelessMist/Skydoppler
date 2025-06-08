package ae.skydoppler.dungeon.map;

public class MapTile {

    private RoomType roomType;

    private DoorType topDoorType;
    private DoorType rightDoorType;
    private DoorType bottomDoorType;
    private DoorType leftDoorType;

    private CheckMarkType checkMarkType;

    public MapTile() {
        this.roomType = RoomType.NONE;
        this.topDoorType = DoorType.NONE;
        this.rightDoorType = DoorType.NONE;
        this.bottomDoorType = DoorType.NONE;
        this.leftDoorType = DoorType.NONE;
        this.checkMarkType = CheckMarkType.NONE;
    }

    public MapTile(RoomType roomType, DoorType topDoorType, DoorType rightDoorType, DoorType bottomDoorType, DoorType leftDoorType, CheckMarkType checkMarkType) {
        this.roomType = roomType;
        this.topDoorType = topDoorType;
        this.rightDoorType = rightDoorType;
        this.bottomDoorType = bottomDoorType;
        this.leftDoorType = leftDoorType;
        this.checkMarkType = checkMarkType;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public DoorType getTopDoorType() {
        return topDoorType;
    }

    public void setTopDoorType(DoorType topDoorType) {
        this.topDoorType = topDoorType;
    }

    public DoorType getRightDoorType() {
        return rightDoorType;
    }

    public void setRightDoorType(DoorType rightDoorType) {
        this.rightDoorType = rightDoorType;
    }

    public DoorType getBottomDoorType() {
        return bottomDoorType;
    }

    public void setBottomDoorType(DoorType bottomDoorType) {
        this.bottomDoorType = bottomDoorType;
    }

    public DoorType getLeftDoorType() {
        return leftDoorType;
    }

    public void setLeftDoorType(DoorType leftDoorType) {
        this.leftDoorType = leftDoorType;
    }

    public CheckMarkType getCheckMarkType() {
        return checkMarkType;
    }

    public void setCheckMarkType(CheckMarkType checkMarkType) {
        this.checkMarkType = checkMarkType;
    }

}