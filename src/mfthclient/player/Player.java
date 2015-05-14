package mfthclient.player;

import java.util.logging.Level;
import java.util.logging.Logger;
import mfthclient.common.Movable;
import mfthclient.common.Placeable;
import mfthclient.map.room.Room;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
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
    //
    private Animation walkingFront, walkingBack, walkingLeft, walkingRight;

    public Player() {
        super(10f, new Vector2f(), new Rectangle(0, 0, 32, 32));
        this.timerHitTheDoor = 0;
        this.timerToHitTheDoor = 1 * 1000;
        //
        //setupAnimations();
    }

    @Override
    public void update(GameContainer container, int delta) {
        //move(container, delta);
    }

    @Override
    public void render(GameContainer gc, Graphics g) {
        g.setColor(Color.blue);
        this.body.setX(position.x);
        this.body.setY(position.y);
        if (graphic != null) {
            ((Animation) graphic).draw(body.getX(), body.getY());
        } else {
            g.fill(body);
        }
    }

    private void move(GameContainer container, int delta) {
        Input input = container.getInput();
        int direction = -1;
        if (input.isKeyDown(Input.KEY_LEFT)) {
            direction = Room.DIRECTION_WEST;
        }
        if (input.isKeyDown(Input.KEY_RIGHT)) {
            direction = Room.DIRECTION_EAST;
        }
        if (input.isKeyDown(Input.KEY_UP)) {
            direction = Room.DIRECTION_NORTH;
        }
        if (input.isKeyDown(Input.KEY_DOWN)) {
            direction = Room.DIRECTION_SOUTH;
        }
        if(direction!=-1){
            
        }
    }

    private void updateAnimation(int delta) {
        if (graphic != null) {
            if (((Animation) graphic).isStopped()) {
                ((Animation) graphic).start();
            }
            ((Animation) graphic).update(delta);
        }
    }

    private void setupAnimations() {
        try {
            //speed: 10px ==> duration: 340 milis
            int duration = 340;
            //animations
            String model = "model1";
            this.walkingFront = new Animation(new Image[]{
                new Image("res/images/players/" + model + "/front0.png"),
                new Image("res/images/players/" + model + "/front1.png"),
                new Image("res/images/players/" + model + "/front2.png")
            }, duration, true);
            this.walkingBack = new Animation(new Image[]{
                new Image("res/images/players/" + model + "/back0.png"),
                new Image("res/images/players/" + model + "/back1.png"),
                new Image("res/images/players/" + model + "/back2.png")
            }, duration, true);
            this.walkingLeft = new Animation(new Image[]{
                new Image("res/images/players/" + model + "/left0.png"),
                new Image("res/images/players/" + model + "/left1.png"),
                new Image("res/images/players/" + model + "/left2.png")
            }, duration, true);
            this.walkingRight = new Animation(new Image[]{
                new Image("res/images/players/" + model + "/right0.png"),
                new Image("res/images/players/" + model + "/right1.png"),
                new Image("res/images/players/" + model + "/right2.png")
            }, duration, true);
            setGraphic(walkingFront);
            ((Animation) graphic).stop();
        } catch (SlickException ex) {
            Logger.getLogger(Player.class.getName()).log(Level.SEVERE, null, ex);
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
