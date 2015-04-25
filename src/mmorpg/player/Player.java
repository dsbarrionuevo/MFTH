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
public class Player extends Movable implements Placeable {

    private Room room;
    //
    private long timerHitTheDoor;
    private long timerToHitTheDoor;

    public Player() {
        super(18f, new Vector2f(), new Rectangle(0, 0, 20, 20));
        this.timerHitTheDoor = 0;
        this.timerToHitTheDoor = 1 * 1000;
    }

    @Override
    public void update(GameContainer container, int delta) {
        move(container, delta);
        //check if stand on door
        checkHitDoor(delta);
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.blue);
        this.body.setX(position.x);
        this.body.setY(position.y);
        g.fill(body);
    }

    private void checkHitDoor(int delta) {
        if (this.timerHitTheDoor > this.timerToHitTheDoor) {
            if (room.hitTheDoor(this)) {
                this.timerHitTheDoor = 0;
            }
        }
        this.timerHitTheDoor += delta;
    }

    private void move(GameContainer container, int delta) {
        Input input = container.getInput();
        float moveFactor = speed * (delta / 100f);
        if (input.isKeyDown(Input.KEY_LEFT) && room.canMoveTo(this, Room.DIRECTION_WEST) && room.movingInsideCamera(this, moveFactor, Room.DIRECTION_WEST)) {
            position.x -= moveFactor;
        }
        if (input.isKeyDown(Input.KEY_RIGHT) && room.canMoveTo(this, Room.DIRECTION_EAST) && room.movingInsideCamera(this, moveFactor, Room.DIRECTION_EAST)) {
            position.x += moveFactor;
        }
        if (input.isKeyDown(Input.KEY_UP) && room.canMoveTo(this, Room.DIRECTION_NORTH) && room.movingInsideCamera(this, moveFactor, Room.DIRECTION_NORTH)) {
            position.y -= moveFactor;
        }
        if (input.isKeyDown(Input.KEY_DOWN) && room.canMoveTo(this, Room.DIRECTION_SOUTH) && room.movingInsideCamera(this, moveFactor, Room.DIRECTION_SOUTH)) {
            position.y += moveFactor;
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
