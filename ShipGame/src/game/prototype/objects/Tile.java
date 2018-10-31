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
	private Point2D.Float A = new Point2D.Float(400,400);
	private Point2D.Float B = new Point2D.Float(606,606);
	private Point2D.Float C = new Point2D.Float();
	private Point2D.Float D = new Point2D.Float();
	private Point2D.Float E = new Point2D.Float();
	private Point2D.Float X = new Point2D.Float((float)(-300* Math.cos(angle(A.x,A.y,B.x,B.y))+A.x),(float)(300* Math.sin(angle(A.x,A.y,B.x,B.y)))+A.y);
	private Point2D.Float vecAB = new Point2D.Float();
	private Point2D.Float vecAC = new Point2D.Float();
	private Point2D.Float vecXA = new Point2D.Float();
	private Point2D.Float vecXC = new Point2D.Float();
	private boolean collide = false;

	public void update(LinkedList<GameObject> object) {
		
	}
	
	

	public void render(Graphics2D g2) {
		//Enable AA
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);	
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(tile());
		//AB
		g2.setColor(Color.red);
		g2.drawLine((int)A.x,(int)A.y, (int)B.x, (int)B.y);
		g2.setColor(Color.blue);
		//AC
		//angle(A.x,A.y,B.x,B.y);
		g2.drawLine((int)A.x,(int)A.y,(int)(X.x), (int)(X.y));
		g2.setColor(Color.green);
		//AD
		g2.drawLine((int)A.x,(int)A.y, (int)(D.x), (int)(D.y));
		g2.drawLine((int)A.x,(int)A.y, (int)(E.x), (int)(E.y));
		
		g2.setColor(Color.orange);
		//CD
		//g2.drawLine((int)C.x,(int)C.y, (int)(D.x), (int)(D.y));
		CollisionVsTileDetection(g2);
		//g2.fill(ship());
		//g2.drawLine((int)xpoints[3],(int)ypoints[3], (int)(E.x), (int)(E.y));
		//prueba(g2);
		g2.setColor(Color.BLACK);
		
	}
	
	private void CollisionVsTileDetection(Graphics2D g2) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);			
			if(tempObject.getId() == ObjectId.PlayerShip) {
				xpoints = tempObject.getxPoints();
				ypoints = tempObject.getyPoints();
				float projectionOnAB_points_x[] = new float[xpoints.length];
				float projectionOnAB_points_y[] = new float[xpoints.length];
				float projectionOnAX_points_x[] = new float[xpoints.length];
				float projectionOnAX_points_y[] = new float[xpoints.length];
				float ship_points_x[] = new float[xpoints.length];
				float ship_points_y[] = new float[xpoints.length];
				for(int j = 0; j<xpoints.length;j++) {
					C.x = (float) xpoints[j];
					C.y = (float) ypoints[j];
					//Projection on vecAB
					vecAB = handler.vectorize(A, B);
					vecAC = handler.vectorize(A, C);
					D = projection(vecAB,vecAC);
					D.x = D.x + A.x;
					D.y = D.y + A.y;
					//Projection on vecAX
					vecXA = handler.vectorize(X, A);
					vecXC = handler.vectorize(X, C);
					E = projection(vecXA, vecXC);
					E.x = E.x + X.x;
					E.y = E.y + X.y;
					
					
					
					projectionOnAX_points_x[j] = E.x;
					projectionOnAX_points_y[j] = E.y;
					
					projectionOnAB_points_x[j] = D.x;
					projectionOnAB_points_y[j] = D.y;
					
					ship_points_x[j] = C.x;
					ship_points_y[j] = C.y;
					
					g2.drawLine((int)C.x,(int)C.y, (int)(E.x), (int)(E.y));
					
				}
				
				//TODO on a function
				float max_projectionAB_value_x = projectionOnAB_points_x[0];
				float max_projectionAB_value_y = projectionOnAB_points_y[0];
				float min_projectionAB_value_x = projectionOnAB_points_x[0];
				float min_projectionAB_value_y = projectionOnAB_points_y[0];
				
				float max_projectionAX_value_x = projectionOnAX_points_x[0];
				float max_projectionAX_value_y = projectionOnAX_points_y[0];
				float min_projectionAX_value_x = projectionOnAX_points_x[0];
				float min_projectionAX_value_y = projectionOnAX_points_y[0];
				
				float max_ship_value_x = ship_points_x[0];
				float max_ship_value_y = ship_points_y[0];
				float min_ship_value_x = ship_points_x[0];
				float min_ship_value_y = ship_points_y[0];
				
				float max_ship_value_x_AX = ship_points_x[0];
				float max_ship_value_y_AX = ship_points_y[0];
				float min_ship_value_x_AX = ship_points_x[0];
				float min_ship_value_y_AX = ship_points_y[0];
				
				
				for(int j = 0; j<projectionOnAB_points_x.length;j++) {
					//Max Values on AB
					if (projectionOnAB_points_x[j] > max_projectionAB_value_x) {
						max_projectionAB_value_x = projectionOnAB_points_x[j];
						max_ship_value_x = ship_points_x[j];
					}
					if (projectionOnAB_points_y[j] > max_projectionAB_value_y) {
						max_projectionAB_value_y = projectionOnAB_points_y[j];
						max_ship_value_y = ship_points_y[j];
					}
					//Min Values on AB
					if (projectionOnAB_points_x[j] < min_projectionAB_value_x) {
						min_projectionAB_value_x = projectionOnAB_points_x[j];
						min_ship_value_x = ship_points_x[j];
					}
					if (projectionOnAB_points_y[j] < min_projectionAB_value_y) {
						min_projectionAB_value_y = projectionOnAB_points_y[j];
						min_ship_value_y = ship_points_y[j];
					}
				}
				

				g2.drawLine((int)(max_ship_value_x),(int)(max_ship_value_y), (int)(max_projectionAB_value_x), (int)(max_projectionAB_value_y));
				g2.drawLine((int)(min_ship_value_x),(int)(min_ship_value_y), (int)(min_projectionAB_value_x), (int)(min_projectionAB_value_y));
						
			}
		}
	}
	
	private float angle(float x1, float y1, float x2 ,float y2) {
		float xdiff = x1-x2;
		float ydiff = y1-y2;
		float atan = (float) Math.atan2(ydiff, xdiff);
		return atan;
	}
	
	private Float projection(Point2D.Float vec1, Point2D.Float vec2) {	
		float d = handler.dotProduct(vec1, vec1);
		float dp = handler.dotProduct(vec1, vec2);
		float multiplier = dp/d;	
		float Dx = vec1.x * multiplier;
		float Dy = vec1.y * multiplier;		
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

	public float[] getxPoints() {return xpoints;}
	public float[] getyPoints() {return ypoints;}
	public boolean getCollision() {return collide;}
}
