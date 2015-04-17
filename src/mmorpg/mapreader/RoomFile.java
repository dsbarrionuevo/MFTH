package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class RoomFile {

    private int id;
    private RoomType roomType;
    private int[][] map;

    public RoomFile(int id, RoomType roomType, int[][] map) {
        this.id = id;
        this.roomType = roomType;
        this.map = map;
    }

}
