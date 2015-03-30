package mmorpg.map.buildingstrategies;

import mmorpg.map.Map;
import mmorpg.map.room.Room;

/**
 *
 * @author Diego
 */
public abstract class MapBuildingStrategy {

    protected int roomsCount;
    protected float tileWidth, tileHeight;

    public MapBuildingStrategy(int roomsCount, float tileWidth, float tileHeight) {
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.roomsCount = roomsCount;
    }

    public abstract Room[] build(Map map);

}
