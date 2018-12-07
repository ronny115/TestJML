package game.prototype.objects;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;

import java.awt.geom.Point2D.Float;
import java.util.LinkedList;

import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class TrailFx extends GameObject {
	//TODO make it better
	private Handler handler;
	private float alpha = 1;
	private float lenght;
	private Color color;
	private float width, height;

	public TrailFx(float x, float y, float w, float h, float lenght, Color color, Handler handler, ObjectId id) {
		super(x, y, w, h, id);
		this.color = color;
		this.handler = handler;
		this.width = w;
		this.height = h;
		this.lenght = lenght;
	}

	public void update(LinkedList<GameObject> object) {
		if(alpha > lenght) {
			alpha -= lenght -0.0001;	
		} else {
			handler.removeObject(this);
		}
	}

	public void render(Graphics2D g2) {
		g2.setComposite(makeTransparent(alpha));
		
		g2.setColor(color);
		g2.drawOval((int)x-3, (int)y-3, 6, 6);
	
		g2.setComposite(makeTransparent(1));	
	}

	private AlphaComposite makeTransparent(float alpha) {
		int type = AlphaComposite.SRC_OVER;
		return(AlphaComposite.getInstance(type, alpha));
	}
	
	public Float getP() {return null;}
	public boolean getCollision() {return false;}
}
