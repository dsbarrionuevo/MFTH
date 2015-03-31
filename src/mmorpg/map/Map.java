package mmorpg.map;

import mmorpg.common.Placeable;
import mmorpg.map.buildingstrategies.MapBuildingStrategy;
import mmorpg.map.buildingstrategies.SingleRowMapBuildingStrategy;
import mmorpg.map.room.Room;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

/**
 *
 * @author Diego
 */
public class Map {

    private Room[] rooms;
    private int currentRoom;
    private MapBuildingStrategy buildingStrategy;

    public Map(int roomsCount) {
        this.buildingStrategy = new SingleRowMapBuildingStrategy(SingleRowMapBuildingStrategy.ORIENTATION_HORIZONTAL, roomsCount, 50, 50);
        this.rooms = buildingStrategy.build(this);
        this.currentRoom = 0;
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        this.rooms[currentRoom].render(container, g);
    }

    public boolean placeObject(Placeable placeable, int room, int tileX, int tileY) {
        placeable.setRoom(this.rooms[room]);
        return this.rooms[room].placeObject(placeable, tileX, tileY);
    }

    public boolean placeObject(Placeable placeable, int tileX, int tileY) {
        return this.placeObject(placeable, this.currentRoom, tileX, tileY);
    }

    public Room getRoom(int room) {
        if (room < 0 || room > rooms.length - 1) {
            return null;
        }
        return this.rooms[room];
    }

    public Room getCurrentRoom() {
        return this.getRoom(this.currentRoom);
    }

    public Room nextRoom(Placeable placeable) {
        Room currentRoom = placeable.getRoom();
        
        //... siempre avanza a la siguiente sala...
        /*
         this.currentRoom = currentRoomId + 1;
         this.rooms[currentRoom].placeObject(placeable, 1, 1);
         return this.rooms[currentRoom];
         */
        return null;
    }
}
