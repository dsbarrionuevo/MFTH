package mmorpg.items;

import mmorpg.common.Drawable;
import mmorpg.common.Placeable;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class Treasure extends Drawable implements Placeable {

    public Treasure() {
        super(new Vector2f(), new Circle(0, 0, 10));
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.yellow);
        this.body.setX(position.x);
        this.body.setY(position.y);
        g.fill(body);
        g.setColor(Color.orange);
        g.draw(body);
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position = position;
    }

    @Override
    public Vector2f getPosition() {
        return this.position;
    }

    @Override
    public float getWidth() {
        return this.width;
    }

    @Override
    public float getHeight() {
        return this.height;
    }

}