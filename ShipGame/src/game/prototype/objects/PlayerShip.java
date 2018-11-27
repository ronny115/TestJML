package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.HUD;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.framework.PlayerId;
import game.prototype.framework.PlayerObject;

public class PlayerShip extends PlayerObject {

	private Handler handler;
	//Ship points respect the ship center point.
	private Point2D.Float[] shipPoints = new Point2D.Float[5];
	private Point2D.Float newP = new Point2D.Float();
	private boolean isColliding;

	
	public PlayerShip(float x, float y, float w, float h, Handler handler, PlayerId id) {
		super(x, y, w, h, id);
		this.handler = handler;
		shipPoints[0] = new Point2D.Float(x, y);
		shipPoints[1] = new Point2D.Float(x,       y-(h/2));
		shipPoints[2] = new Point2D.Float(x-(w/2), y+(h/2));
		shipPoints[3] = new Point2D.Float(x,       y+(h/3));
		shipPoints[4] = new Point2D.Float(x+(w/2), y+(h/2));
	}
	
	public void updatePlayer(LinkedList<PlayerObject> object) {	
		CollisionVsTile();
		shipMovement();
	}

	public void renderPlayer(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.setColor(Color.BLACK);
		g2.draw(ship());
		g2.drawOval((int)shipPoints[0].x-3, (int)shipPoints[0].y-3, 6, 6);

	}
	
	private void CollisionVsTile() {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);		
			if(tempObject.getId() == ObjectId.CollisionBlock) {
				newP = tempObject.getP();
				isColliding = tempObject.getCollision();
				if (isColliding == true) {
					HUD.HEALTH -= 1;
					CollisionVsTileReaction(newP);
				}
			}
		}		
	}
	
	private void CollisionVsTileReaction(Point2D.Float p) {
		shipPoints[0].x += p.x;
		shipPoints[0].y += p.y;
		 
		shipPoints[1].x += p.x;
		shipPoints[1].y += p.y;
		
		shipPoints[2].x += p.x;
		shipPoints[2].y += p.y;
		
		shipPoints[3].x += p.x;
		shipPoints[3].y += p.y;
		
		shipPoints[4].x += p.x;
		shipPoints[4].y += p.y;
	}
	
	private Path2D ship() {
		Path2D polygon = new Path2D.Double();
		float xpoints[] = new float[4];
		float ypoints[] = new float[4];	
		
		for (int i = 0; i < xpoints.length; i++) {
			xpoints[i] = shipPoints[i+1].x;
			ypoints[i] = shipPoints[i+1].y;
		}
		
		polygon.moveTo(xpoints[0], ypoints[0]);
		for(int i = 0; i < xpoints.length; ++i) {
			polygon.lineTo(xpoints[i], ypoints[i]);
		}
		polygon.closePath();
		return polygon;
	}
	
	private void shipMovement() {
		//Move forward and backward
		shipPoints[0] = movement(velY, shipPoints[0], shipPoints[0]);	
		shipPoints[1] = movement(velY, shipPoints[0], shipPoints[1]);
		shipPoints[2] = movement(velY, shipPoints[0], shipPoints[2]);
		shipPoints[3] = movement(velY, shipPoints[0], shipPoints[3]);
		shipPoints[4] = movement(velY, shipPoints[0], shipPoints[4]);

		//Rotate left and right from center
		shipPoints[1] = direction(velX, shipPoints[0], shipPoints[1]);
		shipPoints[2] = direction(velX, shipPoints[0], shipPoints[2]);
		shipPoints[3] = direction(velX, shipPoints[0], shipPoints[3]);
		shipPoints[4] = direction(velX, shipPoints[0], shipPoints[4]);

		//Update ship position
		x = shipPoints[0].x; y = shipPoints[0].y;		
	}
	
	public Point2D.Float[] getPoints() {
		Point2D.Float[] points = new Point2D.Float[4];
		for (int i = 0; i < points.length; i++) {
			points[i] = shipPoints[i+1];
			}
		return points;
	}
		
	private Point2D.Float movement(float speed, Point2D.Float center, Point2D.Float point) {
		float xnew = point.x, ynew = point.y;
		xnew += speed * Math.sin(Math.atan2(shipPoints[1].x - center.x, shipPoints[1].y - center.y));
		ynew += speed * Math.cos(Math.atan2(shipPoints[1].x - center.x, shipPoints[1].y - center.y));
		return new Point2D.Float(xnew,ynew);	
	}
	
	private Point2D.Float direction(float angle, Point2D.Float center, Point2D.Float point) {
		point.x -= center.x;
		point.y -= center.y;
		float xnew = (float) (point.x * Math.cos(angle) -point.y * Math.sin(angle));
		float ynew = (float) (point.x * Math.sin(angle) +point.y * Math.cos(angle));
		point.x = xnew + center.x;
		point.y = ynew + center.y;	
		return new Point2D.Float(point.x,point.y);
	}

}
