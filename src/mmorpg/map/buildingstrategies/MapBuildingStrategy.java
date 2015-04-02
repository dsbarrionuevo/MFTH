package mmorpg.map.buildingstrategies;

import java.util.ArrayList;
import mmorpg.map.Map;
import mmorpg.map.room.Room;

/**
 *
 * @author Diego
 */
public abstract class MapBuildingStrategy {

    protected int roomsCount;
    protected float tileWidth, tileHeight;
    protected ArrayList<Room> rooms;
    protected ArrayList<Passage> passages;

    public MapBuildingStrategy(int roomsCount, float tileWidth, float tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.roomsCount = roomsCount;
        this.rooms = new ArrayList<>();
        this.passages = new ArrayList<>();
    }

    public abstract void build(Map map);

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public ArrayList<Passage> getPassages() {
        return passages;
    }

    public final  Passage findPassageByRoom(Room target) {
        Passage found = null;
        for (Passage passage : passages) {
            if (passage.getRoom().equals(target)) {
                found = passage;
            }
        }
        return found;
    }

    protected final Passage findPassageByIdRoom(int target) {
        Passage found = null;
        for (Passage passage : passages) {
            if (passage.getRoom().getRoomId() == target) {
                found = passage;
            }
        }
        return found;
    }

    public class Passage {

        private Room room;
        private ArrayList<Room> connections;

        public Passage(Room room, ArrayList<Room> passages) {
            this.room = room;
            this.connections = passages;
        }

        public Passage(Room room, Room connection) {
            this(room);
            this.addConnection(connection);
        }

        public Room getRoom() {
            return room;
        }

        public ArrayList<Room> getConnections() {
            return connections;
        }

        public Passage(Room room) {
            this(room, new ArrayList<Room>());
        }

        public void addConnection(Room connection) {
            this.connections.add(connection);
        }

    }

}
