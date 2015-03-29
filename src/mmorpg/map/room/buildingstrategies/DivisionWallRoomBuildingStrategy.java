package mmorpg.map.room.buildingstrategies;

import org.newdawn.slick.geom.Vector2f;
import mmorpg.map.tiles.BlankTile;
import mmorpg.map.tiles.Tile;
import mmorpg.map.tiles.WallTile;

/**
 *
 * @author Diego
 */
public class DivisionWallRoomBuildingStrategy extends RoomBuildingStrategy {

    private static final int ORIENTATION_VERTICAL = 0;
    private static final int ORIENTATION_HORIZONTAL = 1;

    public DivisionWallRoomBuildingStrategy(int widthLength, int heightLength, float tileWidth, float tileHeight) {
        super(widthLength, heightLength, tileWidth, tileHeight);
    }

    @Override
    public Tile[][] build() {
        Tile[][] map = new Tile[heightLength][widthLength];
        //blank map
        blankMap(map);
        buildChamber(map, widthLength, heightLength, ORIENTATION_HORIZONTAL, 0);
        return map;
    }

    private void buildChamber(Tile[][] map, int chamberWidth, int chamberHeight, int orientation, int countDivisions) {
        if (countDivisions >= 2) {
            return;
        }
        countDivisions++;
        if (orientation == ORIENTATION_VERTICAL) {
            int wallIndex = chamberWidth / 2;
            int randomDoorPosition = (int) (Math.random() * (chamberWidth / 2));
            for (int i = 0; i < map.length; i++) {
                Vector2f tilePosition = new Vector2f();
                tilePosition.x = wallIndex * tileWidth;
                tilePosition.y = i * tileHeight;
                if (i != randomDoorPosition) {
                    map[i][wallIndex] = new WallTile(tilePosition, tileWidth, tileHeight);
                } else {
                    map[i][wallIndex] = new BlankTile(tilePosition, tileWidth, tileHeight);
                }
            }
            buildChamber(map, wallIndex, chamberHeight, ORIENTATION_HORIZONTAL, countDivisions);
            buildChamber(map, chamberWidth - wallIndex, chamberHeight, ORIENTATION_HORIZONTAL, countDivisions);
        } else if (orientation == ORIENTATION_HORIZONTAL) {
            int wallIndex = chamberHeight / 2;
            int randomDoorPosition = (int) (Math.random() * (chamberHeight / 2));
            for (int i = 0; i < map[0].length; i++) {
                Vector2f tilePosition = new Vector2f();
                tilePosition.x = i * tileWidth;
                tilePosition.y = wallIndex * tileHeight;
                if (i != randomDoorPosition) {
                    map[wallIndex][i] = new WallTile(tilePosition, tileWidth, tileHeight);
                } else {
                    map[wallIndex][i] = new BlankTile(tilePosition, tileWidth, tileHeight);
                }
            }
            buildChamber(map, chamberWidth, wallIndex, ORIENTATION_VERTICAL, countDivisions);
            buildChamber(map, chamberWidth, chamberHeight - wallIndex, ORIENTATION_VERTICAL, countDivisions);
        }
    }

}
