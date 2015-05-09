package mfthclient.map.room;

import mfthclient.map.tiles.BlankTile;
import mfthclient.map.tiles.Tile;
import mfthclient.map.tiles.WallTile;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Barrionuevo Diego
 */
public class RoomBuilder {

    private String source;
    protected int widthLength, heightLength;
    protected float tileWidth, tileHeight;

    public RoomBuilder(String source) {
        this.source = source;
        this.tileWidth = 50;
        this.tileHeight = 50;
    }

    public Tile[][] build() {
        Tile[][] room;
        JSONObject root = new JSONObject(source);
        int roomId = root.getInt("id_room");
        int roomType = root.getInt("room_type");
        int[][] mapCoors = getMap(root, "map");
        widthLength = mapCoors[0].length;
        heightLength = mapCoors.length;
        room = new Tile[heightLength][widthLength];
        for (int i = 0; i < heightLength; i++) {
            for (int j = 0; j < widthLength; j++) {
                int mapTile = mapCoors[i][j];
                if (mapTile == 0) {
                    room[i][j] = new BlankTile(j, i, tileWidth, tileHeight);
                } else {
                    room[i][j] = new WallTile(j, i, tileWidth, tileHeight);
                }
            }

        }
        return room;
    }

    private int[][] getMap(JSONObject obj, String mapName) {
        int[][] mapCoors;
        JSONArray mapRows = obj.getJSONArray(mapName);
        mapCoors = new int[mapRows.length()][mapRows.getJSONArray(0).length()];
        for (int i = 0; i < mapRows.length(); i++) {
            JSONArray currentRow = mapRows.getJSONArray(i);
            for (int j = 0; j < currentRow.length(); j++) {
                mapCoors[i][j] = Integer.parseInt(currentRow.get(j).toString());
            }
        }
        return mapCoors;
    }

}
