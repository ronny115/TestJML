package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	
	private float[] xpoints, ypoints;
	private Point2D.Float minProjected = new Point2D.Float();
	private Point2D.Float maxProjected = new Point2D.Float();
	private Point2D.Float minProjectedAB = new Point2D.Float();
	private Point2D.Float maxProjectedAB = new Point2D.Float();
	private Point2D.Float A = new Point2D.Float(400,400);
	private Point2D.Float B = new Point2D.Float(600,600);
	private Point2D.Float C = new Point2D.Float();
	private Point2D.Float D = new Point2D.Float();
	private Point2D.Float E = new Point2D.Float();
	private Point2D.Float X = new Point2D.Float((float)(-300* Math.cos(angle(A.x,A.y,B.x,B.y))+A.x),(float)(300* Math.sin(angle(A.x,A.y,B.x,B.y)))+A.y);

	private boolean isColliding = false;

	public void update(LinkedList<GameObject> object) {
		
	}
	
	public void render(Graphics2D g2) {
		//Enable AA
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);	
		g2.setColor(Color.BLACK);
		g2.draw(tile());

		g2.setColor(Color.red);
		g2.drawLine((int)A.x,(int)A.y, (int)B.x, (int)B.y);
		g2.setColor(Color.blue);

		g2.drawLine((int)A.x,(int)A.y,(int)(X.x), (int)(X.y));
		g2.setColor(Color.orange);

		CollisionVsTileDetection(g2);

		g2.setColor(Color.BLACK);
		
	}
	
	private void CollisionVsTileDetection(Graphics2D g2) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);			
			if(tempObject.getId() == ObjectId.PlayerShip) {
				
				xpoints = tempObject.getxPoints();
				ypoints = tempObject.getyPoints();
				//px and py are the arrays of ship points projected
				float px[] = new float[xpoints.length], py[] = new float[xpoints.length];
				float px1[] = new float[xpoints.length], py1[] = new float[xpoints.length];
				//sx and sy are the arrays of ship points
				float sx[] = new float[xpoints.length], sy[] = new float[xpoints.length]; 
				
				for(int j = 0; j<xpoints.length;j++) {
					C.x = xpoints[j];
					C.y = ypoints[j];
					
					D = projection(A,B,C);
					D.x = D.x + A.x;
					D.y = D.y + A.y;		

					E = projection(X,A,C);
					E.x = E.x + X.x;
					E.y = E.y + X.y;				
					
					px1[j] = D.x;
					py1[j] = D.y;
					
					px[j] = E.x;
					py[j] = E.y;
										
					sx[j] = C.x;
					sy[j] = C.y;
					
				}

				maxProjected = getMaxValue(px, py, sx, sy)[0];
				Point2D.Float maxShip = getMaxValue(px, py, sx, sy)[1];
				
				minProjected = getMinValue(px, py, sx, sy)[0];
				minProjectedAB = getMinValue(px1, py1, sx, sy)[0];
				maxProjectedAB = getMaxValue(px1, py1, sx, sy)[0];
				Point2D.Float minShip = getMinValue(px, py, sx, sy)[1];
				
				//TODO Adjust the collision boundaries
				
				if(minProjected.x < A.x && minProjectedAB.y > A.y && maxProjectedAB.y < B.y) {
					isColliding = true;	
				}else {
					isColliding = false;
				}
				if (maxProjected.x < A.x){
					isColliding = false;
				}

				g2.drawLine((int)(maxShip.x),(int)(maxShip.y), (int)(maxProjected.x), (int)(maxProjected.y));
				g2.drawLine((int)(minShip.x),(int)(minShip.y), (int)(minProjected.x), (int)(minProjected.y));
					
			}
		}
	}
	
	
	private Float[] getMaxValue(float[] points_x, float[] points_y, float[] shipPoints_x, float[] shipPoints_y) {

		Point2D.Float MaxValue[] = new Point2D.Float[2];
		Point2D.Float ValueP = new Point2D.Float(points_x[0],points_y[0]);
		Point2D.Float ValueS = new Point2D.Float(shipPoints_x[0],shipPoints_y[0]);
	
		MaxValue[0] = ValueP; 
		MaxValue[1] = ValueS;

		for(int i = 0; i < points_x.length; i++) {
			if (points_x[i] > MaxValue[0].x) {
				MaxValue[0].x = points_x[i];
				MaxValue[1].x = shipPoints_x[i];
			}
			if (points_y[i] < MaxValue[0].y) {
				MaxValue[0].y = points_y[i];
				MaxValue[1].y = shipPoints_y[i];
			}
			
		}
		return MaxValue;
	}
	
	private Float[] getMinValue(float[] points_x, float[] points_y, float[] shipPoints_x, float[] shipPoints_y) {

		Point2D.Float MinValue[] = new Point2D.Float[2];
		Point2D.Float ValueP = new Point2D.Float(points_x[0],points_y[0]);
		Point2D.Float ValueS = new Point2D.Float(shipPoints_x[0],shipPoints_y[0]);
	
		MinValue[0] = ValueP; 
		MinValue[1] = ValueS;

		for(int i = 0; i < points_x.length; i++) {
			if (points_x[i] < MinValue[0].x) {
				MinValue[0].x = points_x[i];
				MinValue[1].x = shipPoints_x[i];
			}
			if (points_y[i] > MinValue[0].y) {
				MinValue[0].y = points_y[i];
				MinValue[1].y = shipPoints_y[i];
			}
			
		}
		return MinValue;
	}
	
	private float angle(float x1, float y1, float x2 ,float y2) {
		float xdiff = x1-x2;
		float ydiff = y1-y2;
		float atan = (float) Math.atan2(ydiff, xdiff);
		return atan;
	}
	
	private Float projection(Point2D.Float A, Point2D.Float B, Point2D.Float C) {
		Point2D.Float vector1 = handler.vectorize(A,B);
		Point2D.Float vector2 = handler.vectorize(A,C);
		float d = handler.dotProduct(vector1, vector1);
		float dp = handler.dotProduct(vector1, vector2);
		float multiplier = dp/d;	
		float Dx = vector1.x * multiplier;
		float Dy = vector1.y * multiplier;		
		return new Point2D.Float(Dx,Dy);		
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

	public float[] getxPoints() {
		float[] px = new float[1];
		px[0] = minProjected.x-A.x;
		return px;
		}
	public float[] getyPoints() {
		float[] py = new float[1];
		py[0] = minProjected.y-A.y;
		return py;
		}
	public boolean getCollision() {return isColliding;}
}
