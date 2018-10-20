package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class PlayerShip extends GameObject {

	public PlayerShip(float x, float y, float w, float h, ObjectId id) {
		super(x, y, w, h, id);
	}
	//Ship points respect the ship center point.
	Point2D.Double center = new Point2D.Double(x,y);
	Point2D.Double firstPoint = new Point2D.Double(x,          y-sizeX/2);
	Point2D.Double secondPoint = new Point2D.Double(x-sizeY/2, y+sizeX/2);
	Point2D.Double thirdPoint = new Point2D.Double(x,          y+sizeY/3);
	Point2D.Double fourthPoint = new Point2D.Double(x+sizeY/2, y+sizeX/2);
	
	public void update(LinkedList<GameObject> object) {
		//Move forward and backwards
		center = movePoint(velY, center, center);
		firstPoint = movePoint(velY, center, firstPoint);
		secondPoint = movePoint(velY, center, secondPoint);
		thirdPoint = movePoint(velY, center, thirdPoint);
		fourthPoint = movePoint(velY, center, fourthPoint);
		//Rotate left and right
		firstPoint = rotatePoint(velX, center, firstPoint);
		secondPoint = rotatePoint(velX, center, secondPoint);
		thirdPoint = rotatePoint(velX, center, thirdPoint);
		fourthPoint = rotatePoint(velX, center, fourthPoint);
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
		//TODO draw a better ship
		//Enable AA
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(polygon);
		//g2.fill(polygon);
		g2.setColor(Color.RED);
		g2.drawOval((int)center.x, (int)center.y, 1, 1);
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
