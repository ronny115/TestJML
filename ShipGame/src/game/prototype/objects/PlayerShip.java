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
	
	private Point2D.Double center = new Point2D.Double(x,y);
	//Ship points respect the ship center point.
	private Point2D.Double firstPoint = new Point2D.Double(x,            y-(sizeY/2));
	private Point2D.Double secondPoint = new Point2D.Double(x-(sizeX/2), y+(sizeY/2));
	private Point2D.Double thirdPoint = new Point2D.Double(x,            y+(sizeY/3));
	private Point2D.Double fourthPoint = new Point2D.Double(x+(sizeX/2), y+(sizeY/2));
	//Points for bound circles
	private Point2D.Double boundTopCenter = new Point2D.Double(x,                y-(sizeY/3));
	private Point2D.Double boundLeftCenter = new Point2D.Double(x-(sizeX*0.37),  y+(sizeY/5*2));
	private Point2D.Double boundRightCenter = new Point2D.Double(x+(sizeX*0.37), y+(sizeY/5*2));
	//Bound circle dimension
	private double ellipse = sizeX/4;
	
	public void update(LinkedList<GameObject> object) {
		//Move forward and backwards
		if (CollisionVsScreen(object) == false) {
			shipMovement();
		}else{
			shipMovement();
		}
	}

	public void render(Graphics2D g2) {
		
		double xpoints[] = {firstPoint.x,secondPoint.x,thirdPoint.x,fourthPoint.x};
		double ypoints[] = {firstPoint.y,secondPoint.y,thirdPoint.y,fourthPoint.y};
		Path2D polygon = new Path2D.Double();
		
		polygon.moveTo(xpoints[0], ypoints[0]);
		for(int i = 1; i < xpoints.length; ++i) {
			polygon.lineTo(xpoints[i], ypoints[i]);
		}
		polygon.closePath();
		//Enable AA
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(polygon);
		//g2.fill(polygon);
		g2.drawOval((int)center.x-3, (int)center.y-3, 6, 6);	
		g2.setColor(Color.RED);
		g2.draw(getBounds());
		g2.draw(getBoundsLeft());
		g2.draw(getBoundsRight());
	}
	
	private boolean CollisionVsScreen(LinkedList<GameObject> object) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.PlayerShip) {
				double r = ellipse/2;
				//collide vs. x-bounds
				double top_dx = 0 - (boundTopCenter.x - r);
				double left_dx = 0 - (boundLeftCenter.x - r);
				double right_dx = 0 - (boundRightCenter.x - r);
				if (0<top_dx) {
					//TODO Physics HERE
					velY -=5;
					return true;
				}else{
					top_dx = (boundTopCenter.x + r)-Game.WIDTH;
					if(0<top_dx) {
						//TODO Physics HERE
						velY -=5;
						return true;
					}
				}
				if(0<left_dx) {
					//TODO Physics HERE
					velX -= 0.15;
					return true;
				}else{
					left_dx = (boundLeftCenter.x + r)-Game.WIDTH;
					if(0<left_dx) {
						//TODO Physics HERE
						velX -= 0.15;
						return true;
					}
				}
				if(0<right_dx) {
					//TODO Physics HERE
					velX += 0.15;
					return true;
				}else{
					right_dx = (boundRightCenter.x + r)-Game.WIDTH;
					if(0<right_dx) {
						//TODO Physics HERE
						velX += 0.15;
						return true;
					}
				}
				//collide vs. y-bounds
				double top_dy = 0 - (boundTopCenter.y - r);
				double left_dy = 0 - (boundLeftCenter.y - r);
				double right_dy = 0 - (boundRightCenter.y - r);
				if (0<top_dy) {
					//TODO Physics HERE
					velY -=5;
					return true;
				}else{
					top_dy = (boundTopCenter.y + r)-Game.HEIGHT;
					if(0<top_dy) {
						//TODO Physics HERE
						velY -=5;
						return true;
					}
				}
				if (0<left_dy) {
					//TODO Physics HERE
					velX -= 0.15;
					return true;
				}else{
					left_dy = (boundLeftCenter.y + r)-Game.HEIGHT;
					if(0<left_dy) {
						//TODO Physics HERE
						velX -= 0.15;
						return true;
					}
				}
				if (0<right_dy) {
					//TODO Physics HERE
					velX += 0.15;
					return true;
				}else{
					right_dy = (boundRightCenter.y + r)-Game.HEIGHT;
					if(0<right_dy) {
						//TODO Physics HERE
						velX += 0.15;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Ellipse2D getBounds() {
		return new Ellipse2D.Double(boundTopCenter.x-ellipse/2, boundTopCenter.y-ellipse/2, ellipse, ellipse);
	}
	public Ellipse2D getBoundsLeft() {
		return new Ellipse2D.Double(boundLeftCenter.x-ellipse/2, boundLeftCenter.y-ellipse/2, ellipse, ellipse);
	}
	public Ellipse2D getBoundsRight() {
		return new Ellipse2D.Double(boundRightCenter.x-ellipse/2, boundRightCenter.y-ellipse/2, ellipse, ellipse);
	}

	private void shipMovement() {
		center = movePoint(velY, center, center);
		
		firstPoint = movePoint(velY, center, firstPoint);
		secondPoint = movePoint(velY, center, secondPoint);
		thirdPoint = movePoint(velY, center, thirdPoint);
		fourthPoint = movePoint(velY, center, fourthPoint);
		
		boundTopCenter = movePoint(velY,center, boundTopCenter);
		boundLeftCenter = movePoint(velY,center, boundLeftCenter);
		boundRightCenter = movePoint(velY,center, boundRightCenter);
		//Rotate left and right
		firstPoint = rotatePoint(velX, center, firstPoint);
		secondPoint = rotatePoint(velX, center, secondPoint);
		thirdPoint = rotatePoint(velX, center, thirdPoint);
		fourthPoint = rotatePoint(velX, center, fourthPoint);
		
		boundTopCenter = rotatePoint(velX,center, boundTopCenter);
		boundLeftCenter = rotatePoint(velX,center, boundLeftCenter);
		boundRightCenter = rotatePoint(velX,center, boundRightCenter);		
	}
	private Point2D.Double movePoint(double speed, Point2D.Double center, Point2D.Double point) {
		double xnew = point.x, ynew = point.y;
		xnew += speed * Math.sin(Math.atan2(firstPoint.x - center.x, firstPoint.y - center.y));
		ynew += speed * Math.cos(Math.atan2(firstPoint.x - center.x, firstPoint.y - center.y));
		return new Point2D.Double(xnew,ynew);	
	}
	private Point2D.Double rotatePoint(double angle, Point2D.Double center, Point2D.Double point) {
		point.x -= center.x;
		point.y -= center.y;
		double xnew = point.x * Math.cos(angle) -point.y * Math.sin(angle);
		double ynew = point.x * Math.sin(angle) +point.y * Math.cos(angle);
		point.x = xnew + center.x;
		point.y = ynew + center.y;	
		return new Point2D.Double(point.x,point.y);
	}
}
