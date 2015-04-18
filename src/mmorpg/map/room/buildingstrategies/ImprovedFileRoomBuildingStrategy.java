package mmorpg.map.room.buildingstrategies;

import mmorpg.map.tiles.BlankTile;
import mmorpg.map.tiles.DoorTile;
import mmorpg.map.tiles.Tile;
import mmorpg.map.tiles.WallTile;
import mmorpg.mapreader.RoomFile;
import mmorpg.mapreader.RoomType;
import mmorpg.mapreader.TileFile;
import mmorpg.mapreader.TileType;

/**
 *
 * @author Barrionuevo Diego
 */
public class ImprovedFileRoomBuildingStrategy extends RoomBuildingStrategy {

    private RoomFile roomFile;

    public ImprovedFileRoomBuildingStrategy(RoomFile roomFile, float tileWidth, float tileHeight) {
        super(roomFile.getMap()[0].length, roomFile.getMap().length, tileWidth, tileHeight);
        this.roomFile = roomFile;
    }

    @Override
    public Tile[][] build() {
        Tile[][] map = new Tile[heightLength][widthLength];
        RoomType roomType = roomFile.getRoomType();
        int[][] roomMap = roomFile.getMap();
        for (int i = 0; i < roomMap.length; i++) {
            for (int j = 0; j < roomMap[0].length; j++) {
                TileFile tileFile = roomType.findTileFile(roomMap[i][j]);
                if (tileFile != null) {
                    TileType tileType = tileFile.getTileType();
                    if ("empty".equalsIgnoreCase(tileType.getName())) {
                        map[i][j] = new BlankTile(j, i, tileWidth, tileHeight);
                    } else if ("wall".equalsIgnoreCase(tileType.getName())) {
                        map[i][j] = new WallTile(j, i, tileWidth, tileHeight);
                    }else if ("door".equalsIgnoreCase(tileType.getName())) {
                        map[i][j] = new DoorTile(j, i, tileWidth, tileHeight);
                    }
                } else {
                    //shouldnt go this line...
                    map[i][j] = new BlankTile(j, i, tileWidth, tileHeight);
                }
            }
        }
        return map;
    }

}
