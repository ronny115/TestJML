package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class ScreenBounds extends GameObject {
	
	private int width;
	public ScreenBounds(float x, float y, float w, float h, int boundWidth, ObjectId id) {
		super(x, y, w, h, id);
		width = boundWidth;
	}

	public void update(LinkedList<GameObject> object) {	
	}

	public void render(Graphics2D g2) {
		//g2.setColor(Color.RED);
		//g2.draw(getScreenBoundTop());
		//g2.draw(getScreenBoundBottom());
		//g2.draw(getScreenBoundLeft());
		//g2.draw(getScreenBoundRight());
	}
	private Rectangle getScreenBoundTop() {
		return new Rectangle(0,0,Game.WIDTH,+width);
	}
	private Rectangle getScreenBoundBottom() {
		return new Rectangle(0,Game.HEIGHT-width,Game.WIDTH,+width);
	}
	private Rectangle getScreenBoundLeft() {
		return new Rectangle(0,width,width,Game.HEIGHT-(width*2));
	}
	private Rectangle getScreenBoundRight() {
		return new Rectangle(Game.WIDTH-width,width,width,Game.HEIGHT-(width*2));
	}

	public float[] getxPoints() {return null;}
	public float[] getyPoints() {return null;}
	public boolean getCollision() {return false;}
}
