package game.prototype;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ResourceLoader {

    private BufferedImage image;
    private Font pixelFont;

    public BufferedImage loadImage(String path) {
        try {
            image = ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public Font loadFont(String path) {
        try {
            pixelFont = Font.createFont(Font.TRUETYPE_FONT,
                                        getClass().getClassLoader().getResourceAsStream("PressStart2P.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        return pixelFont;
    }
}
