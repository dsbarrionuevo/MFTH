package mfthclient.client;

import com.sun.javafx.geom.Vec2d;
import mfthclient.common.CommunicationLogger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mfthclient.player.Player;
import mfthclient.map.room.Room;
import org.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class Client implements Runnable {

    public static final String DEFAULT_SERVER_HOST = "localhost";
    public static final int DEFAULT_PORT = 4646;
    private static final int CLIENT_ID_INVALID = -1;
    //
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private boolean connected;
    //
    private int clientId;
    //
    private Room room;
    private Player player;

    public Client(Socket socket) {
        this.connected = true;
        this.socket = socket;
        this.clientId = CLIENT_ID_INVALID;
    }

    public void update(GameContainer container, int delta) throws SlickException {
        if (room != null) {
            room.update(container, delta);
            if (clientId != CLIENT_ID_INVALID && player != null) {
                Input input = container.getInput();
                int direction = -1;
                if (input.isKeyDown(Input.KEY_LEFT)) {
                    direction = Room.DIRECTION_WEST;
                }
                if (input.isKeyDown(Input.KEY_RIGHT)) {
                    direction = Room.DIRECTION_EAST;
                }
                if (input.isKeyDown(Input.KEY_UP)) {
                    direction = Room.DIRECTION_NORTH;
                }
                if (input.isKeyDown(Input.KEY_DOWN)) {
                    direction = Room.DIRECTION_SOUTH;
                }
                if (direction != -1) {
                    sendJson("{command:'move', client_id:" + this.clientId + ", direction:" + direction + "}");
                }
            }
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        if (room != null) {
            room.render(container, g);
        }
    }

    @Override
    public void run() {
        try {
            waitForResponse();
            introduceMyself();
            //now I hace the both channels open: to listen and speak
            while (connected) {
                String jsonCommandString = inputStream.readUTF();
                JSONObject jsonCommand = new JSONObject(jsonCommandString);
                if (jsonCommand.getString("command").equals("id_client")) {
                    //set the id for client
                    this.clientId = jsonCommand.getInt("id_client");
                    log("My client id is: " + clientId);
                } else if (jsonCommand.getString("command").equals("init")) {
                    System.out.println(jsonCommandString);
                    //create room and init player position
                    System.out.println("Building the current room");
                    //"{command:'init', id_room:" + chosenRoom.getRoomId() + ", room_source: '" + source + "', tile: {x: " + emptyTile.getTileX() + ", y: " + emptyTile.getTileY() + "} }";
                    player = new Player();
                    String roomSource = jsonCommand.getString("room_source");
                    int roomId = jsonCommand.getInt("id_room");
                    //build the room (for firest time only)...
                    room = new Room(roomId, roomSource);
                    JSONObject beginingTileJson = jsonCommand.getJSONObject("tile");
                    //assign player for room and place it on the position told by the server
                    room.addObject(player, beginingTileJson.getInt("x"), beginingTileJson.getInt("y"));
                } else if (jsonCommand.getString("command").equals("map_source")) {
                    //... have to separte the thred of listen packages to the game thread
                } else if (jsonCommand.getString("command").equals("response_move")) {
                    boolean canMove = jsonCommand.getBoolean("can_move");
                    if (canMove) {
                        //player.move(direction);
                        JSONObject position = jsonCommand.getJSONObject("position");
                        Vector2f newPosition = new Vector2f((float) position.getDouble("x"), (float) position.getDouble("y"));
                        player.getPosition().x = newPosition.x;
                        player.getPosition().y = newPosition.y;
                    }
                }/*else if(jsonCommand.getString("command").equals("id_client")){
                    
                 }*/

                System.out.println("in loop");
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            closeSocket();
        }
    }

    private void sendJson(String json) {
        try {
            this.outputStream.writeUTF(json);
            this.outputStream.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void introduceMyself() throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        String greeting = "Hello server!, I'm an ordinary client.";
        this.outputStream.writeUTF(greeting);
        this.outputStream.flush();
        System.out.println("Me: " + greeting);
    }

    private void waitForResponse() throws IOException {
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        String response = inputStream.readUTF();
        System.out.println("Server: " + response);
    }

    private void closeSocket() {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void serverLog(String message) {
        CommunicationLogger.log("Server", message);
    }

    private void log(String message) {
        CommunicationLogger.log("Me", message);
    }

}
