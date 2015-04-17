package mmorpg.mapreader;

/**
 *
 * @author Barrionuevo Diego
 */
public class TileFile {

    private int id;//in map of the room
    private TileType tileType;
    private String resourcePath;

    public TileFile(int id, TileType tileType, String resourcePath) {
        this.id = id;
        this.tileType = tileType;
        this.resourcePath = resourcePath;
    }

}
