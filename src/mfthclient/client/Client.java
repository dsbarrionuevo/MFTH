package mfthclient.client;

import mfthclient.common.CommunicationLogger;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import mfthclient.player.Player;
import mfthclient.map.room.Room;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import mfthserver.server.commands.*;
import mfthserver.server.*;
import static mfthserver.server.commands.CommandFactory.*;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Barrionuevo Diego
 */
public class Client {

    public static final String DEFAULT_SERVER_HOST = "localhost";
    public static final int DEFAULT_SERVER_PORT = 4646;
    public static final int DEFAULT_LISTEN_UDP_PORT = 5656;
    private static final int CLIENT_ID_INVALID = -1;
    //
    private SocketController socketController;
    private DatagramController datagramController;
    private boolean connected;
    //
    private Room room;
    private Player player;

    public Client(Socket socket, DatagramSocket datagramSocket) {
        this.connected = true;
        this.socketController = new SocketController(socket);
        this.datagramController = new DatagramController(datagramSocket);
        this.player = new Player(CLIENT_ID_INVALID);
    }

    public void update(GameContainer container, int delta) throws SlickException {
        try {
            if (room != null) {
                room.update(container, delta);
                if (getId() != CLIENT_ID_INVALID && player != null) {
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
                        sendDatagram(new CommandMove(getId(), direction));
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void render(GameContainer container, Graphics g) throws SlickException {
        if (room != null) {
            room.render(container, g);
        }
    }

    public void start() {
        //TCP listen thread
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    socketController.handshake(SocketController.HANDSHAKE_ORDER_FIRST_LISTEN_THEN_SPEAK);
                    //set address and port of the server to send datagrams with UDP
                    datagramController.setReceptorAddress(socketController.getSocket().getInetAddress());
                    datagramController.setReceptorPort(DEFAULT_SERVER_PORT);
                    //send the port i'm listening to in UDP
                    sendPacket(new CommandNetworkInformation(datagramController.getSocket().getLocalPort()));
                    while (connected) {
                        Command command = socketController.receive();
                        switch (command.getType()) {
                            case (COMMAND_CLIENT_ID):
                                CommandSetupClientId commandSetupClientId = (CommandSetupClientId) command;
                                player.setId(commandSetupClientId.getClientId());
                                break;
                            case (COMMAND_INIT_PLAYER):
                                CommandInitPlayer commandInitPlayer = (CommandInitPlayer) command;
                                //create room and init player position
                                //??? esto ya lo hice antes...
                                player = new Player(getId());
                                //build the room (for first time only)...
                                room = new Room(commandInitPlayer.getRoomId(), commandInitPlayer.getRoomSource());
                                //is it necesary?
                                player.setRoom(room);
                                //assign player for room and place it on the position told by the server
                                room.addObject(player, commandInitPlayer.getTileX(), commandInitPlayer.getTileY());
                                //create and add all the current players in the room
                                ArrayList<mfthserver.player.Player> otherPlayers = commandInitPlayer.getPlayers();
                                for (int i = 0; i < otherPlayers.size(); i++) {
                                    Player otherPlayer = new Player(otherPlayers.get(i).getId());
                                    otherPlayer.setPosition(otherPlayers.get(i).getPosition());
                                    room.addObject(otherPlayer);
                                }
                                break;
                            case (COMMAND_MOVE_REPSONSE):
                                System.out.println("miss");
                                /*
                                 CommandMoveResponse commandMoveResponse = (CommandMoveResponse) command;
                                 if (commandMoveResponse.isCanMove()) {
                                 Player movingPlayer = room.getPlayerById(commandMoveResponse.getClientId());
                                 if (movingPlayer != null) {//that is, we are in the same room
                                 Vector2f newPosition = commandMoveResponse.getPosition();
                                 movingPlayer.getPosition().x = newPosition.x;
                                 movingPlayer.getPosition().y = newPosition.y;
                                 }
                                 }*/
                                break;
                            case (COMMAND_NEW_PLAYER):
                                CommandNewPlayer commandNewPlayer = (CommandNewPlayer) command;
                                if (commandNewPlayer.getRoomId() == room.getRoomId()) {
                                    Player newPlayer = new Player(commandNewPlayer.getClientId());
                                    //assign player for room and place it on the position told by the server
                                    room.addObject(newPlayer, commandNewPlayer.getTileX(), commandNewPlayer.getTileY());
                                }
                                break;
                            case (COMMAND_DISCONNECT_RESPONSE):
                                CommandDisconnectResponse commandDisconnectResponse = (CommandDisconnectResponse) command;
                                if (room.getRoomId() == commandDisconnectResponse.getRoomId()) {
                                    room.removeObject(room.getPlayerById(commandDisconnectResponse.getClientId()));
                                }
                                break;
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
        ).start();

        //UDP listen thread
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    //datagramController.isReady() is necesary to listen?...
                    //if (datagramController.isReady()) {
                    while (connected) {
                        Command command = datagramController.receive();
                        switch (command.getType()) {
                            case (COMMAND_MOVE_REPSONSE):
                                CommandMoveResponse commandMoveResponse = (CommandMoveResponse) command;
                                if (commandMoveResponse.isCanMove()) {
                                    Player movingPlayer = room.getPlayerById(commandMoveResponse.getClientId());
                                    if (movingPlayer != null) {//that is, we are in the same room
                                        Vector2f newPosition = commandMoveResponse.getPosition();
                                        movingPlayer.getPosition().x = newPosition.x;
                                        movingPlayer.getPosition().y = newPosition.y;
                                    }
                                }
                                break;
                        }
                    }
                    //}
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        ).start();
    }

    private void closeSocket() {
        try {
            socketController.close();
        } catch (IOException ex) {
            Logger.getLogger(Client.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void sendPacket(Command command) throws IOException {
        socketController.send(command);
    }

    public void sendDatagram(Command command) throws IOException {
        if (datagramController.isReady()) {
            datagramController.send(command);
        }
    }

    private int getId() {
        return player.getId();
    }

    public void disconnect() {
        try {
            connected = false;
            sendPacket(new CommandDisconnect(getId(), player.getRoom().getRoomId()));
            closeSocket();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
