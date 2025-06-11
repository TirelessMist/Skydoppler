package ae.skydoppler.dungeon.map;

public class MapTile {

    private RoomType roomType;

    private int id; // Unique identifier for the tile, if needed

    private DoorType topDoorType;
    private DoorType rightDoorType;
    private DoorType bottomDoorType;
    private DoorType leftDoorType;

    private RoomMarkType roomMarkType;

    public MapTile() {
        this.roomType = RoomType.NONE;
        this.id = 0; // Default UUID, can be set later if needed
        this.topDoorType = DoorType.NONE;
        this.rightDoorType = DoorType.NONE;
        this.bottomDoorType = DoorType.NONE;
        this.leftDoorType = DoorType.NONE;
        this.roomMarkType = RoomMarkType.NONE;
    }

    public MapTile(RoomType roomType, int id, DoorType topDoorType, DoorType rightDoorType, DoorType bottomDoorType, DoorType leftDoorType, RoomMarkType roomMarkType) {
        this.roomType = roomType;
        this.id = id;
        this.topDoorType = topDoorType;
        this.rightDoorType = rightDoorType;
        this.bottomDoorType = bottomDoorType;
        this.leftDoorType = leftDoorType;
        this.roomMarkType = roomMarkType;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getUuid() {
        return id;
    }

    public void setUuid(int uuid) {
        this.id = uuid;
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

    public RoomMarkType getRoomMarkType() {
        return roomMarkType;
    }

    public void setRoomMarkType(RoomMarkType roomMarkType) {
        this.roomMarkType = roomMarkType;
    }

}