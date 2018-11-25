package game.prototype.framework;

import java.awt.Graphics2D;
import java.util.LinkedList;

public abstract class PlayerObject {
	
	protected float x,y;
	protected PlayerId id;
	protected float velX = 0, velY = 0;
	protected float w = 0, h = 0;
	
	public PlayerObject(float x, float y, float w, float h, PlayerId id) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.id = id;
	}
	
	public abstract void updatePlayer(LinkedList<PlayerObject> object);
	public abstract void renderPlayer(Graphics2D g2);

	public abstract float[] getxPoints();
	public abstract float[] getyPoints();

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
		return w;
	}
	public float getSizeY() {
		return h;
	}
	public void setSizeX(float sizeX) {
		this.w = sizeX;
	}
	public void setSizeY(float sizeY) {
		this.h = sizeY;
	}
	public PlayerId getId() {
		return id;
	}


}