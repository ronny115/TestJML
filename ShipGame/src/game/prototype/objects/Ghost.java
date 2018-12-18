package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;
import java.util.Random;

import game.prototype.Animation;
import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class Ghost extends GameObject {
	
	private Handler handler;
	private TextureManager tex = Game.getTexInstance();
	private Animation idle;
	private Random r = new Random();
	private int choose = 0, life = 100;
	private int ex, ey;
	private long firstTime;
	
	public Ghost(float x, float y, float w, float h, Handler handler, ObjectId id) {
		super(x, y, w, h, id);
		this.handler = handler;
		ex = (int) (x + w/2);
		ey = (int) (y + h/2);
		firstTime = System.currentTimeMillis();
		idle = new Animation(3, tex.ghostIdle);
	}


	public void update(LinkedList<GameObject> object) {
		long currentTime = System.currentTimeMillis();
		final long threshold = 1000;
		
		idle.runAnimation();
		
		ex += velX;
		ey += velY;
		
		choose = r.nextInt(10);
		
		if(ex < x || ey < y || ( ex < x && ey < y) || (ex < x && ey > y + h) || ex > x + w || ey > y + h || (ex > x + w && ey < y) 
				|| (ex > x + w && ey > y + h)) {
			ex -= (int) (velX*2);
			ey -= (int) (velY*2);
			velX *=-1 ;
			velY *=-1;
		}
		
		if (choose == 0) {
			velX = (r.nextInt(8) -4);
			velY = (r.nextInt(8) -4);
			
		}

		if (currentTime - firstTime > threshold) {
			Point2D.Float[] points = new Point2D.Float[2];
			points[0] = new Point2D.Float(ex,ey);
			points[1] = handler.player.get(0).getPoints()[0];
			handler.addObject(new Projectile(ex, ey, 5, 20, 2, "enemy", Helper.projectileAngle(points), ObjectId.Projectile));
			firstTime = currentTime;
		}
		
		for(int i =0; i< handler.object.size(); i++) {
			if(handler.object.get(i).getId() == ObjectId.Projectile) {
				if(handler.object.get(i).getType() == "player") {
					System.out.println("lol");
				}
			}
		}
	}

	public void render(Graphics2D g2) {
		idle.drawAnimation(g2, (int)(ex - (w/3)/2), (int)(ey - (h/3)/2), (int)(w/3), (int)(h/3));
		g2.setColor(Color.RED);
		g2.drawRect((int)x, (int)y, (int)w, (int)h);
		g2.drawRect((int)(ex - (w/3)/2), (int)(ey - (h/3)/2), (int)(w/3), (int)(h/3));
	}

	public Float getP() {return null;}
	public boolean getCollision() {return false;}
	public String getType() {return null;}
}
