package mmorpg.common;

import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public interface Placeable {
    
    public void setPosition(Vector2f position);
    public Vector2f getPosition();
    public float getWidth();
    public float getHeight();
    
}
