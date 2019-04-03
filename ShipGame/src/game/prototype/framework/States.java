package game.prototype.framework;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class States {
    
    private int health, shield_health, points, life;
    private boolean shield_state, hit_state, prop_state;
    private Point2D.Float objectCoords = new Point2D.Float();
    private ArrayList<String> saveSlots = new ArrayList<String>();
    private ArrayList<int[]> colorRGB = new ArrayList<int[]>();
    private ArrayList<float[]> coords = new ArrayList<float[]>();
    
    public void update() {
        health = (Helper.clamp(health, 0, 100));
        shield_health = (Helper.clamp(shield_health, 0, 100));
    }
    
    public void setLevel(ArrayList<int[]> colorRGB, ArrayList<float[]> coords) {
        this.colorRGB = colorRGB;
        this.coords = coords;
    }
    
    public ArrayList<String> saveSlots(){
        return saveSlots;
    }
    
    public void setSaveSlots(ArrayList<String> saveSlots) {
        this.saveSlots = saveSlots;
    }
    
    public ArrayList<int[]> levelRGB() {
        return colorRGB;
    }
    
    public ArrayList<float[]> levelObjCoords() {
        return coords;
    }

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
        this.points = points;  
    }
    
    public void setLife(int life) {
        this.life = life;              
    }
    
    public void setShieldState(boolean state) {
        this.shield_state = state;
    }
    
    public void setShieldHit(boolean state) {
        this.hit_state = state;
    }
    
    public void setPropulsionState(boolean state) {
        this.prop_state = state;
    }
    
    public boolean getPropulsionState() {
        return prop_state;
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
    
    public int getPoints() {
        return points;
    }
    
    public int getLife() {
        return life;
    }

}
