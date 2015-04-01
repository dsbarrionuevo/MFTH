package mmorpg.map.room;

import java.util.ArrayList;
import mmorpg.camera.Camera;
import mmorpg.common.Placeable;
import mmorpg.map.Map;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;
import mmorpg.map.tiles.DoorTile;
import mmorpg.map.tiles.Tile;
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
    private ArrayList<Room> passages;
    //
    private Camera camera;
    private Vector2f offset;
    private Map map;

    public Room(int roomId, RoomBuildingStrategy buildingStrategy) {
        this.roomId = roomId;
        this.tileWidth = 50;
        this.tileHeight = 50;
        this.buildingStrategy = buildingStrategy;
        this.passages = new ArrayList<>();
        this.camera = Camera.getInstance();
        this.offset = new Vector2f();
        this.build();
    }

    public final void build() {
        this.room = this.buildingStrategy.build();
        this.roomWidth = room[0].length;
        this.roomHeight = room.length;
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].setTileX(j);
                room[i][j].setTileY(i);
            }
        }
    }

    public boolean putDoor(Room fromRoom, Room toRoom, int tileX, int tileY) {
        //only on borders...
        if ((tileX == 0 || tileX == room[0].length - 1) || (tileY == 0 || tileY == room.length - 1)) {
            room[tileY][tileX] = new DoorTile(fromRoom, toRoom, new Vector2f(tileX * tileWidth, tileY * tileHeight), tileWidth, tileHeight);
            return true;
        }
        return false;
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        //when using camera logic, this should be optimized
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].render(container, g);
            }
        }
    }

    @Deprecated
    public void move(Vector2f newPosition) {
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].setPosition(newPosition);
            }
        }
    }

    public void moveX(float factor) {
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].getPosition().x += factor;
            }
        }
    }

    public void moveY(float factor) {
        for (int i = 0; i < room.length; i++) {
            for (int j = 0; j < room[0].length; j++) {
                room[i][j].getPosition().y += factor;
            }
        }
    }

    public Tile getByPositionInRoom(int tileX, int tileY) {
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
        Vector2f absolutePosition = new Vector2f(
                Math.abs(this.offset.x - room[0][0].getPosition().x) + placeable.getPosition().x,
                Math.abs(this.offset.y - room[0][0].getPosition().y) + placeable.getPosition().y
        );
        int tileX = (int) (Math.floor((absolutePosition.x) / tileWidth));
        int tileY = (int) (Math.floor((absolutePosition.y) / tileHeight));
        foundTile = room[tileY][tileX];
        System.out.println("Current tileX: " + tileX + ", tileY: " + tileY);
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
        switch (direction) {
            case (DIRECTION_WEST):
                if (position.x < 65) {
                    moveX(distanceMoving);
                    allowMoving = false;
                }
            case (DIRECTION_EAST):
                if (position.x > 715) {
                    moveX(distanceMoving * (-1));
                    allowMoving = false;
                }
            case (DIRECTION_NORTH):
                if (position.y < 65) {
                    moveY(distanceMoving);
                    allowMoving = false;
                }
            case (DIRECTION_SOUTH):
                if (position.y > 515) {
                    moveY(distanceMoving * (-1));
                    allowMoving = false;
                }
        }
        return allowMoving;
    }

    public void hitTheDoor(Placeable placeable) {
        if (getCurrentTile(placeable).getType() == Tile.DOOR_TILE) {
            Room nextRoom = map.nextRoom(placeable);
            //placeable.setRoom(nextRoom);
        }
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

    public void addPassage(Room connectedTo) {
        for (Room passage : passages) {
            if (!passage.equals(connectedTo)) {
                this.passages.add(connectedTo);
            }
        }
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

}
