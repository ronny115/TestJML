package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.LinkedList;

import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class CollisionBlock extends GameObject {

	public CollisionBlock(float x, float y, float w, float h, Handler handler, ObjectId id) {
		super(x, y, w, h, id);
		this.handler = handler;
		buildHexagonBB();
	}

	Handler handler;
		
	float[] bound_x = new float[6];
	float[] bound_y = new float[6];
	float[] px = new float[1];
	float[] py = new float[1];
	
	private Point2D.Float minProjected = new Point2D.Float();
	private Point2D.Float maxProjected = new Point2D.Float();
	private Point2D.Float maxShip = new Point2D.Float();
	private Point2D.Float minShip = new Point2D.Float();	
	private Point2D.Float segmentPointA = new Point2D.Float();
	private Point2D.Float segmentPointB = new Point2D.Float();
	private Point2D.Float X1,X2,X3,X4,X5,X6;	
	private boolean isColliding = false;
	
	public void update(LinkedList<GameObject> object) {
		CollisionVsTile();
	}
	
	public void render(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.draw(tile());
	}
	
	private void CollisionVsTile() {
		float segmentDist, projDist;
		boolean insideBB = false;
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);			
			if(tempObject.getId() == ObjectId.PlayerShip) {
				for(int j = 0; j<tempObject.getxPoints().length; j++) {
					//Check if any point of the ship is inside the tile box
					if(tempObject.getxPoints()[j] > (x-w-(tempObject.getSizeX()/2)) && tempObject.getxPoints()[j] < (x+w+(tempObject.getSizeX()/2)) &&
					   tempObject.getyPoints()[j] > (y-h) && tempObject.getyPoints()[j] < (y+h)) {
						insideBB = true;
						break;
					} else {
						insideBB = false;
					}
				}
				for(int j = 0; j<tempObject.getxPoints().length; j++) {
					//If any point of the ship is inside the tile box, then set the collisions
					if(insideBB == true) {
						//Segment 0-1
						if(tempObject.getxPoints()[j] > bound_x[1] && tempObject.getyPoints()[j] > bound_y[0] && tempObject.getyPoints()[j] < bound_y[1]) {
							segmentPointA.x = bound_x[0]; segmentPointA.y = bound_y[0];
							segmentPointB.x = bound_x[1]; segmentPointB.y = bound_y[1];
							
							getProjectionPoints(segmentPointA, segmentPointB, X1, "right", tempObject);
							segmentDist = distance(segmentPointA,segmentPointB);
							projDist = distance(minShip,minProjected);
							//Collision goes here
							if(minProjected.x < segmentPointA.x) {
								isColliding = true;
								if(minShip.y < segmentPointA.y - tempObject.getSizeX()/2) {
									isColliding = false;
								}
							}
							if (minProjected.x > segmentPointA.x || segmentDist+tempObject.getSizeX()/2 < projDist) {
								isColliding = false;
							}
							px[0] = segmentPointA.x - minProjected.x;
							py[0] = segmentPointA.y - minProjected.y;
						}
						//Segment 1-2
						if(tempObject.getxPoints()[j] < bound_x[1] && tempObject.getyPoints()[j] > bound_y[1] && tempObject.getxPoints()[j] > bound_x[2]) {
							segmentPointA.x = bound_x[1]; segmentPointA.y = bound_y[1];
							segmentPointB.x = bound_x[2]; segmentPointB.y = bound_y[2];
							
							getProjectionPoints(segmentPointA, segmentPointB, X2, "right", tempObject);
							projDist = distance(minShip,minProjected);
							//Collision goes here
							if(minProjected.y < segmentPointA.y) {
								isColliding = true;
								if(maxShip.x > segmentPointA.x + tempObject.getSizeX()/2) {
									isColliding = false;
								}
								if(maxShip.x < segmentPointB.x - tempObject.getSizeX()/2) {
									isColliding = false;
								}
							}
							if (minProjected.y > segmentPointA.y) {
								isColliding = false;
							} 
							px[0] = segmentPointA.x - minProjected.x;
							py[0] = segmentPointA.y - minProjected.y;
						}
						//Segment 2-3
						if(tempObject.getxPoints()[j] < bound_x[2] && tempObject.getyPoints()[j] < bound_y[2] && tempObject.getyPoints()[j] > bound_y[3]) {
							segmentPointA.x = bound_x[2]; segmentPointA.y = bound_y[2];
							segmentPointB.x = bound_x[3]; segmentPointB.y = bound_y[3];
							
							getProjectionPoints(segmentPointA, segmentPointB, X3, "reverse", tempObject);
							segmentDist = distance(segmentPointA,segmentPointB);
							projDist = distance(maxShip,maxProjected);
							//Collision goes here
							if (maxProjected.x > segmentPointA.x) {
								isColliding = true;
								if(maxShip.y > segmentPointA.y + tempObject.getSizeX()/2) {
									isColliding = false;
								}
							}
							if(maxProjected.x < segmentPointA.x || segmentDist+tempObject.getSizeX()/2 < projDist) {
								isColliding = false;
							}
							px[0] = segmentPointA.x - maxProjected.x;
							py[0] = segmentPointA.y - maxProjected.y;
						}
						//Segment 3-4
						if(tempObject.getxPoints()[j] < bound_x[4] && tempObject.getyPoints()[j] < bound_y[3] && tempObject.getyPoints()[j] > bound_y[4]) {
							segmentPointA.x = bound_x[3]; segmentPointA.y = bound_y[3];	
							segmentPointB.x = bound_x[4]; segmentPointB.y = bound_y[4];
							
							getProjectionPoints(segmentPointA, segmentPointB, X4, "right", tempObject);
							segmentDist = distance(segmentPointA,segmentPointB);
							projDist = distance(maxShip,maxProjected);
							//Collision goes here
							if (maxProjected.x > segmentPointA.x) {
								isColliding = true;
								if(maxShip.y > segmentPointA.y + tempObject.getSizeX()/2) {
									isColliding = false;
								}
							} 
							if(maxProjected.x < segmentPointA.x || segmentDist+tempObject.getSizeX()/2 < projDist) {
								isColliding = false;
							}
							px[0] = segmentPointA.x - maxProjected.x;
							py[0] = segmentPointA.y - maxProjected.y;		
						}
						//Segment 4-5
						if(tempObject.getyPoints()[j] < bound_y[4] && tempObject.getxPoints()[j] < bound_x[5] && tempObject.getxPoints()[j] > bound_x[4]) {
							segmentPointA.x = bound_x[4]; segmentPointA.y = bound_y[4];	
							segmentPointB.x = bound_x[5]; segmentPointB.y = bound_y[5];
							
							getProjectionPoints(segmentPointA, segmentPointB, X5, "reverse", tempObject);
							projDist = distance(minShip,minProjected);
							//Collision goes here
							if (minProjected.y > segmentPointA.y) {
								isColliding = true;
								if(minShip.x < segmentPointA.x - tempObject.getSizeX()/2) {
									isColliding = false;
								}
								if(minShip.x > segmentPointB.x + tempObject.getSizeX()/2) {
									isColliding = false;
								}
							}
							if(minProjected.y < segmentPointA.y) {
								isColliding = false;
							}
							px[0] = segmentPointA.x - minProjected.x;
							py[0] = segmentPointA.y - minProjected.y;				
						}
						//Segment 5-0
						if(tempObject.getxPoints()[j] > bound_x[5] && tempObject.getyPoints()[j] > bound_y[5] && tempObject.getyPoints()[j] < bound_y[0] ) {
							segmentPointA.x = bound_x[5]; segmentPointA.y = bound_y[5];
							segmentPointB.x = bound_x[0]; segmentPointB.y = bound_y[0];
							
							getProjectionPoints(segmentPointA, segmentPointB, X6, "reverse", tempObject);
							segmentDist = distance(segmentPointA,segmentPointB);
							projDist = distance(minShip,minProjected);
							//Collision goes here
							if(minProjected.x < segmentPointA.x) {
								isColliding = true;
								if(minShip.y < segmentPointA.y - tempObject.getSizeX()/2) {
									isColliding = false;
								}
							}
							if (minProjected.x > segmentPointA.x || segmentDist+(tempObject.getSizeX()/2) < projDist) {
								isColliding = false;
							}
							px[0] = segmentPointA.x - minProjected.x; 
							py[0] = segmentPointA.y - minProjected.y;
						}
					} else {
						isColliding = false;
					}
				}
			}
		}
	}
	
	private void getProjectionPoints(Point2D.Float A, Point2D.Float B, Point2D.Float X, String dir, GameObject ship) {
		Point2D.Float shipPointsC = new Point2D.Float();
		Point2D.Float projectionOnPerpendicularE = new Point2D.Float();
		float[]	px = {0,0,0,0};  float[] py = {0,0,0,0};
		float[]	sx = {0,0,0,0};  float[] sy = {0,0,0,0};
		
		for(int j = 0; j<ship.getxPoints().length;j++) {
			shipPointsC.x = ship.getxPoints()[j];
			shipPointsC.y = ship.getyPoints()[j];
			
			projectionOnPerpendicularE = projection(X,A,shipPointsC);
			projectionOnPerpendicularE.x = projectionOnPerpendicularE.x + X.x;
			projectionOnPerpendicularE.y = projectionOnPerpendicularE.y + X.y;				
			
			px[j] = projectionOnPerpendicularE.x;
			py[j] = projectionOnPerpendicularE.y;
								
			sx[j] = ship.getxPoints()[j];
			sy[j] = ship.getyPoints()[j];
		}		
		maxProjected = getMaxValue(px,py,sx,sy,dir)[0];
		maxShip = getMaxValue(px,py,sx,sy,dir)[1];	
		minProjected = getMinValue(px,py,sx,sy,dir)[0];
		minShip = getMinValue(px,py,sx,sy,dir)[1];
	}
	
	private void buildHexagonBB() {
		X1=X2=X3=X4=X5=X6 = new Point2D.Float();
		ArrayList<float[]> points = new ArrayList<float[]>();
		float[] coords = new float[6];
		//for drawing purposes
		float len = 0.5f;
		
		for(PathIterator path = tile().getPathIterator(null); !path.isDone(); path.next()) {	
			path.currentSegment(coords);
			float[] pathpoints = {coords[0], coords[1]};
			points.add(pathpoints);		
		}	
		for(int i = 0; i < 6; i++) {
			bound_x[i] = points.get(i)[0];
			bound_y[i] = points.get(i)[1];
		}		
		X1 = getPerpendicular(bound_x[0],bound_y[0],bound_x[1],bound_y[1], len);
		X2 = getPerpendicular(bound_x[1],bound_y[1],bound_x[2],bound_y[2], len);
		X3 = getPerpendicular(bound_x[2],bound_y[2],bound_x[3],bound_y[3], len);
		X4 = getPerpendicular(bound_x[3],bound_y[3],bound_x[4],bound_y[4], len);
		X5 = getPerpendicular(bound_x[4],bound_y[4],bound_x[5],bound_y[5], len);
		X6 = getPerpendicular(bound_x[5],bound_y[5],bound_x[0],bound_y[0], len);	
	}
	
	private Float[] getMaxValue(float[] points_x, float[] points_y, float[] shipPoints_x, float[] shipPoints_y, String dir) {
		Point2D.Float MaxValue[] = new Point2D.Float[2];
		Point2D.Float ValueP = new Point2D.Float(points_x[0],points_y[0]);
		Point2D.Float ValueS = new Point2D.Float(shipPoints_x[0],shipPoints_y[0]);
		MaxValue[0] = ValueP; 
		MaxValue[1] = ValueS;

		for(int i = 0; i < points_x.length; i++) {	
			switch(dir) {
				case "right":
					if (points_x[i] > MaxValue[0].x) {
						MaxValue[0].x = points_x[i];
						MaxValue[1].x = shipPoints_x[i];
					}
					if (points_y[i] > MaxValue[0].y) {
						MaxValue[0].y = points_y[i];
						MaxValue[1].y = shipPoints_y[i];
					}
					break;
				case "reverse":
					if (points_x[i] > MaxValue[0].x) {
						MaxValue[0].x = points_x[i];
						MaxValue[1].x = shipPoints_x[i];
					}
					if (points_y[i] < MaxValue[0].y) {
						MaxValue[0].y = points_y[i];
						MaxValue[1].y = shipPoints_y[i];
					}
					break;
			}			
		}
		return MaxValue;
	}
	
	private Float[] getMinValue(float[] points_x, float[] points_y, float[] shipPoints_x, float[] shipPoints_y, String dir) {
		Point2D.Float MinValue[] = new Point2D.Float[2];
		Point2D.Float ValueP = new Point2D.Float(points_x[0],points_y[0]);
		Point2D.Float ValueS = new Point2D.Float(shipPoints_x[0],shipPoints_y[0]);
		MinValue[0] = ValueP; 
		MinValue[1] = ValueS;

		for(int i = 0; i < points_x.length; i++) {
			switch(dir) {
				case "right":
					if (points_x[i] < MinValue[0].x) {
						MinValue[0].x = points_x[i];
						MinValue[1].x = shipPoints_x[i];
					}
					if (points_y[i] < MinValue[0].y) {
						MinValue[0].y = points_y[i];
						MinValue[1].y = shipPoints_y[i];
					}
					break;
				case "reverse":
					if (points_x[i] < MinValue[0].x) {
						MinValue[0].x = points_x[i];
						MinValue[1].x = shipPoints_x[i];
					}
					if(points_y[i] > MinValue[0].y) {
						MinValue[0].y = points_y[i];
						MinValue[1].y = shipPoints_y[i];
					}
					break;
			}				
		}
		return MinValue;
	}
	
	private Point2D.Float getPerpendicular(float x1, float y1, float x2 ,float y2,float length) {
		Point2D.Float pointA = new Point2D.Float();
		Point2D.Float pointB = new Point2D.Float();
		pointA.x = x1;     pointA.y = y1;
		pointB.x = x2;     pointB.y = y2;
		Point2D.Float vector = handler.vectorize(pointB, pointA);
		Point2D.Float pVector = new Point2D.Float();
		pVector.x = vector.y;    pVector.y = -vector.x;
		float x = x1 - pVector.x * length;
		float y = y1 - pVector.y * length;
		return new Point2D.Float(x,y);
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
	
	private float distance(Point2D.Float a, Point2D.Float b) {
		float d = (float) Math.sqrt(((b.x-a.x)*(b.x-a.x))+((b.y-a.y)*(b.y-a.y)));
		return  d;
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
	
	public float[] getxPoints() {return px;}
	public float[] getyPoints() {return py;}
	public boolean getCollision() {return isColliding;}
}
