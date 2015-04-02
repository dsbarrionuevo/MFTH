package mmorpg.map;

import java.util.ArrayList;
import mmorpg.common.Placeable;
import mmorpg.map.buildingstrategies.MapBuildingStrategy;
import mmorpg.map.buildingstrategies.SingleRowMapBuildingStrategy;
import mmorpg.map.room.Room;
import mmorpg.map.tiles.DoorTile;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Diego
 */
public class Map {

    private ArrayList<Room> rooms;
    private int currentRoom;
    private MapBuildingStrategy buildingStrategy;

    public Map(int roomsCount) {
        this.buildingStrategy = new SingleRowMapBuildingStrategy(SingleRowMapBuildingStrategy.ORIENTATION_HORIZONTAL, roomsCount, 50, 50);
        this.buildingStrategy.build(this);
        this.rooms = this.buildingStrategy.getRooms();
        this.currentRoom = 0;
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        this.rooms.get(currentRoom).render(container, g);
    }

    public boolean placeObject(Placeable placeable, int room, int tileX, int tileY) {
        placeable.setRoom(this.rooms.get(room));
        return this.rooms.get(room).placeObject(placeable, tileX, tileY);
    }

    public boolean placeObject(Placeable placeable, int tileX, int tileY) {
        return this.placeObject(placeable, this.currentRoom, tileX, tileY);
    }

    public Room getRoom(int room) {
        if (room < 0 || room > rooms.size() - 1) {
            return null;
        }
        return this.rooms.get(room);
    }

    public Room getCurrentRoom() {
        return this.getRoom(this.currentRoom);
    }

    public void nextRoom(Room currentRoom, DoorTile doorTile, Placeable placeable) {
        DoorTile otherDoor = doorTile.getConnectedTo();
        Room nextRoom = otherDoor.getMyRoom();
        //placeObject(placeable, nextRoom.getRoomId(), otherDoor.getTileX(), otherDoor.getTileY());
        currentRoom.removeObject(placeable);
        nextRoom.addObject(placeable, otherDoor.getTileX(), otherDoor.getTileY());
        nextRoom.focusObject(placeable);
        changeRoom(nextRoom.getRoomId());
    }

    public void changeRoom(int idNewRoom) {
        this.currentRoom = idNewRoom;
    }
}
