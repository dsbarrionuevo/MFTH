package mfthclient;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mfthclient.camera.Camera;
import mfthclient.client.Client;
import static mfthclient.client.Client.DEFAULT_LISTEN_UDP_PORT;
import mfthserver.server.CommunicationUtility;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

/**
 *
 * @author Diego
 */
public class MMORPG extends BasicGame {

    private Client client;

    public MMORPG() throws SlickException {
        super("Martin Fierro - Treasure Hunters");
        AppGameContainer container = new AppGameContainer(this);
        container.setDisplayMode(800, 600, false);
        container.setShowFPS(false);
        container.setAlwaysRender(true);
        container.start();
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        Camera.createCamera(new Vector2f(0f, 0f), container.getWidth(), container.getHeight());
        Camera.getInstance().setPadding(4);
        try {
            System.out.println("CLIENT");
            client = new Client(
                    new Socket(Client.DEFAULT_SERVER_HOST, Client.DEFAULT_SERVER_PORT),
                    new DatagramSocket(findFreeUDPPort(), InetAddress.getByName("localhost")));
            client.start();
        } catch (IOException ex) {
            Logger.getLogger(MMORPG.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        client.update(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        client.render(container, g);
    }

    @Override
    public boolean closeRequested() {
        System.out.println("DISCONNECT FROM SERVER");
        client.disconnect();
        System.exit(0);
        return false;
    }

    private int findFreeUDPPort() {
        int portToTry = DEFAULT_LISTEN_UDP_PORT;
        while (!CommunicationUtility.isPortAvailable(portToTry)) {
            portToTry++;
        }
        System.out.println("port UDP chosen: " + portToTry);
        return portToTry;
    }

    public static void main(String[] args) {
        try {
            new MMORPG();
        } catch (SlickException ex) {
            Logger.getLogger(MMORPG.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
