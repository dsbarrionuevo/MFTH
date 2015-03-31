package mmorpg.map.buildingstrategies;

import mmorpg.map.Map;
import mmorpg.map.room.Room;
import mmorpg.map.room.buildingstrategies.BorderRoomBuildingStrategy;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;

/**
 *
 * @author Barrionuevo Diego
 */
public class SingleRowMapBuildingStrategy extends MapBuildingStrategy {

    public static final int ORIENTATION_HORIZONTAL = 0;
    public static final int ORIENTATION_VERTICAL = 1;

    private final int orientation;

    public SingleRowMapBuildingStrategy(int orientation, int roomsCount, float tileWidth, float tileHeight) {
        super(roomsCount, tileWidth, tileHeight);
        this.orientation = orientation;
    }

    @Override
    public Room[] build(Map map) {
        Room[] rooms = new Room[roomsCount];
        int widthRoom = 20;
        int heightRoom = 20;
        if (this.orientation == ORIENTATION_HORIZONTAL) {
            for (int i = 0; i < rooms.length; i++) {
                RoomBuildingStrategy roomBuildingStrategy = new BorderRoomBuildingStrategy(widthRoom, heightRoom, tileWidth, tileHeight);
                Room newRoom = new Room(i, roomBuildingStrategy);
                newRoom.setMap(map);
                if (i >= 0 && i != rooms.length - 1) {
                    newRoom.putDoor(rooms[i], rooms[i + 1], newRoom.getRoomWidth() - 1, 6);
                }
                if (i <= rooms.length - 1 && i != 0) {
                    newRoom.putDoor(rooms[i], rooms[i - 1], 0, 6);
                }
                rooms[i] = newRoom;
            }
        } else if (this.orientation == ORIENTATION_VERTICAL) {
            for (int i = 0; i < rooms.length; i++) {
                RoomBuildingStrategy roomBuildingStrategy = new BorderRoomBuildingStrategy(widthRoom, heightRoom, tileWidth, tileHeight);
                Room newRoom = new Room(i, roomBuildingStrategy);
                if (i >= 0 && i != rooms.length - 1) {
                    newRoom.putDoor(rooms[i], rooms[i + 1], 8, newRoom.getRoomHeight() - 1);
                }
                if (i <= rooms.length - 1 && i != 0) {
                    newRoom.putDoor(rooms[i], rooms[i - 1], 8, 0);
                }
                rooms[i] = newRoom;
            }
        }
        return rooms;
    }
}
