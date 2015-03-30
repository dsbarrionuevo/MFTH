package mmorpg.map.tiles;

import mmorpg.map.room.Room;
import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class DoorTile extends Tile {

    private Room myRoom, connectedTo;

    public DoorTile(Room myRoom, Room connectedTo, Vector2f position, float width, float height) {
        super(position, width, height, Color.lightGray, true);
        this.myRoom = myRoom;
        this.connectedTo = connectedTo;
    }

    @Override
    public int getType() {
        return Tile.DOOR_TILE;
    }

}
