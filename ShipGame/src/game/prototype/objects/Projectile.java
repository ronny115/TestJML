package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class Projectile extends GameObject {
	private TextureManager tex = Game.getTexInstance();
	private int speed;
	private float angle;
	
	public Projectile(float x, float y, float w, float h, int speed, float angle, ObjectId id) {
		super(x, y, w, h, id);
		this.speed = speed;
		this.angle = angle;
	}

	public void update(LinkedList<GameObject> object) {
		x += (float) speed * Math.sin(angle);
		y -= (float) speed * Math.cos(angle);
	}

	public void render(Graphics2D g2) {
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.rotate(angle);
	    at.translate(-(w/2), -(h/2));
	    at.scale((w/tex.bullet.getWidth()), (h/tex.bullet.getHeight()));
		g2.drawImage(tex.bullet, at, null);
	}
	
	public Float getP() {return null;}
	public boolean getCollision() {return false;}
}
