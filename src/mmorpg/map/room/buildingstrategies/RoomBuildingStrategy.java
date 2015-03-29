package mmorpg.map.room.buildingstrategies;

import mmorpg.map.tiles.BlankTile;
import mmorpg.map.tiles.Tile;
import mmorpg.map.tiles.WallTile;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public abstract class RoomBuildingStrategy {

    protected int widthLength, heightLength;
    protected float tileWidth, tileHeight;

    public RoomBuildingStrategy(int widthLength, int heightLength, float tileWidth, float tileHeight) {
        this.widthLength = widthLength;
        this.heightLength = heightLength;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
    }

    public abstract Tile[][] build();

    protected void blankMap(Tile[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = new BlankTile(new Vector2f(j * tileWidth, i * tileHeight), tileWidth, tileHeight);
            }
        }
    }

    protected void borderMap(Tile[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (i == 0 || i == heightLength - 1 || j == 0 || j == widthLength - 1) {
                    map[i][j] = new WallTile(new Vector2f(j * tileWidth, i * tileHeight), tileWidth, tileHeight);
                } else {
                    map[i][j] = new BlankTile(new Vector2f(j * tileWidth, i * tileHeight), tileWidth, tileHeight);
                }
            }
        }
    }

}