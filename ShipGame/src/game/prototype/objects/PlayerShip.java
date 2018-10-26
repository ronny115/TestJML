package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class PlayerShip extends GameObject {

	public PlayerShip(float x, float y, float w, float h, Handler handler, ObjectId id) {
		super(x, y, w, h, id);
		this.handler = handler;
	}
	
	private Handler handler;
	//Bound circle dimension
	private float ellipse = w;
	//Ship points respect the ship center point.
	private Point2D.Float center = new Point2D.Float(x, y);
	private Point2D.Float firstPoint = new Point2D.Float(center.x,        center.y-(h/2));
	private Point2D.Float secondPoint = new Point2D.Float(center.x-(w/2), center.y+(h/2));
	private Point2D.Float thirdPoint = new Point2D.Float(center.x,        center.y+(h/3));
	private Point2D.Float fourthPoint = new Point2D.Float(center.x+(w/2), center.y+(h/2));
	//Point for bound circle
	private Point2D.Float boundCircleCenter = center;
	private Point2D.Float velComp = new Point2D.Float(), oldPos = new Point2D.Float(), newPos = new Point2D.Float();
	private float xmin, xmax, ymin, ymax;

	public void update(LinkedList<GameObject> object) {
		//Move forward and backwards	
		if (CollisionVsScreen(object) == false) {

			oldPos = firstPoint;
			shipMovement();
			newPos = firstPoint;
			velComp.x = newPos.x-oldPos.x;
			velComp.y = newPos.y-oldPos.y;
		}else{

			shipMovement();
		}
	}

	public void render(Graphics2D g2) {
		//Enable AA
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(ship());
		//g2.draw(ship().getBounds2D());
		//g2.fill(ship());
		g2.drawOval((int)center.x-3, (int)center.y-3, 6, 6);	
		g2.setColor(Color.RED);
		g2.draw(getBounds());
	}
	
	private boolean CollisionVsScreen(LinkedList<GameObject> object) {
		int w = handler.getScreenBoundsWidth();
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);		
			if(tempObject.getId() == ObjectId.PlayerShip) {
				//collide vs. x-bounds	
				if ((xmin-w) < 0) {
					CollisionVsScreenReaction(-xmin+w, 0, 1, 0);
					//Check if reach corner
					if(ymin-w < 0) {
						
						CollisionVsScreenReaction(0, -ymin+w, 0, 1);
						return true;
					}else{
						ymax = ymax-Game.HEIGHT;
						if(ymax+w > 0) {
							CollisionVsScreenReaction(0, -ymax-w, 0, -1);
							return true;
						}	
					}
					return true;
				}else{
					xmax = xmax-Game.WIDTH;
					if(xmax+w > 0) {
						CollisionVsScreenReaction(-xmax-w, 0 , -1, 0);
						//Check if reach corner
						if(ymin-w < 0) {
							CollisionVsScreenReaction(0, -ymin+w, 0, 1);
							return true;
						}else{
							ymax = ymax-Game.HEIGHT;
							if(ymax+w > 0) {
								CollisionVsScreenReaction(0, -ymax-w, 0, -1);
								return true;
							}	
						}
						return true;
					}
				}
				//collide vs. y-bounds
				if (ymin-w < 0) {
					CollisionVsScreenReaction(0, -ymin+w, 0, 1);
					return true;
				}else{
					ymax = ymax-Game.HEIGHT;
					if(ymax+w > 0) {
						CollisionVsScreenReaction(0, -ymax-w, 0, -1);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Ellipse2D getBounds() {
		return new Ellipse2D.Float(boundCircleCenter.x-ellipse/2, boundCircleCenter.y-ellipse/2, ellipse, ellipse);
	}
	
	private void CollisionVsScreenReaction(float px, float py, float dx, float dy) {	
		boundCircleCenter.x += px;
		boundCircleCenter.y += py;
		 
		firstPoint.x += px;
		firstPoint.y += py;
		
		secondPoint.x += px;
		secondPoint.y += py;
		
		thirdPoint.x += px;
		thirdPoint.y += py;
		
		fourthPoint.x += px;
		fourthPoint.y += py;
		
		center = boundCircleCenter;
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
		//Get the min and max X and Y values for the projections on the screen bounds
		xmin = (float) polygon.getBounds2D().getMinX();
		xmax = (float) polygon.getBounds2D().getMaxX();
		ymin = (float) polygon.getBounds2D().getMinY();
		ymax = (float) polygon.getBounds2D().getMaxY();
		return polygon;
	}
	
	private void shipMovement() {
		//Move forward and backward
		center = movement(velY, center, center);
		
		firstPoint = movement(velY, center, firstPoint);
		secondPoint = movement(velY, center, secondPoint);
		thirdPoint = movement(velY, center, thirdPoint);
		fourthPoint = movement(velY, center, fourthPoint);
	
		boundCircleCenter = movement(velY,center, boundCircleCenter);

		//Rotate left and right from center
		firstPoint = direction(velX, center, firstPoint);
		secondPoint = direction(velX, center, secondPoint);
		thirdPoint = direction(velX, center, thirdPoint);
		fourthPoint = direction(velX, center, fourthPoint);

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
}
