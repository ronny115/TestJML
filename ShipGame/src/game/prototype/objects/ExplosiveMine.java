package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import game.prototype.Animation;
import game.prototype.Game;
import game.prototype.HUD;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class ExplosiveMine extends GameObject{
	
	private Handler handler;
	private TextureManager tex = Game.getTexInstance();
	private Animation explosion;
	private boolean isHit = false;

	public ExplosiveMine(float x, float y, float w, float h, Handler handler, ObjectId id) {
		super(x, y, w, h, id);
		this.handler = handler;
		explosion = new Animation(1, tex.explosion);
	}

	public void update(LinkedList<GameObject> object) {
		if (isHit == true) explosion.runAnimationOnce();
		if (Animation.isDone == true) isHit = false;	
		projectileCollision();
	}

	public void render(Graphics2D g2) {
		g2.setColor(Color.RED);
		if (isHit == true) explosion.drawAnimation(g2, (int)x, (int)y, (int)w, (int)h);
		else g2.drawImage(tex.enemyType1[0], (int)x, (int)y, (int)w, (int)h, null);
		g2.draw(bounds());
	}
	
	public Rectangle2D bounds() {
		return new Rectangle2D.Float(x,y,w,h);
	}
	
	private void projectileCollision() {
		for (int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);				
			if (tempObject.getId() == ObjectId.Projectile) {
				Point2D.Float projectile = new Point2D.Float(tempObject.getX(), tempObject.getY());	
				if (bounds().contains(projectile) == true && isHit == false) {
					isHit = true;
					handler.removeObject(tempObject);
				} 
			}			
			if (Animation.isDone == true && isHit == false) {
				handler.removeObject(this);
				Animation.isDone = false;
			}		
		}
	}	
	
	public Float getP() {return null;}
	public boolean getCollision() {return false;}
}
