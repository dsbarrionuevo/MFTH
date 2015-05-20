package mfthclient;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mfthclient.camera.Camera;
import mfthclient.client.Client;
import mfthclient.client.Main;
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
            client = new Client(new Socket(Client.DEFAULT_SERVER_HOST, Client.DEFAULT_PORT));
            new Thread(client).start();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
        System.out.println("DESCONNECT FROM SERVER");
        client.disconnect();
        System.exit(0); // Use this if you want to quit the app.
        return false;
    }

    public static void main(String[] args) {
        try {
            new MMORPG();
        } catch (SlickException ex) {
            Logger.getLogger(MMORPG.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
