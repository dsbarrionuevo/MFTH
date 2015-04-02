package mmorpg.map.room.buildingstrategies;

import mmorpg.map.tiles.Tile;
import mmorpg.map.tiles.WallTile;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class RandomRoomBuildingStrategy extends RoomBuildingStrategy {

    public RandomRoomBuildingStrategy(int widthLength, int heightLength, float tileWidth, float tileHeight) {
        super(widthLength, heightLength, tileWidth, tileHeight);
    }

    @Override
    public Tile[][] build() {
        Tile[][] map = new Tile[heightLength][widthLength];
        borderMap(map);
        int j = heightLength - 1, i = (int) (Math.random() * (widthLength - 2)) + 1;
        map[j][i] = new WallTile(new Vector2f(j * tileWidth, i * tileHeight), tileWidth, tileHeight);
        return map;

    }

}
