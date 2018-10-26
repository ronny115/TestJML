package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;

import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class Tile extends GameObject {

	public Tile(float x, float y, float w, float h,Handler handler, ObjectId id) {
		super(x, y, w, h, id);
		this.handler = handler;
	}

	Handler handler;
	
	private Ellipse2D playerBound = new Ellipse2D.Double();
	private Point2D.Float A = new Point2D.Float(100,100);
	private Point2D.Float B = new Point2D.Float(250,250);
	private Point2D.Float C = new Point2D.Float();
	private Point2D.Float D = new Point2D.Float();
	private Point2D.Float vecAB = new Point2D.Float();
	private Point2D.Float vecAC = new Point2D.Float();

	public void update(LinkedList<GameObject> object) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);			
			if(tempObject.getId() == ObjectId.PlayerShip) {
				playerBound = tempObject.getBounds();
				C.x = (float) playerBound.getCenterX();
				C.y = (float) playerBound.getCenterY();
				vecAB = handler.vectorize(A, B);
				vecAC = handler.vectorize(A, C);
				D = projection(vecAB,vecAC);
			}
		}

	}
	
	

	public void render(Graphics2D g2) {
		//Enable AA
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(tile());
		//AB
		g2.drawLine((int)A.x,(int)A.y, (int)B.x, (int)B.y);
		g2.setColor(Color.blue);
		//AC
		g2.drawLine((int)A.x,(int)A.y,(int)playerBound.getCenterX(), (int)playerBound.getCenterY());
		g2.setColor(Color.green);
		//AD
		g2.drawLine((int)A.x,(int)A.y, (int)(D.x+A.x), (int)(D.y+A.y));
		g2.setColor(Color.orange);
		//CD
		g2.drawLine((int)playerBound.getCenterX(),(int)playerBound.getCenterY(), (int)(D.x+A.x), (int)(D.y+A.y));
		//g2.fill(ship());
		g2.setColor(Color.BLACK);
		
	}

	private Float projection(Point2D.Float vec1, Point2D.Float vec2) {
		
		float d = handler.dotProduct(vec1, vec1);
		float dp = handler.dotProduct(vec1, vec2);
		float multiplier = dp/d;
		
		float Dx = vec1.x * multiplier;
		float Dy = vec1.y * multiplier;
			
		return new Point2D.Float(Dx,Dy);		
	}

	public Ellipse2D getBounds() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Path2D tile() {
		Point2D.Double center = new Point2D.Double(x,y);
		Point2D.Double radius = new Point2D.Double(w,h);
		double xpoints[] = new double[6];
		double ypoints[] = new double[6];
		Path2D polygon= new Path2D.Double();
		for (int i = 0; i < 6; i++) {
			xpoints[i] = center.x + radius.x * Math.cos(i*2*Math.PI/6);
			ypoints[i] = center.y + radius.y * Math.sin(i*2*Math.PI/6);
		}
		polygon.moveTo(xpoints[0], ypoints[0]);
		for(int i = 1; i < xpoints.length; ++i) {
			polygon.lineTo(xpoints[i], ypoints[i]);
		}
		polygon.closePath();
		return polygon;
	}

}
