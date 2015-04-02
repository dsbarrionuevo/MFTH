package mmorpg.map.buildingstrategies;

import java.util.ArrayList;
import mmorpg.map.Map;
import mmorpg.map.room.Room;
import mmorpg.map.room.buildingstrategies.BorderRoomBuildingStrategy;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;
import mmorpg.map.tiles.DoorTile;
import org.newdawn.slick.geom.Vector2f;

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
        if (this.orientation == ORIENTATION_HORIZONTAL) {
            for (int i = 0; i < roomsCount; i++) {
                RoomBuildingStrategy roomBuildingStrategy = new BorderRoomBuildingStrategy(widthRoom, heightRoom, tileWidth, tileHeight);
                Room newRoom = new Room(i, roomBuildingStrategy);
                newRoom.setMap(map);
                /*
                 if (i >= 0 && i != rooms.length - 1) {
                 newRoom.putDoor(rooms[i], rooms[i + 1], newRoom.getRoomWidth() - 1, 6);
                 }
                 if (i <= rooms.length - 1 && i != 0) {
                 newRoom.putDoor(rooms[i], rooms[i - 1], 0, 6);
                 }*/
                rooms.add(newRoom);
            }
        } /*else if (this.orientation == ORIENTATION_VERTICAL) {
         for (int i = 0; i < roomsCount; i++) {
         RoomBuildingStrategy roomBuildingStrategy = new BorderRoomBuildingStrategy(widthRoom, heightRoom, tileWidth, tileHeight);
         Room newRoom = new Room(i, roomBuildingStrategy);
         //                 if (i >= 0 && i != rooms.length - 1) {
         //                 newRoom.putDoor(rooms[i], rooms[i + 1], 8, newRoom.getRoomHeight() - 1);
         //                 }
         //                 if (i <= rooms.length - 1 && i != 0) {
         //                 newRoom.putDoor(rooms[i], rooms[i - 1], 8, 0);
         //                 }
         rooms.add(newRoom);
         }}*/

        //now create the passages
        this.passages = new ArrayList<>();
        for (int i = 0; i < rooms.size(); i++) {
            Room currentRoom = rooms.get(i);
            if (i < rooms.size() - 1) {
                Room nextRoom = rooms.get(i + 1);
                /*Passage passage = super.findPassageByRoom(currentRoom);
                if (passage == null) {
                    passage = new Passage(currentRoom, nextRoom);
                } else {
                    passage.addConnection(nextRoom);
                }
                passages.add(passage);
                DoorTile otherDoor = new DoorTile(nextRoom, null, new Vector2f(tileWidth * 0, tileHeight * 6), tileWidth, tileHeight);
                currentRoom.putDoor(currentRoom, nextRoom, currentRoom.getRoomWidth() - 1, 6);
                //passages.add(new Passage(nextRoom,currentRoom));
                */
                connectRooms(currentRoom, nextRoom);
            }
            /*
             if (i > 0) {
             Room prevRoom = rooms.get(i - 1);
             passages.add(new Passage(currentRoom, prevRoom));
             //passages.add(new Passage(prevRoom, currentRoom));
             }*/
        }
    }
    
    private void connectRooms(Room room1, Room room2){
        DoorTile doorRoom1 = room1.putDoor(room1.getRoomWidth()-1, 2);
        DoorTile doorRoom2 = room2.putDoor(0, 2);
        doorRoom1.setMyRoom(room1);
        doorRoom1.setConnectedTo(doorRoom2);
        doorRoom2.setMyRoom(room2);
        doorRoom2.setConnectedTo(doorRoom1);
    }
    
}
