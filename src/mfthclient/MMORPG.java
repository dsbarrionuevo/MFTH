package mfthclient;

import java.io.IOException;
import java.net.Socket;
import mfthclient.map.Map;
import mfthclient.player.Player;
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
        super("MFTH");
        AppGameContainer container = new AppGameContainer(this);
        container.setDisplayMode(800, 600, false);
        container.setShowFPS(false);
        container.start();
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        Camera.createCamera(new Vector2f(0f, 0f), container.getWidth(), container.getHeight());
        Camera.getInstance().setPadding(4);
        try {
            System.out.println("CLIENT");
            client = new Client(new Socket("localhost", 4646));

            Map map = new Map(0);
            Player player = new Player();
            //map.getCurrentRoom()
            client.setMap(map);
            client.setPlayer(player);
            new Thread(client).start();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*map = new Map(1);
         player = new Player();
         map.getCurrentRoom().addObject(player, 1, 1);
         map.getCurrentRoom().focusObject(player);
         */
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        client.update(container, delta);
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        client.render(container, g);
    }

    public static void main(String[] args) {
        try {
            new MMORPG();
        } catch (SlickException ex) {
            Logger.getLogger(MMORPG.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
