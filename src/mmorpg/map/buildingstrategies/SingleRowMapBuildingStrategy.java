package mmorpg.map.buildingstrategies;

import mmorpg.map.room.Room;
import mmorpg.map.room.buildingstrategies.BorderRoomBuildingStrategy;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;
import mmorpg.map.tiles.Tile;

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
    public Room[] build() {
        Room[] map = new Room[roomsCount];
        int widthRoom = 16;
        int heightRoom = 12;
        if (this.orientation == ORIENTATION_HORIZONTAL) {
            for (int i = 0; i < map.length; i++) {
                RoomBuildingStrategy roomBuildingStrategy = new BorderRoomBuildingStrategy(widthRoom, heightRoom, tileWidth, tileHeight);
                Room newRoom = new Room(i,roomBuildingStrategy);
                if (i >= 0 && i != map.length - 1) {
                    newRoom.putDoor(newRoom.getRoomWidth() - 1, 6);
                }
                if (i <= map.length - 1 && i != 0) {
                    newRoom.putDoor(0, 6);
                }
                map[i] = newRoom;
            }
        } else if (this.orientation == ORIENTATION_VERTICAL) {
            for (int i = 0; i < map.length; i++) {
                RoomBuildingStrategy roomBuildingStrategy = new BorderRoomBuildingStrategy(widthRoom, heightRoom, tileWidth, tileHeight);
                Room newRoom = new Room(i,roomBuildingStrategy);
                if (i >= 0 && i != map.length - 1) {
                    newRoom.putDoor(8, newRoom.getRoomHeight()-1);
                }
                if (i <= map.length - 1 && i != 0) {
                    newRoom.putDoor(8, 0);
                }
                map[i] = newRoom;
            }
        }
        return map;
    }
}
