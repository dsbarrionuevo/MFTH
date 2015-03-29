package mmorpg.map.tiles;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class BlankTile extends Tile{

    public BlankTile(Vector2f position, float width, float height) {
        super(position, width, height, Color.white, true);
    }

    @Override
    public int getType() {
        return Tile.BLANK_TILE;
    }
    
}
