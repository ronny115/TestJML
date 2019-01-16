package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import game.prototype.framework.Helper;
import game.prototype.objects.Shield;

public class HUD {

    public static int HEALTH = 100;
    public static int SHIELD_HEALTH = 100;
    public static int POINTS = 0;
    public static int LIFE = 3;
    private Font font;
    private Point2D.Float healthBarPos = new Point2D.Float(1025, 660);

    public void update() {
        HEALTH = Helper.clamp(HEALTH, 0, 100);
        SHIELD_HEALTH = Helper.clamp(SHIELD_HEALTH, 0, 100);
    }

    public void render(Graphics2D g2) {
        // Health Bar
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(15f));
        g2.drawString("Score:" + POINTS, 10, 25);
        g2.drawString("Lifes:" + LIFE, Game.WIDTH/2, 25);
        if(Shield.SHIELD) 
            healthBar(g2, healthBarPos, SHIELD_HEALTH);
        else
            healthBar(g2, healthBarPos, HEALTH);        
    }

    public void setFont(Font font) {
        this.font = font;
    }

    private void healthBar(Graphics2D g2, Point2D.Float position, int health) {
        //Health Box
        float w = 205, h = 25;
        //Health/shield
        float w1 = 15, h1 = 15;
        Rectangle2D healthbox = new Rectangle2D.Float(position.x, position.y, w, h);
        g2.draw(healthbox);
        for (int i = 0; i < (HEALTH / 10); i++) {
            Rectangle2D healthbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                                          position.y + 5, w1, h1);
            g2.draw(healthbar);
          
        }
        for (int i = 0; i < (SHIELD_HEALTH / 10); i++) {
            Rectangle2D shieldbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                                          position.y + 5, w1, h1);
            if (Shield.SHIELD == true) {
                g2.fill(shieldbar);
            }
        }
    }
}
