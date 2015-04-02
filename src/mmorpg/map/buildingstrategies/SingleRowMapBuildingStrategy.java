package mmorpg.map.buildingstrategies;

import java.util.ArrayList;
import mmorpg.map.Map;
import mmorpg.map.room.Room;
import mmorpg.map.room.buildingstrategies.BorderRoomBuildingStrategy;
import mmorpg.map.room.buildingstrategies.RandomRoomBuildingStrategy;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;
import mmorpg.map.tiles.DoorTile;

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
    public void build(Map map) {
        this.rooms = new ArrayList<>();
        int widthRoom = 16;
        int heightRoom = 12;
        //create the rooms
        if (this.orientation == ORIENTATION_HORIZONTAL) {
            for (int i = 0; i < roomsCount; i++) {
                RoomBuildingStrategy roomBuildingStrategy = new BorderRoomBuildingStrategy(widthRoom, heightRoom, tileWidth, tileHeight);
                Room newRoom = new Room(i, roomBuildingStrategy);
                newRoom.setMap(map);
                rooms.add(newRoom);
            }
        } else if (this.orientation == ORIENTATION_VERTICAL) {
            for (int i = 0; i < roomsCount; i++) {
                RoomBuildingStrategy roomBuildingStrategy = new BorderRoomBuildingStrategy(widthRoom, heightRoom, tileWidth, tileHeight);
                Room newRoom = new Room(i, roomBuildingStrategy);
                newRoom.setMap(map);
                rooms.add(newRoom);
            }
        }

        //now create the passages
        this.passages = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            Room currentRoom = rooms.get(i);
            if (i < rooms.size() - 1) {
                Room nextRoom = rooms.get(i + 1);
                if (this.orientation == ORIENTATION_HORIZONTAL) {
                    int randomPosition = ((int) (Math.random() * (heightRoom - 2)) + 1);
                    connectRooms(currentRoom, currentRoom.getRoomWidth() - 1, randomPosition, nextRoom, 0, randomPosition);
                } else if (this.orientation == ORIENTATION_VERTICAL) {
                    int randomPosition = ((int) (Math.random() * (widthRoom - 2)) + 1);
                    connectRooms(currentRoom, randomPosition, currentRoom.getRoomHeight() - 1, nextRoom, randomPosition, 0);
                }
            }
        }
    }

    private void connectRooms(Room room1, int tileX1, int tileY1, Room room2, int tileX2, int tileY2) {
        DoorTile doorRoom1 = room1.putDoor(tileX1, tileY1);
        DoorTile doorRoom2 = room2.putDoor(tileX2, tileY2);
        doorRoom1.setMyRoom(room1);
        doorRoom1.setConnectedTo(doorRoom2);
        doorRoom2.setMyRoom(room2);
        doorRoom2.setConnectedTo(doorRoom1);
    }

}
