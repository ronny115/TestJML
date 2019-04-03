package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import game.prototype.framework.States;

public class HUD {

    private States states;
    private Font font;
    private Point2D.Float hBarPos = new Point2D.Float(1025, 660);
    
    public HUD(States s) {
        this.states = s;
    }
    
    public void update() {
       
    }

    public void render(Graphics2D g2) {
        // Health Bar
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(15f));
        g2.drawString("Score:" + states.getPoints(), 10, 25);
        g2.drawString("Lifes:" + states.getLife(), Game.WIDTH/2, 25);
        if(states.getShieldState()) 
            healthBar(g2, hBarPos);
        else
            healthBar(g2, hBarPos);        
    }

    public void setFont(Font font) {
        this.font = font;
    }

    private void healthBar(Graphics2D g2, Point2D.Float position) {
        //Health Box
        float w = 205, h = 25;
        //Health/shield
        float w1 = 15, h1 = 15;
        Rectangle2D healthbox = new Rectangle2D.Float(position.x, position.y, w, h);
        g2.draw(healthbox);
        for (int i = 0; i < (states.getHealth() / 10); i++) {
            Rectangle2D healthbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                                          position.y + 5, w1, h1);
            g2.draw(healthbar);
          
        }
        for (int i = 0; i < (states.getShieldHealth() / 10); i++) {
            Rectangle2D shieldbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                                          position.y + 5, w1, h1);
            if (states.getShieldState()) {
                g2.fill(shieldbar);
            }
        }
    }
}
