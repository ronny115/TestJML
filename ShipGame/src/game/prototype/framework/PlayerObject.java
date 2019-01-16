package game.prototype.framework;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public abstract class PlayerObject {

    protected float x, y;
    protected PlayerId id;
    protected float velX = 0, velY = 0;
    protected float w = 0, h = 0;
    protected boolean isOut = false;

    public PlayerObject(float x, float y, float w, float h, PlayerId id) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.id = id;
    }

    public abstract void updatePlayer(LinkedList<PlayerObject> object);

    public abstract void renderPlayer(Graphics2D g2);

    public abstract Point2D.Float[] points();
    
    public abstract boolean state();

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public void setVelX(float velX) {
        this.velX = velX;
    }

    public void setVelY(float velY) {
        this.velY = velY;
    }

    public float getW() {
        return w;
    }

    public float getH() {
        return h;
    }

    public void setW(float sizeX) {
        this.w = sizeX;
    }

    public void setH(float sizeY) {
        this.h = sizeY;
    }

    public PlayerId getId() {
        return id;
    }
}
