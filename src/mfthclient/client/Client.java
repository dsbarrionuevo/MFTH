package mfthclient.client;

import mfthclient.common.CommunicationLogger;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mfthclient.map.Map;
import mfthclient.player.Player;
import org.json.JSONObject;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
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
    private Map map;
    private Player player;

    public Client(Socket socket) {
        this.connected = true;
        this.socket = socket;
        this.clientId = CLIENT_ID_INVALID;
    }

    public void update(GameContainer container, int delta) throws SlickException {
        if (map != null) {
            map.getCurrentRoom().update(container, delta);
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        if (map != null) {
            map.render(container, g);
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
                    this.clientId = jsonCommand.getInt("id_client");
                    log("My client id is: " + clientId);
                } else if (jsonCommand.getString("command").equals("init")) {
                    System.out.println("Building the map and room");
                    //"{command:'init', id_room:" + chosenRoom.getRoomId() + ", tile: {x: " + emptyTile.getTileX() + ", y: " + emptyTile.getTileY() + "} }"
                    //map = new Map(0);
                    map.changeRoom(jsonCommand.getInt("id_room"));
                    //player = new Player();
                    JSONObject beginingTileJson = jsonCommand.getJSONObject("tile");
                    map.getCurrentRoom().addObject(player, beginingTileJson.getInt("x"), beginingTileJson.getInt("y"));
                }/*else if(jsonCommand.getString("command").equals("id_client")){
                    
                 }else if(jsonCommand.getString("command").equals("id_client")){
                    
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

    public void setMap(Map map) {
        this.map = map;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

}
