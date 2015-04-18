package mmorpg.map.buildingstrategies;

import java.util.ArrayList;
import mmorpg.map.Map;
import mmorpg.map.room.Room;
import mmorpg.map.room.buildingstrategies.ImprovedFileRoomBuildingStrategy;
import mmorpg.map.room.buildingstrategies.RoomBuildingStrategy;
import mmorpg.mapreader.MapFile;
import mmorpg.mapreader.MapReader;
import mmorpg.mapreader.RoomFile;

/**
 *
 * @author Barrionuevo Diego
 */
public class ImprovedFileMapBuildingStrategy extends MapBuildingStrategy {

    private final String pathFile;

    public ImprovedFileMapBuildingStrategy(String pathFile, float tileWidth, float tileHeight) {
        super(1, tileWidth, tileHeight);
        this.pathFile = pathFile;
    }

    @Override
    public void build(Map map) {
        this.rooms = new ArrayList<>();
        MapReader mapReader = new MapReader();
        MapFile mapFile = mapReader.buildMapFormFile(pathFile);
        int[][] mainMap = mapFile.getMap();
        for (int i = 0; i < mainMap.length; i++) {
            for (int j = 0; j < mainMap[0].length; j++) {
                RoomFile roomFile = mapFile.findRoomFile(mainMap[i][j]);
                if (roomFile != null) {
                    RoomBuildingStrategy roomBuildingStrategy = new ImprovedFileRoomBuildingStrategy(roomFile, tileWidth, tileHeight);
                    Room newRoom = new Room(i, roomBuildingStrategy);
                    newRoom.setMap(map);
                    rooms.add(newRoom);
                    //for now, when 1 is the id of room, I say its the first room
                    if (roomFile.getId() == 1) {
                        this.firstRoom = newRoom;
                    }
                }
            }
        }
        if (this.firstRoom == null) {
            this.firstRoom = rooms.get(0);
        }
    }

}
