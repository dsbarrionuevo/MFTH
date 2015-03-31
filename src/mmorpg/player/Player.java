package mmorpg.player;

import mmorpg.common.Movable;
import mmorpg.common.Placeable;
import mmorpg.map.room.Room;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class Player extends Movable implements Placeable{

    private Room room;

    public Player() {
        super(30f, new Vector2f(), new Rectangle(0, 0, 20, 20));
    }

    @Override
    public void update(GameContainer container, int delta) {
        move(container, delta);
        //check if stand on door
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.blue);
        this.body.setX(position.x);
        this.body.setY(position.y);
        g.fill(body);
    }

    private void move(GameContainer container, int delta) {
        Input input = container.getInput();
        float moveFactor = speed * (delta / 100f);
        if (input.isKeyDown(Input.KEY_LEFT) && room.canMoveTo(this, Room.DIRECTION_WEST)) {
            if (position.x < 65) {
                room.moveX(moveFactor);
            } else {
                position.x -= moveFactor;
            }
        }
        if (input.isKeyDown(Input.KEY_RIGHT) && room.canMoveTo(this, Room.DIRECTION_EAST)) {
            if (position.x > 715) {
                room.moveX(moveFactor * (-1));
            } else {
                position.x += moveFactor;
            }
        }
        if (input.isKeyDown(Input.KEY_UP) && room.canMoveTo(this, Room.DIRECTION_NORTH)) {
            if (position.y < 65) {
                room.moveY(moveFactor);
            } else {
                position.y -= moveFactor;
            }
        }
        if (input.isKeyDown(Input.KEY_DOWN) && room.canMoveTo(this, Room.DIRECTION_SOUTH)) {
            if (position.y > 515) {
                room.moveY(moveFactor * (-1));
            } else {
                position.y += moveFactor;
            }
        }
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public void setPosition(Vector2f position) {
        this.position = position;
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }

}
