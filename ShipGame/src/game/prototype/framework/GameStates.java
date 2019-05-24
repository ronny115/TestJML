package game.prototype.framework;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GameStates {

    private int health, shield_health, points, life, shootMode;
    private float angle;
    private boolean shield_state, shield_hit, prop_state, blockCollision, shield_pickup,
                    player_hit;
    private boolean gamePaused, gameReseted, gameContinued, 
                    gameExited, gameGameOver, gameGameOn;
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

    public ArrayList<String> saveSlots() {
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
        this.shield_hit = state;
    }

    public void setShieldPickUp(boolean state) {
        this.shield_pickup = state;
    }

    public void setPropulsionState(boolean state) {
        this.prop_state = state;
    }

    public boolean getPropulsionState() {
        return prop_state;
    }

    public Point2D.Float getObjState() {
        return objectCoords;
    }

    public boolean getShieldState() {
        return shield_state;
    }

    public boolean getShieldHit() {
        return shield_hit;
    }

    public int getHealth() {
        return health;
    }

    public int getShieldHealth() {
        return shield_health;
    }

    public boolean getShieldPickUp() {
        return shield_pickup;
    }

    public int getPoints() {
        return points;
    }

    public int getLife() {
        return life;
    }
    
    public void setBlockCollision(boolean value) {
       this.blockCollision = value; 
    }
    
    public boolean getBlockCollision() {
        return blockCollision;
    }

    public void setShootMode(int n) {
        this.shootMode = n;
    }

    public int getShootMode() {
        return shootMode;
    }

    public void setAngle(float angle){
        this.angle = angle;
    }

    public float getAngle(){
        return angle;
    }

    public void setPlayerHit(boolean value){
        this.player_hit = value;
    }

    public boolean getPlayerHit(){
        return player_hit;
    }
    ////////////////
    // Game States//
    ////////////////
    public void setGamePaused(boolean value) {
        this.gamePaused = value;
    }

    public boolean getGamePaused() {
        return gamePaused;
    }

    public void setGameReseted(boolean value) {
        this.gameReseted = value;
    }

    public boolean getGameReseted() {
        return gameReseted;
    }

    public void setGameContinued(boolean value) {
        this.gameContinued = value;
    }

    public boolean getGameContinued() {
        return gameContinued;
    }

    public void setGameExited(boolean value) {
        this.gameExited = value;
    }

    public boolean getGameExited() {
        return gameExited;
    }

    public void setGameGameOver(boolean value) {
        this.gameGameOver = value;
    }

    public boolean getGameGameOver() {
        return gameGameOver;
    }

    public void setGameGameOn(boolean value) {
        this.gameGameOn = value;
    }

    public boolean getGameGameOn() {
        return gameGameOn;
    }

}
