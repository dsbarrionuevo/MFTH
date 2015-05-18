package mfthclient.map.room;

import java.util.ArrayList;
import java.util.HashMap;
import mfthclient.camera.Camera;
import mfthclient.common.Placeable;
import mfthclient.map.tiles.Tile;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class Room {

    public static final int DIRECTION_NORTH = 0;
    public static final int DIRECTION_SOUTH = 1;
    public static final int DIRECTION_EAST = 2;
    public static final int DIRECTION_WEST = 3;
    //
    public static final int CORNER_TOP_LEFT = 0;
    public static final int CORNER_TOP_RIGHT = 1;
    public static final int CORNER_BOTTOM_LEFT = 2;
    public static final int CORNER_BOTTOM_RIGHT = 3;
    //
    private Tile[][] room;
    private final float tileWidth, tileHeight;
    private int roomId, roomWidth, roomHeight;
    private RoomBuilder builder;
    //
    private Camera camera;
    //
    //Displayable objects
    ArrayList<Placeable> objects;
    //las posiciones iniciales de los enemigos
    HashMap<Placeable, Vector2f> initPositions;

    public Room(int roomId, String source) {
        this.roomId = roomId;
        this.tileWidth = 50;
        this.tileHeight = 50;
        this.camera = Camera.getInstance();
        this.objects = new ArrayList<>();
        this.initPositions = new HashMap<>();
        this.builder = new RoomBuilder(source);
        this.build();
    }

    public final void build() {
        this.room = builder.build();
        this.roomWidth = room[0].length;
        this.roomHeight = room.length;
    }

    public void update(GameContainer container, int delta) throws SlickException {
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) != null) {
                objects.get(i).update(container, delta);
            }
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        //when using camera logic, this should be optimized
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].render(container, g);
            }
        }
        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) != null) {
                objects.get(i).render(container, g);
            }
        }
    }

    public boolean addObject(Placeable placeable, int tileX, int tileY){
        this.objects.add(placeable);
        return placeObject(placeable, tileX, tileY);
    }
    
    public boolean addObject(Placeable placeable, Vector2f position){
        this.objects.add(placeable);
        placeable.setPosition(position);
        return true;
    }
    
    public boolean addObject(Placeable placeable){
        this.objects.add(placeable);
        return true;
    }
    
    public boolean placeObject(Placeable placeable, int tileX, int tileY) {
        if (tileX < 0 || tileX > room[0].length - 1 || tileY < 0 || tileY > room.length - 1) {
            return false;
        }
        Vector2f newPosition = new Vector2f();
        newPosition.x = tileWidth * tileX + tileWidth / 2 - placeable.getWidth() / 2;
        newPosition.y = tileHeight * tileY + tileHeight / 2 - placeable.getHeight() / 2;
        placeable.setPosition(newPosition);
        return true;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Room) obj).getRoomId() == this.getRoomId();
    }

}
