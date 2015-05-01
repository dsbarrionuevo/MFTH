package mfthclient.enemies;

import mfthclient.common.Movable;
import mfthclient.common.Placeable;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public abstract class Enemy extends Movable implements Placeable {

    public Enemy(float speed, Vector2f position, Shape body) {
        super(speed, position, body);
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
