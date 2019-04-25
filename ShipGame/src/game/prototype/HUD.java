package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import game.prototype.framework.GameStates;
import game.prototype.framework.TextureManager;

public class HUD {

    private GameStates gs;
    private Font font;
    private TextureManager tex = Game.getTexInstance();
    private Point2D.Float hBarPos;
    
    public HUD(GameStates gs) {
        this.gs = gs;
        hBarPos = new Point2D.Float(Game.WIDTH-(Game.WIDTH-20), Game.HEIGHT - 50);
    }
    
    public void update() {
       
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(15f));
        if (gs.getPoints() == 0) {
            g2.drawString("Score:0000", 
                          Game.WIDTH - (Game.WIDTH - 20), Game.HEIGHT - (Game.HEIGHT - 30));
        } else if(gs.getPoints() < 100) {
            g2.drawString("Score:00" + gs.getPoints(), 
                          Game.WIDTH - (Game.WIDTH - 20), Game.HEIGHT - (Game.HEIGHT - 30));       
        } else if (gs.getPoints() < 1000) {
            g2.drawString("Score:0" + gs.getPoints(), 
                          Game.WIDTH - (Game.WIDTH - 20), Game.HEIGHT - (Game.HEIGHT - 30));
        } else {
            g2.drawString("Score:" + gs.getPoints(), 
                          Game.WIDTH - (Game.WIDTH - 20), Game.HEIGHT - (Game.HEIGHT - 30));
        }
        g2.drawImage(tex.player, Game.WIDTH - 70, Game.HEIGHT - (Game.HEIGHT - 10), 19, 25, null);
        g2.drawString("x" + gs.getLife(), Game.WIDTH - 50, Game.HEIGHT - (Game.HEIGHT - 30));
        if (gs.getShieldState())
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
        for (int i = 0; i < (gs.getHealth() / 10); i++) {
            Rectangle2D healthbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                                          position.y + 5, w1, h1);
            g2.draw(healthbar);
          
        }
        for (int i = 0; i < (gs.getShieldHealth() / 10); i++) {
            Rectangle2D shieldbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                                          position.y + 5, w1, h1);
            if (gs.getShieldState()) {
                g2.fill(shieldbar);
            }
        }
    }
}
