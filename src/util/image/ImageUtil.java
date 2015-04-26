package util.image;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Barrionuevo Diego
 */
public class ImageUtil {

    /**
     * Retruns and array list with of the sprite cut from the source given the rectangel spec.
     * @param source The BufferedImage source in a spritesheet form.
     * @param model The Rectangle with the with and height of the desired sprite.
     * @return An ArrayList<> with all of the sprites from the source.
     */
    public static ArrayList<BufferedImage> getSprites(BufferedImage source, Rectangle model) {
        ArrayList<BufferedImage> sprites = new ArrayList<>();
        int x = 0, y = 0;
        int columns = (int) Math.floor((source.getWidth() / model.width));
        int rows = (int) Math.floor(source.getHeight() / (model.height));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                Rectangle currentModel = new Rectangle(x, y, model.width, model.height);
                BufferedImage sprite = cropImage(source, currentModel);
                sprites.add(sprite);
                x += model.width;
            }
            x = 0;
            y += model.height;
        }
        return sprites;
    }

    private static BufferedImage cropImage(BufferedImage source, Rectangle rect) {
        BufferedImage dest = source.getSubimage(rect.x, rect.y, rect.width, rect.height);
        return dest;
    }

    public static void main(String[] args) {
        try {
            BufferedImage original = ImageIO.read(new File("res/images/players.png"));
            ArrayList<BufferedImage> sprites = ImageUtil.getSprites(original, new Rectangle(0, 0, 32, 32));
            for (int i = 0; i < sprites.size(); i++) {
                ImageIO.write(sprites.get(i), "png", new File("res/images/players/image" + i + ".png"));
            }
        } catch (IOException ex) {
            Logger.getLogger(ImageUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
