package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import game.prototype.framework.Helper;

public class HUD {

    private int health, shield_health, points, life;
    private Font font;
    private boolean shield_state, hit_state;
    private Point2D.Float healthBarPos = new Point2D.Float(1025, 660);
    private Point2D.Float objectCoords = new Point2D.Float();
    
    public void update() {
        health = Helper.clamp(health, 0, 100);
        shield_health = Helper.clamp(shield_health, 0, 100);
    }

    public void render(Graphics2D g2) {
        // Health Bar
        g2.setColor(Color.BLACK);
        g2.setFont(font.deriveFont(15f));
        g2.drawString("Score:" + points, 10, 25);
        g2.drawString("Lifes:" + life, Game.WIDTH/2, 25);
        if(shield_state) 
            healthBar(g2, healthBarPos);
        else
            healthBar(g2, healthBarPos);        
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
        for (int i = 0; i < (health / 10); i++) {
            Rectangle2D healthbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                                          position.y + 5, w1, h1);
            g2.draw(healthbar);
          
        }
        for (int i = 0; i < (shield_health / 10); i++) {
            Rectangle2D shieldbar = new Rectangle2D.Float(position.x + 5 + (i * 20), 
                                                          position.y + 5, w1, h1);
            if (shield_state) {
                g2.fill(shieldbar);
            }
        }
    }
    
    //Setters and getters
    public void setObjState(float objCoordsX, float objCoordsY) {
        this.objectCoords.x = objCoordsX;
        this.objectCoords.y = objCoordsY;
    }
    
    public void setHealth(int health) {
        this.health += health;  
    }
    
    public void setShieldHealth(int shield_health) {
        this.shield_health += shield_health;
    }
    
    public void setPoints(int points) {
        this.points += points;  
    }
    
    public void setLife(int life) {
        this.life += life;              
    }
    
    public void setShieldState(boolean state) {
        this.shield_state = state;
    }
    
    public void setShieldHit(boolean state) {
        this.hit_state = state;
    }
    
    public Point2D.Float getObjState(){
        return objectCoords;
    }
    
    public boolean getShieldState() {
        return shield_state;
    }
    
    public boolean getShieldHit() {
        return hit_state;
    }
    
    public int getHealth() {
        return health;
    }
    
    public int getShieldHealth() {
        return shield_health;
    }
    
    public int gePoints() {
        return points;
    }
    
    public int getLife() {
        return life;
    }
}
