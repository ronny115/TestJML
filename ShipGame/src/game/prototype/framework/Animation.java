package game.prototype.framework;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Animation {

    private int frames;
    private int index = 0, count = 0, playCount = 0;
    public boolean isDone = false, hasStarted = false;
    private BufferedImage[] images;
    private BufferedImage currentImg;

    public Animation(BufferedImage[] args) {
        
        images = args;
        for (int i = 0; i < args.length; i++)
            images[i] = args[i];

        frames = args.length;
    }

    public void runAnimation(int speed) {
        index++;
        if (index > speed) {
            index = 0;
            nextFrame();
        }
    }

    public void runAnimationOnce(int speed) {
        if (playCount < frames) {
            hasStarted = true;
            index++;
            if (index > speed) {
                index = 0;
                nextFrame();
                playCount++;
            }
        } else {
            isDone = true;
            hasStarted = false;
            playCount = 0;
        }
    }

    public void nextFrame() {
        currentImg = images[count % frames];
        count++;
    }

    public void drawAnimation(Graphics2D g2, int x, int y) {
        g2.drawImage(currentImg, x, y, null);
    }

    public void drawAnimation(Graphics2D g2, int x, int y, int scalex, int scaley) {
        g2.drawImage(currentImg, x, y, scalex, scaley, null);
    }
    
    public void drawAnimation(Graphics2D g2, AffineTransform at) {
        g2.drawImage(currentImg, at, null);
    }
}
