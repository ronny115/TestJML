package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	//Ship points respect the ship center point.
	private Point2D.Float center = new Point2D.Float(x, y);
	private Point2D.Float firstPoint = new Point2D.Float(center.x,        center.y-(h/2));
	private Point2D.Float secondPoint = new Point2D.Float(center.x-(w/2), center.y+(h/2));
	private Point2D.Float thirdPoint = new Point2D.Float(center.x,        center.y+(h/3));
	private Point2D.Float fourthPoint = new Point2D.Float(center.x+(w/2), center.y+(h/2));
	//Velocity Components
	//private Point2D.Float velocityComponent = new Point2D.Float(), oldPos = new Point2D.Float(), newPos = new Point2D.Float();
	//Projection on screen bounds variables.
	private float projectionXmin, projectionXmax, projectionYmin, projectionYmax;
	private boolean collide;
	float newxpoints[];
	float newypoints[];

	public void update(LinkedList<GameObject> object) {	
		CollisionVsScreenDetection();
		//oldPos = firstPoint;
		shipMovement();
		//newPos = firstPoint;
		//velocityComponent.x = newPos.x-oldPos.x;
		//velocityComponent.y = newPos.y-oldPos.y;

	}

	public void render(Graphics2D g2) {
		//Enable AA
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(ship());
		//g2.fill(ship());
		g2.drawOval((int)center.x-3, (int)center.y-3, 6, 6);
		g2.setColor(Color.RED);
		g2.draw(ship().getBounds2D());
	}
	
	private boolean CollisionVsScreenDetection() {
		int screenBoundwidth = handler.getScreenBoundsWidth();
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);		
			if(tempObject.getId() == ObjectId.PlayerShip) {
				//Collide vs. x-bounds	
				if ((projectionXmin - screenBoundwidth) < 0) {
					CollisionVsScreenReaction(-projectionXmin + screenBoundwidth, 0, 1, 0);
					//Check if reach upper left corner
					if(projectionYmin - screenBoundwidth < 0) {
						
						CollisionVsScreenReaction(0, -projectionYmin + screenBoundwidth, 0, 1);
						return true;
					}else{
						//Check if reach upper right corner
						projectionYmax = projectionYmax-Game.HEIGHT;
						if(projectionYmax + screenBoundwidth > 0) {
							CollisionVsScreenReaction(0, -projectionYmax - screenBoundwidth, 0, -1);
							return true;
						}	
					}
					return true;
				}else{
					projectionXmax = projectionXmax-Game.WIDTH;
					if(projectionXmax + screenBoundwidth > 0) {
						CollisionVsScreenReaction(-projectionXmax - screenBoundwidth, 0 , -1, 0);
						//Check if reach lower left corner
						if(projectionYmin - screenBoundwidth < 0) {
							CollisionVsScreenReaction(0, -projectionYmin + screenBoundwidth, 0, 1);
							return true;
						}else{
							//Check if reach lower right corner
							projectionYmax = projectionYmax-Game.HEIGHT;
							if(projectionYmax + screenBoundwidth > 0) {
								CollisionVsScreenReaction(0, -projectionYmax - screenBoundwidth, 0, -1);
								return true;
							}	
						}
						return true;
					}
				}
				//Collide vs. y-bounds
				if (projectionYmin - screenBoundwidth < 0) {
					CollisionVsScreenReaction(0, -projectionYmin + screenBoundwidth, 0, 1);
					return true;
				}else{
					projectionYmax = projectionYmax-Game.HEIGHT;
					if(projectionYmax + screenBoundwidth > 0) {
						CollisionVsScreenReaction(0, -projectionYmax - screenBoundwidth, 0, -1);
						return true;
					}
				}
			}
			//Collide vs. Tile bound box

			if(tempObject.getId() == ObjectId.Tile) {
				newxpoints = tempObject.getxPoints();
				newypoints =tempObject.getyPoints();
				collide = tempObject.getCollision();
				if (collide == true) {
					System.out.println(projectionXmin);
					CollisionVsTileReaction(newxpoints, newypoints);
				}
			}
		}
		return false;
	}

	private void CollisionVsScreenReaction(float px, float py, float dx, float dy) {	
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
	
	private void CollisionVsTileReaction(float x[], float y[]) {
		center.x += x[0]-newxpoints[0];
		center.y += y[0]-center.y;
		 
		firstPoint.x += x[1]-firstPoint.x;
		firstPoint.y += y[1]-firstPoint.y;
		
		secondPoint.x += x[2]-secondPoint.x;
		secondPoint.y += y[2]-secondPoint.y;
		
		thirdPoint.x += x[3]-thirdPoint.x;
		thirdPoint.y += y[3]-thirdPoint.y;
		
		fourthPoint.x += x[4]-fourthPoint.x;
		fourthPoint.y += y[4]-fourthPoint.y;
//		if (collide == true) {
//			velY=0;
//		}
		
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
		projectionXmin = (float) polygon.getBounds2D().getMinX();
		projectionXmax = (float) polygon.getBounds2D().getMaxX();
		projectionYmin = (float) polygon.getBounds2D().getMinY();
		projectionYmax = (float) polygon.getBounds2D().getMaxY();
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
