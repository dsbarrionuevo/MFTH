package mfthclient.client;

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

/**
 *
 * @author Barrionuevo Diego
 */
public class Client implements Runnable {

    private static final int CLIENT_ID_INVALID = -1;
    //
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
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
        }
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
                String jsonCommandString = input.readUTF();
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
                    room = new Room(roomId, roomSource);
                    JSONObject beginingTileJson = jsonCommand.getJSONObject("tile");
                    room.addObject(player, beginingTileJson.getInt("x"), beginingTileJson.getInt("y"));
                } else if (jsonCommand.getString("command").equals("map_source")) {
                    //... have to separte the thred of listen packages to the game thread
                }/*else if(jsonCommand.getString("command").equals("id_client")){
                    
                 }else if(jsonCommand.getString("command").equals("id_client")){
                    
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
            this.output.writeUTF(json);
            this.output.flush();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void introduceMyself() throws IOException {
        this.output = new ObjectOutputStream(socket.getOutputStream());
        String greeting = "Hello server!, I'm an ordinary client.";
        this.output.writeUTF(greeting);
        this.output.flush();
        System.out.println("Me: " + greeting);
    }

    private void waitForResponse() throws IOException {
        this.input = new ObjectInputStream(socket.getInputStream());
        String response = input.readUTF();
        System.out.println("Server: " + response);
    }

    private void closeSocket() {
        try {
            if (output != null) {
                output.close();
            }
            if (input != null) {
                input.close();
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
