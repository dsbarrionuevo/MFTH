package mmorpg;

import java.util.ArrayList;
import mmorpg.debug.TimedMessage;
import mmorpg.map.Map;
import mmorpg.items.Treasure;
import mmorpg.player.Player;
import java.util.logging.Level;
import java.util.logging.Logger;
import mmorpg.camera.Camera;
import mmorpg.enemies.Enemy;
import mmorpg.enemies.SmartWallEnemy;
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

    private Map map;
    private Treasure treasure;
    private Player player;
    private ArrayList<Enemy> enemies;
    //
    private TimedMessage message;

    public MMORPG() throws SlickException {
        super("MMORPG");
        AppGameContainer container = new AppGameContainer(this);
        container.setDisplayMode(800, 600, false);
        container.setShowFPS(false);
        container.start();
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        Camera.createCamera(new Vector2f(0f, 0f), container.getWidth(), container.getHeight());
        map = new Map(3);
        player = new Player();
        map.placeObject(player, 12, 1);
        /*
         treasure = new Treasure();
         map.placeObject(treasure, 14, 10);*/
        enemies = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            Enemy newEnemy = new SmartWallEnemy();
            ((SmartWallEnemy) newEnemy).setRoom(map.getCurrentRoom());
            map.placeObject(newEnemy,
                    ((int) (Math.random() * 14) + 1),
                    ((int) (Math.random() * 10) + 1));
            enemies.add(newEnemy);
        }
        //
        message = new TimedMessage(new Vector2f(10f, 10f), "Welcome");
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        player.update(container, delta);
        for (Enemy enemy : enemies) {
            enemy.update(container, delta);
            if (player.collide(enemy)) {
                message.changeMessage("Player got killed by enemy...");
            }

            //            for (Enemy otherEnemy : enemies) {
            //                if (enemy != otherEnemy) {
            //                    if (enemy.collide(otherEnemy)) {
            //                        ((WallEnemy)enemy).changeToOppositeDirection();
            //                        ((WallEnemy)otherEnemy).changeToOppositeDirection();
            //                    }
            //                }
            //            }
        }
        /*
         //
         if (player.collide(treasure)) {
         message.changeMessage("Player took the treasure!");
         }*/
        //
        message.update(delta);

    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        map.render(container, g);
        player.render(container, g);
        /*treasure.render(container, g);*/
        for (Enemy enemy : enemies) {
            enemy.render(container, g);
        }
        message.render(container, g);
    }

    public static void main(String[] args) {
        try {
            new MMORPG();
        } catch (SlickException ex) {
            Logger.getLogger(MMORPG.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
