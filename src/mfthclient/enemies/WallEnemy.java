package mfthclient.enemies;

import mfthclient.map.room.Room;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class WallEnemy extends Enemy {

    private int currentDirection;
    //
    private Room room;

    public WallEnemy() {
        super(7f, new Vector2f(), new Rectangle(0, 0, 15, 15));
        this.currentDirection = (int) (Math.random() * 4);
    }

    @Override
    public void update(GameContainer container, int delta) {
        
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.red);
        this.body.setX(position.x);
        this.body.setY(position.y);
        g.fill(body);
    }

    public void changeDirection() {
        int oldDirection = this.currentDirection;
        do {
            this.currentDirection = (int) (Math.random() * 4);
        } while (this.currentDirection == oldDirection);
    }

    public void changeDirection(int direction) {
        this.currentDirection = direction;
    }

    public void changeToOppositeDirection() {
        int oppositeDirection = -1;
        switch (this.currentDirection) {
            case (Room.DIRECTION_EAST):
                oppositeDirection = Room.DIRECTION_WEST;
                break;
            case (Room.DIRECTION_WEST):
                oppositeDirection = Room.DIRECTION_EAST;
                break;
            case (Room.DIRECTION_NORTH):
                oppositeDirection = Room.DIRECTION_SOUTH;
                break;
            case (Room.DIRECTION_SOUTH):
                oppositeDirection = Room.DIRECTION_NORTH;
                break;
        }
        this.changeDirection(oppositeDirection);
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public Room getRoom() {
        return this.room;
    }
}
