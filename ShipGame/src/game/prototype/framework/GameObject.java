package game.prototype.framework;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public abstract class GameObject {
	
	protected float x,y;
	protected ObjectId id;
	protected float velX = 0, velY = 0;
	protected float w = 0, h = 0;
	
	public GameObject(float x, float y, float w, float h, ObjectId id) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.id = id;
	}
	
	public abstract void update(LinkedList<GameObject> object);
	public abstract void render(Graphics2D g2);

	public abstract Point2D.Float deltaPoints();
	public abstract boolean collision();
	public abstract String type();
	
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
	public ObjectId getId() {
		return id;
	}

}
