package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class RoomType {

    private int id;
    private String name;
    private TileFile[] tiles;

    public RoomType(int id, String name, TileFile[] tiles) {
        this.id = id;
        this.name = name;
        this.tiles = tiles;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public TileFile[] getTiles() {
        return tiles;
    }

}
