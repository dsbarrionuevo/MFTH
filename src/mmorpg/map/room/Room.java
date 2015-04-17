package mmorpg.map.room;

import java.util.ArrayList;
import java.util.HashMap;
import mmorpg.camera.Camera;
import mmorpg.common.Placeable;
import mmorpg.enemies.WallEnemy;
import mmorpg.map.Map;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;
import mmorpg.map.tiles.DoorTile;
import mmorpg.map.tiles.Tile;
import mmorpg.player.Player;
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
    private Tile[][] room;
    private RoomBuildingStrategy buildingStrategy;
    private final float tileWidth, tileHeight;
    private int roomId, roomWidth, roomHeight;
    //
    private Camera camera;
    private Map map;
    //
    //Displayable objects
    ArrayList<Placeable> objects;
    //las posiciones iniciales de los enemigos
    HashMap<Placeable, Vector2f> initPositions;

    public Room(int roomId, RoomBuildingStrategy buildingStrategy) {
        this.roomId = roomId;
        this.tileWidth = 50;
        this.tileHeight = 50;
        this.buildingStrategy = buildingStrategy;
        this.camera = Camera.getInstance();
        this.objects = new ArrayList<>();
        this.initPositions = new HashMap<>();
        this.build();
    }

    public final void build() {
        this.room = this.buildingStrategy.build();
        this.roomWidth = room[0].length;
        this.roomHeight = room.length;
        //add enemies

        if (this.roomId == 1) {
            for (int i = 0; i < 0; i++) {
                WallEnemy enemy = new WallEnemy();
                addObject(enemy, 2, 2);
                initPositions.put(enemy, new Vector2f(2, 2));
            }
        }
    }

    public DoorTile putDoor(int tileX, int tileY) {
        //only on borders...
        if ((tileX == 0 || tileX == room[0].length - 1) || (tileY == 0 || tileY == room.length - 1)) {
            room[tileY][tileX] = new DoorTile(tileX, tileY, tileWidth, tileHeight);
            return (DoorTile) room[tileY][tileX];
        }
        return null;
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

    public void move(Vector2f moveFactor, Placeable ignorePlaceable) {
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].getPosition().x += moveFactor.x;
                room[i][j].getPosition().y += moveFactor.y;
            }
        }
        for (Placeable object : objects) {
            if (ignorePlaceable == null) {
                object.getPosition().x += moveFactor.x;
                object.getPosition().y += moveFactor.y;
            } else {
                if (object != ignorePlaceable) {
                    object.getPosition().x += moveFactor.x;
                    object.getPosition().y += moveFactor.y;
                }
            }
        }
    }

    public void move(Vector2f moveFactor) {
        this.move(moveFactor, null);
    }

    public void moveToPosition(Vector2f newPosition) {
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].setPosition(new Vector2f(tileWidth * j + newPosition.x, tileHeight * i + newPosition.y));
            }
        }
        for (Placeable object : objects) {
            //move objects to their init position
            //object.setPosition(new Vector2f(object.getPosition().x + newPosition.x, object.getPosition().y+ newPosition.y));
            placeObject(object, (int) initPositions.get(object).x, (int) initPositions.get(object).y);
        }
    }

    public Tile getTileByPositionInRoom(int tileX, int tileY) {
        return room[tileY][tileX];
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

    public Tile getCurrentTile(Placeable placeable) {
        Tile foundTile = null;
        //int tileX = (int) (Math.floor((placeable.getPosition().x + placeable.getWidth() / 2 - tileWidth / 2) / tileWidth));
        //int tileY = (int) (Math.floor((placeable.getPosition().y + placeable.getHeight()/2 - tileHeight/2) / tileHeight));
        /*int tileX = (int) (Math.floor((placeable.getPosition().x) / tileWidth));
         int tileY = (int) (Math.floor((placeable.getPosition().y) / tileHeight));*/
        Vector2f mainPosition = room[0][0].getPosition();
        Vector2f absolutePosition = new Vector2f(
                placeable.getPosition().x - mainPosition.x,
                placeable.getPosition().y - mainPosition.y
        );
        int tileX = (int) (Math.floor((absolutePosition.x) / tileWidth));
        int tileY = (int) (Math.floor((absolutePosition.y) / tileHeight));
        foundTile = room[tileY][tileX];
        //System.out.println("Current tileX: " + tileX + ", tileY: " + tileY);
        return foundTile;
    }

    public Tile findNextTile(Placeable placeable, int direction) {
        Tile foundTile = null;
        Tile currentTile = getCurrentTile(placeable);
        int tileX = currentTile.getTileX();
        int tileY = currentTile.getTileY();
        switch (direction) {
            case (DIRECTION_WEST):
                if (tileX - 1 >= 0) {
                    foundTile = room[tileY][tileX - 1];
                }
                break;
            case (DIRECTION_EAST):
                if (tileX + 1 <= room[0].length - 1) {
                    foundTile = room[tileY][tileX + 1];
                }
                break;
            case (DIRECTION_NORTH):
                if (tileY - 1 >= 0) {
                    foundTile = room[tileY - 1][tileX];
                }
                break;
            case (DIRECTION_SOUTH):
                if (tileY + 1 <= room.length - 1) {
                    foundTile = room[tileY + 1][tileX];
                }
                break;
        }
        return foundTile;
    }

    public boolean canMoveTo(Placeable placeable, int direction) {
        Tile nextTile = this.findNextTile(placeable, direction);
        Vector2f position = placeable.getPosition();
        if (nextTile != null) {
            if (nextTile.isWalkable()) {
                return true;
            } else {
                switch (direction) {
                    case (DIRECTION_WEST):
                        return (Math.abs((nextTile.getPosition().x + nextTile.getWidth()) - position.x) >= 1);
                    case (DIRECTION_EAST):
                        return (Math.abs((nextTile.getPosition().x) - (position.x)) >= placeable.getWidth() + 2);
                    case (DIRECTION_NORTH):
                        return (Math.abs((nextTile.getPosition().y + nextTile.getHeight()) - position.y) > 1);
                    case (DIRECTION_SOUTH):
                        return (Math.abs((nextTile.getPosition().y) - (position.y)) >= placeable.getHeight() + 2);
                }
            }
        }
        return false;
    }

    public boolean movingInsideCamera(Placeable placeable, float distanceMoving, int direction) {
        Vector2f position = placeable.getPosition();
        boolean allowMoving = true;
        //65 = 50 + 25 - 10
        //   = 50*1 + 50/2 - 20/2
        //   = tileWidth*tilesPadding + tileWidth/2 + placeble.getWidth()/2
        float limit = 0f;
        float definedLimitWidth = tileWidth * camera.getPadding() + tileWidth / 2 - placeable.getWidth() / 2;
        float definedLimitHeight = tileHeight * camera.getPadding() + tileHeight / 2 - placeable.getHeight() / 2;
        Vector2f movement = new Vector2f();
        movement.x = 0;
        movement.y = 0;
        //direction = -1;
        switch (direction) {
            case (DIRECTION_WEST):
                limit = definedLimitWidth;
                if (position.x < limit) {
                    movement.x = distanceMoving;
                    move(movement, placeable);
                    allowMoving = false;
                }
                break;
            case (DIRECTION_EAST):
                limit = camera.getWidth() - definedLimitWidth;
                if (position.x > limit) {
                    movement.x = distanceMoving * (-1);
                    move(movement, placeable);
                    allowMoving = false;
                }
                break;
            case (DIRECTION_NORTH):
                limit = definedLimitHeight;
                if (position.y < limit) {
                    movement.y = distanceMoving;
                    move(movement, placeable);
                    allowMoving = false;
                }
                break;
            case (DIRECTION_SOUTH):
                limit = camera.getHeight() - definedLimitHeight;
                if (position.y > limit) {
                    movement.y = distanceMoving * (-1);
                    move(movement, placeable);
                    allowMoving = false;
                }
                break;
        }
        return allowMoving;
    }

    public void focusObject(Placeable placeable) {
        Vector2f position = this.getCurrentTile(placeable).getPosition();
        Vector2f mainPosition = room[0][0].getPosition();
        Vector2f center = new Vector2f(camera.getWidth() / 2, camera.getHeight() / 2);
        Vector2f finalPosition = new Vector2f(
                center.x - position.getX() - Math.abs(tileWidth / 2),
                center.y - position.getY() - Math.abs(tileHeight / 2)
        );
        move(finalPosition);
    }

    public boolean hitTheDoor(Placeable placeable) {
        if (getCurrentTile(placeable).getType() == Tile.DOOR_TILE) {
            map.nextRoom(this, (DoorTile) getCurrentTile(placeable), placeable);
            this.moveToPosition(new Vector2f(0, 0));
            return true;
        }
        return false;
    }

    public Tile[] getTilesOfType(int tileType) {
        Tile[] result = null;
        ArrayList<Tile> foundTiles = new ArrayList<>();
        for (int i = 0; i < this.room.length; i++) {
            for (int j = 0; j < this.room[0].length; j++) {
                Tile currentTile = this.room[i][j];
                if (currentTile.getType() == tileType) {
                    foundTiles.add(currentTile);
                }
            }
        }
        result = (Tile[]) foundTiles.toArray();
        return result;
    }

    public void addObject(Placeable placeable, int tileX, int tileY) {
        placeable.setRoom(this);
        this.placeObject(placeable, tileX, tileY);
        this.objects.add(placeable);
    }

    public void removeObject(Placeable placeable) {
        this.objects.remove(placeable);
    }

    public int getRoomWidth() {
        return roomWidth;
    }

    public int getRoomHeight() {
        return roomHeight;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public boolean equals(Object obj) {
        return ((Room) obj).getRoomId() == this.getRoomId();
    }

    public boolean isInsideTile(Placeable placeable) {
        Tile tile = getCurrentTile(placeable);
        if (tile == null) {
            return false;
        }
        Vector2f tilePosition = tile.getPosition();
        Vector2f position = placeable.getPosition();
        boolean r = (position.x > tilePosition.x
                && position.y > tilePosition.y
                && position.x < (tilePosition.x + tile.getWidth() - placeable.getWidth())
                && position.y < (tilePosition.y + tile.getHeight() - placeable.getHeight()));
        System.out.println(r);
        return r;
    }

}
