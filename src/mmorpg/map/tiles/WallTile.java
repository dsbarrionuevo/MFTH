package mmorpg.map.tiles;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class WallTile extends Tile {

    public WallTile(Vector2f position, float width, float height) {
        super(position, width, height, Color.gray, false);
    }

    @Override
    public int getType() {
        return Tile.WALL_TILE;
    }

}
