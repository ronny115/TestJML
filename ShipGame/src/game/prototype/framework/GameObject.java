package game.prototype.framework;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

public abstract class GameObject {
	
	protected float x,y;
	protected ObjectId id;
	protected float velX = 0, velY = 0;
	protected float sizeX = 0, sizeY = 0;
	
	public GameObject(float x, float y, float w, float h, ObjectId id) {
		this.x = x;
		this.y = y;
		this.sizeX = w;
		this.sizeY = h;
		this.id = id;
	}
	
	public abstract void update(LinkedList<GameObject> object);
	public abstract void render(Graphics2D g2);
	public abstract Ellipse2D getBounds();
	
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
	public float getSizeX() {
		return sizeX;
	}
	public float getSizeY() {
		return sizeY;
	}
	public void setSizeX(float sizeX) {
		this.sizeX = sizeX;
	}
	public void setSizeY(float sizeY) {
		this.sizeY = sizeY;
	}
	public ObjectId getId() {
		return id;
	}

}
