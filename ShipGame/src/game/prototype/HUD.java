package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import game.prototype.framework.Helper;

public class HUD {

    public static int HEALTH = 100;
    public static int POINTS = 0;
    private Font font;
    private Point2D.Float healthBarPos = new Point2D.Float(1025, 660);

    public void update() {
        HEALTH = Helper.clamp(HEALTH, 0, 100);
    }

    public void render(Graphics2D g2) {
        // Health Bar
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(15f));
        g2.drawString("Score:" + POINTS, 10, 25);
        healthBar(g2, healthBarPos, HEALTH);
    }

    public void setFont(Font font) {
        this.font = font;
    }

    private void healthBar(Graphics2D g2, Point2D.Float position, int health) {
        float w = 205;
        float h = 25;
        float w1 = 15, h1 = 15;
        Rectangle2D healthbar = new Rectangle2D.Float(position.x, position.y, w, h);
        g2.draw(healthbar);
        for (int i = 0; i < (HEALTH / 10); i++) {
            healthbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                              position.y + 5, w1, h1);
            g2.draw(healthbar);
        }
    }
}
