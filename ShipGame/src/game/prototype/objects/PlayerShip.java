package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class PlayerShip extends GameObject {

	public PlayerShip(float x, float y, float w, float h, Handler handler, ObjectId id) {
		super(x, y, w, h, id);
		this.handler = handler;
	}
	
	private Handler handler;
	//Ship points respect the ship center point.
	private Point2D.Float center = new Point2D.Float(x, y);
	private Point2D.Float firstPoint = new Point2D.Float(center.x,        center.y-(h/2));
	private Point2D.Float secondPoint = new Point2D.Float(center.x-(w/2), center.y+(h/2));
	private Point2D.Float thirdPoint = new Point2D.Float(center.x,        center.y+(h/3));
	private Point2D.Float fourthPoint = new Point2D.Float(center.x+(w/2), center.y+(h/2));

	private boolean isColliding;
	float newpx, newpy;

	public void update(LinkedList<GameObject> object) {	
		CollisionVsTile();
		shipMovement();
	}

	public void render(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.draw(ship());
		g2.drawOval((int)center.x-3, (int)center.y-3, 6, 6);
	}
	
	private void CollisionVsTile() {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);		
			if(tempObject.getId() == ObjectId.CollisionBlock) {
				newpx = tempObject.getxPoints()[0];
				newpy = tempObject.getyPoints()[0];
				isColliding = tempObject.getCollision();
				if (isColliding == true) {
					CollisionVsTileReaction(newpx, newpy);
				}
			}
		}		
	}
	
	private void CollisionVsTileReaction(float px, float py) {
		center.x += px;
		center.y += py;
		 
		firstPoint.x += px;
		firstPoint.y += py;
		
		secondPoint.x += px;
		secondPoint.y += py;
		
		thirdPoint.x += px;
		thirdPoint.y += py;
		
		fourthPoint.x += px;
		fourthPoint.y += py;
	}
	
	private Path2D ship() {
		float xpoints[] = {firstPoint.x,secondPoint.x,thirdPoint.x,fourthPoint.x};
		float ypoints[] = {firstPoint.y,secondPoint.y,thirdPoint.y,fourthPoint.y};
		Path2D polygon = new Path2D.Double();
		
		polygon.moveTo(xpoints[0], ypoints[0]);
		for(int i = 0; i < xpoints.length; ++i) {
			polygon.lineTo(xpoints[i], ypoints[i]);
		}
		polygon.closePath();
		return polygon;
	}
	
	private void shipMovement() {
		//Move forward and backward
		center = movement(velY, center, center);
		
		firstPoint = movement(velY, center, firstPoint);
		secondPoint = movement(velY, center, secondPoint);
		thirdPoint = movement(velY, center, thirdPoint);
		fourthPoint = movement(velY, center, fourthPoint);

		//Rotate left and right from center
		firstPoint = direction(velX, center, firstPoint);
		secondPoint = direction(velX, center, secondPoint);
		thirdPoint = direction(velX, center, thirdPoint);
		fourthPoint = direction(velX, center, fourthPoint);
		
		//Update ship position
		x = center.x; y = center.y;
	}
	
	public float[] getxPoints() {
		float xpoints[] = {firstPoint.x,secondPoint.x,thirdPoint.x,fourthPoint.x};
		return xpoints;
	}
	
	public float[] getyPoints() {
		float ypoints[] = {firstPoint.y,secondPoint.y,thirdPoint.y,fourthPoint.y};
		return ypoints;
	}
	
	private Point2D.Float movement(float speed, Point2D.Float center, Point2D.Float point) {
		float xnew = point.x, ynew = point.y;
		xnew += speed * Math.sin(Math.atan2(firstPoint.x - center.x, firstPoint.y - center.y));
		ynew += speed * Math.cos(Math.atan2(firstPoint.x - center.x, firstPoint.y - center.y));
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
	public boolean getCollision() {return false;}
}
