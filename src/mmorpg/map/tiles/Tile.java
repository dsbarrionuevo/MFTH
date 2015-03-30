package mmorpg.map.tiles;

import mmorpg.common.Drawable;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public abstract class Tile extends Drawable {

    public static final int BLANK_TILE = 0;
    public static final int WALL_TILE = 1;
    public static final int DOOR_TILE = 2;

    protected Color color;
    protected boolean walkable;

    protected int tileX, tileY;

    public Tile(Vector2f position, float width, float height, Color color, boolean walkable) {
        super(position, new Rectangle(0, 0, width, height));
        this.color = color;
        this.walkable = walkable;
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(this.color);
        this.body.setX(position.x);
        this.body.setY(position.y);
        g.fill(body);
        g.setColor(Color.black);
        g.draw(body);
    }

    public boolean isWalkable() {
        return walkable;
    }

    public abstract int getType();

    public int getTileX() {
        return tileX;
    }

    public void setTileX(int tileX) {
        this.tileX = tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void setTileY(int tileY) {
        this.tileY = tileY;
    }

}
