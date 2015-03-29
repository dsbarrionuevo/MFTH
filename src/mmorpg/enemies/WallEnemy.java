package mmorpg.enemies;

import mmorpg.map.Map;
import mmorpg.map.tiles.Tile;
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
    private Map map;

    public WallEnemy() {
        super(7f, new Vector2f(), new Rectangle(0, 0, 15, 15));
        this.currentDirection = (int) (Math.random() * 4);
    }

    @Override
    public void update(GameContainer container, int delta) {
        /*
        boolean stopMoving = !move(currentDirection, delta);
        if (stopMoving) {
            //change direction
            this.changeDirection();
        }
        */
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
        /*switch (this.currentDirection) {
            case (Map.DIRECTION_EAST):
                oppositeDirection = Map.DIRECTION_WEST;
                break;
            case (Map.DIRECTION_WEST):
                oppositeDirection = Map.DIRECTION_EAST;
                break;
            case (Map.DIRECTION_NORTH):
                oppositeDirection = Map.DIRECTION_SOUTH;
                break;
            case (Map.DIRECTION_SOUTH):
                oppositeDirection = Map.DIRECTION_NORTH;
                break;
        }*/
        this.changeDirection(oppositeDirection);
    }

    private boolean move(int direction, int delta) {
        /*float moveFactor = speed * (delta / 100f);
         Tile nextTile = null;
         Vector2f prevPosition = new Vector2f(position.x, position.y);
         switch (direction) {
         case (Map.DIRECTION_WEST):
         nextTile = map.findNextTile(this, Map.DIRECTION_WEST);
         if (nextTile != null) {
         if (nextTile.isWalkable()) {
         position.x -= moveFactor;
         } else {
         if (Math.abs((nextTile.getPosition().x + nextTile.getWidth()) - position.x) > 1) {
         position.x -= moveFactor;
         }
         }
         }
         break;
         case (Map.DIRECTION_EAST):
         nextTile = map.findNextTile(this, Map.DIRECTION_EAST);
         if (nextTile != null) {
         if (nextTile.isWalkable()) {
         position.x += moveFactor;
         } else {
         if (Math.abs((nextTile.getPosition().x) - (position.x)) >= getWidth() + 2) {
         position.x += moveFactor;
         }
         }
         }
         break;
         case (Map.DIRECTION_NORTH):
         nextTile = map.findNextTile(this, Map.DIRECTION_NORTH);
         if (nextTile != null) {
         if (nextTile.isWalkable()) {
         position.y -= moveFactor;
         } else {
         if (Math.abs((nextTile.getPosition().y + nextTile.getHeight()) - position.y) > 1) {
         position.y -= moveFactor;
         }
         }
         }
         break;
         case (Map.DIRECTION_SOUTH):
         nextTile = map.findNextTile(this, Map.DIRECTION_SOUTH);
         if (nextTile != null) {
         if (nextTile.isWalkable()) {
         position.y += moveFactor;
         } else {
         if (Math.abs((nextTile.getPosition().y) - (position.y)) >= getHeight() + 2) {
         position.y += moveFactor;
         }
         }
         }
         break;
         }
         //return false when stop moving
         return !(prevPosition.x == position.x && prevPosition.y == position.y);
         //return nextTile != null;
         */
        return true;
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
