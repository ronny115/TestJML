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
	boolean upK = false, leftK = false, rightK = false, downK = false;
	double topSpeed = 5, deltaSpeed = 0.1;
	Point2D.Double shipCenterPoint = new Point2D.Double(x,y);
	Point2D.Double firstPoint = new Point2D.Double(x,          y-sizeX/2);
	Point2D.Double secondPoint = new Point2D.Double(x-sizeY/2, y+sizeX/2);
	Point2D.Double thirdPoint = new Point2D.Double(x,          y+sizeY/3);
	Point2D.Double fourthPoint = new Point2D.Double(x+sizeY/2, y+sizeX/2);
	
	public void update(LinkedList<GameObject> object) {
		//Move forward and backwards
		shipCenterPoint = movePoint(velY,shipCenterPoint);
		firstPoint = movePoint(velY,firstPoint);
		secondPoint = movePoint(velY,secondPoint);
		thirdPoint = movePoint(velY,thirdPoint);
		fourthPoint = movePoint(velY,fourthPoint);
		//Rotate left and right
		firstPoint = rotatePoint(shipCenterPoint, velX, firstPoint);
		secondPoint = rotatePoint(shipCenterPoint, velX, secondPoint);
		thirdPoint = rotatePoint(shipCenterPoint, velX, thirdPoint);
		fourthPoint = rotatePoint(shipCenterPoint, velX, fourthPoint);
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
		g2.drawOval((int)shipCenterPoint.getX(), (int)shipCenterPoint.getY(), 1, 1);
	}

	private Point2D.Double movePoint(double speed, Point2D.Double point) {
		double xnew = point.x, ynew = point.y;
		xnew += speed * Math.sin(Math.atan2(firstPoint.x - shipCenterPoint.x, firstPoint.y - shipCenterPoint.y));
		ynew += speed * Math.cos(Math.atan2(firstPoint.x - shipCenterPoint.x, firstPoint.y - shipCenterPoint.y));
		return new Point2D.Double(xnew,ynew);	
}
	private Point2D.Double rotatePoint(Point2D.Double center, double angle, Point2D.Double point) {
		point.x -= center.x;
		point.y -= center.y;
		double xnew = point.x * Math.cos(angle) -point.y * Math.sin(angle);
		double ynew = point.x * Math.sin(angle) +point.y * Math.cos(angle);
		point.x = xnew + center.x;
		point.y = ynew + center.y;	
		return new Point2D.Double(point.x,point.y);
	}
}
