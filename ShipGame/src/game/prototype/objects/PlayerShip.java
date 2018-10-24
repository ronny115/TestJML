package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
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
	private double ellipse = sizeX;
	//Ship points respect the ship center point.
	private Point2D.Double center = new Point2D.Double(x, y);
	private Point2D.Double firstPoint = new Point2D.Double(center.x,            center.y-(sizeY/2));
	private Point2D.Double secondPoint = new Point2D.Double(center.x-(sizeX/2), center.y+(sizeY/2));
	private Point2D.Double thirdPoint = new Point2D.Double(center.x,            center.y+(sizeY/3));
	private Point2D.Double fourthPoint = new Point2D.Double(center.x+(sizeX/2), center.y+(sizeY/2));
	//Point for bound circle
	private Point2D.Double boundCircleCenter = center;
	private Point2D.Double velComp = new Point2D.Double(), oldPos = new Point2D.Double(), newPos = new Point2D.Double();

	
	public void update(LinkedList<GameObject> object) {
		//Move forward and backwards
	
		if (CollisionVsScreen(object) == false) {
			shipMovement();
		}else{
			oldPos = firstPoint;
			shipMovement();
			newPos = firstPoint;
			velComp.x = newPos.x-oldPos.x;
			velComp.y = newPos.y-oldPos.y;
		}
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
		g2.draw(getBounds());
	}
	
	private boolean CollisionVsScreen(LinkedList<GameObject> object) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.PlayerShip) {
				double r = ellipse/2;
				double dx = -(boundCircleCenter.x - r);
				double dy = -(boundCircleCenter.y - r);
				//collide vs. x-bounds	
				if (0<dx) {
					CollisionVsScreenReaction(dx, 0, 1, 0, boundCircleCenter);
					center = boundCircleCenter;
					//Check if reach corner
					if(0<dy) {
						CollisionVsScreenReaction(0, dy, 0, 1, boundCircleCenter);
						center = boundCircleCenter;
						return true;
					}else{
						dy = (boundCircleCenter.y + r)-Game.HEIGHT;
						if(0<dy) {
							CollisionVsScreenReaction(0, -dy, 0, -1, boundCircleCenter);
							center = boundCircleCenter;
							return true;
						}	
					}
					return true;
				}else{
					dx = (boundCircleCenter.x + r)-Game.WIDTH;
					if(0<dx) {
						CollisionVsScreenReaction(-dx, 0 , -1, 0, boundCircleCenter);
						center = boundCircleCenter;
						//Check if reach corner
						if(0<dy) {
							CollisionVsScreenReaction(0, dy, 0, 1, boundCircleCenter);
							center = boundCircleCenter;
							return true;
						}else{
							dy = (boundCircleCenter.y + r)-Game.HEIGHT;
							if(0<dy) {
								CollisionVsScreenReaction(0, -dy, 0, -1, boundCircleCenter);
								center = boundCircleCenter;
								return true;
							}	
						}
						return true;
					}
				}
				//collide vs. y-bounds
				if (0<dy) {
					CollisionVsScreenReaction(0, dy, 0, 1, boundCircleCenter);
					center = boundCircleCenter;
					return true;
				}else{
					dy = (boundCircleCenter.y + r)-Game.HEIGHT;
					if(0<dy) {
						CollisionVsScreenReaction(0, -dy, 0, -1, boundCircleCenter);
						center = boundCircleCenter;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Ellipse2D getBounds() {
		return new Ellipse2D.Double(boundCircleCenter.x-ellipse/2, boundCircleCenter.y-ellipse/2, ellipse, ellipse);
	}
	
	private void CollisionVsScreenReaction(double px, double py, double dx, double dy, Point2D.Double position) {

		double vx = velComp.x;
		double vy = velComp.y;
		double dp = (vx*dx + vy*dy);//project velocity onto collision normal
		 
		double nx = dp*dx;//nx,ny is normal velocity
		double ny = dp*dy;
			
		double tx = vx-nx;//px,py is tangent velocity
		double ty = vy-ny;

		//we only want to apply collision response forces if the object is travelling into, and not out of, the collision
		double b,bx,by,f,fx,fy;
		 
		if(dp < 0)
			{
				f = 0.05;
				fx = tx*f;
				fy = ty*f;		
				
				b = 1+0.3;//this bounce constant should be elsewhere, i.e inside the object/tile/etc..
				
				bx = nx*b;
				by = ny*b;	
			}else{
				//moving out of collision, do not apply forces
				bx = by = fx = fy = 0;
			}
		
		position.x += px;
		position.y += py;
		 
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
		double xpoints[] = {firstPoint.x,secondPoint.x,thirdPoint.x,fourthPoint.x};
		double ypoints[] = {firstPoint.y,secondPoint.y,thirdPoint.y,fourthPoint.y};
		Path2D polygon = new Path2D.Double();
		
		polygon.moveTo(xpoints[0], ypoints[0]);
		for(int i = 1; i < xpoints.length; ++i) {
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
	
		boundCircleCenter = movement(velY,center, boundCircleCenter);

		//Rotate left and right
		firstPoint = direction(velX, center, firstPoint);
		secondPoint = direction(velX, center, secondPoint);
		thirdPoint = direction(velX, center, thirdPoint);
		fourthPoint = direction(velX, center, fourthPoint);

		boundCircleCenter = direction(velX,center, boundCircleCenter);
	}
	
	private Point2D.Double movement(double speed, Point2D.Double center, Point2D.Double point) {

		double xnew = point.x, ynew = point.y;
		xnew += speed * Math.sin(Math.atan2(firstPoint.x - center.x, firstPoint.y - center.y));
		ynew += speed * Math.cos(Math.atan2(firstPoint.x - center.x, firstPoint.y - center.y));
		return new Point2D.Double(xnew,ynew);	
	}
	
	private Point2D.Double direction(double angle, Point2D.Double center, Point2D.Double point) {
		point.x -= center.x;
		point.y -= center.y;
		double xnew = point.x * Math.cos(angle) -point.y * Math.sin(angle);
		double ynew = point.x * Math.sin(angle) +point.y * Math.cos(angle);
		point.x = xnew + center.x;
		point.y = ynew + center.y;	
		return new Point2D.Double(point.x,point.y);
	}
}
