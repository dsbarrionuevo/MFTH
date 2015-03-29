package mmorpg.map.tiles;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class DoorTile extends Tile {

    public DoorTile(Vector2f position, float width, float height) {
        super(position, width, height, Color.lightGray, true);
    }

    @Override
    public int getType() {
        return Tile.DOOR_TILE;
    }

}
