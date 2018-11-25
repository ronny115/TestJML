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
import game.prototype.framework.PlayerId;
import game.prototype.framework.PlayerObject;

public class CollisionBlock extends GameObject {

	private Handler handler;
	private Path2D poly = new Path2D.Double();

	private float[] bound_x = new float[6];
	private float[] bound_y = new float[6];
	private float[] px = new float[1];
	private float[] py = new float[1];
	
	private float ipx, ipy;
	private Point2D.Float shipPoints[];
	
	private Point2D.Float minProjected = new Point2D.Float();
	private Point2D.Float maxProjected = new Point2D.Float();
	private Point2D.Float maxShip = new Point2D.Float();
	private Point2D.Float minShip = new Point2D.Float();	
	private Point2D.Float segmentPointA = new Point2D.Float();
	private Point2D.Float segmentPointB = new Point2D.Float();
	
	private Point2D.Float X[];
	private boolean isColliding = false;
	
	public CollisionBlock(float x, float y, float w, float h, Handler handler, ObjectId id) {
		super(x, y, w, h, id);
		this.handler = handler;
		buildHexagonBB();
		poly=tile();
	}
	
	public void update(LinkedList<GameObject> object) {
		CollisionVsTile();
	}
	
	public void render(Graphics2D g2) {
		g2.setColor(Color.RED);
		g2.draw(poly);
	}
	
	private void CollisionVsTile() {
		boolean insideBB = false;
		shipPoints = new Point2D.Float[4];
		
		for(int i = 0; i < handler.player.size(); i++) {
			PlayerObject tempObject = handler.player.get(i);			
			if(tempObject.getId() == PlayerId.PlayerShip) {
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
						//Get ship points
						for(int k = 0; k < tempObject.getxPoints().length; k++) {
							shipPoints[k] = new Point2D.Float(tempObject.getxPoints()[k],tempObject.getyPoints()[k]);
						}
						//Segment 0-1			
						if(tempObject.getyPoints()[j] > bound_y[0] && tempObject.getxPoints()[j] > bound_x[1] &&tempObject.getyPoints()[j] < bound_y[1]) {
							segmentPointA.x = bound_x[0]; segmentPointA.y = bound_y[0];
							segmentPointB.x = bound_x[1]; segmentPointB.y = bound_y[1];
							getProjectionPoints(segmentPointA, segmentPointB, X[0], "right", tempObject);
							//Collision /<---
							if(minProjected.x < segmentPointA.x) {
								isColliding = true;
								px[0] = segmentPointA.x - minProjected.x;
								py[0] = segmentPointA.y - minProjected.y;
							}				
							//End of segment collision
							if(minShip.y < segmentPointA.y) {
								isColliding = false;
								lineIntersection(segmentPointA, shipPoints[0], shipPoints[1]);
								lineIntersection(segmentPointA, shipPoints[0], shipPoints[3]);
							}
							//End of segment collision
							if(minShip.y > segmentPointB.y) {
								isColliding = false;
								lineIntersection(segmentPointB, shipPoints[0], shipPoints[3]);
								lineIntersection(segmentPointB, shipPoints[1], shipPoints[3]);
							}		
							//Collision / -->
							if (minProjected.x > segmentPointA.x) {
								isColliding = false;
							}
						}															
						//Segment 1-2
						if(tempObject.getxPoints()[j] < bound_x[1] && tempObject.getyPoints()[j] > bound_y[1] && tempObject.getxPoints()[j] > bound_x[2]) {
							segmentPointA.x = bound_x[1]; segmentPointA.y = bound_y[1];
							segmentPointB.x = bound_x[2]; segmentPointB.y = bound_y[2];
							getProjectionPoints(segmentPointA, segmentPointB, X[1], "right", tempObject);
							//Collision |<---
							if(minProjected.y < segmentPointA.y) {
								isColliding = true;
								px[0] = segmentPointA.x - minProjected.x;
								py[0] = segmentPointA.y - minProjected.y;
							}
							//End of segment collision
							if(maxShip.x > segmentPointA.x) {
								isColliding = false;
								lineIntersection(segmentPointA, shipPoints[0], shipPoints[1]);
								lineIntersection(segmentPointA, shipPoints[1], shipPoints[3]);
							}
							//End of segment collision
							if(maxShip.x < segmentPointB.x) {
								isColliding = false;
								lineIntersection(segmentPointB, shipPoints[0], shipPoints[3]);
								lineIntersection(segmentPointB, shipPoints[1], shipPoints[3]);
							}
							//Collision | -->
							if (minProjected.y > segmentPointA.y) {
								isColliding = false;
							} 
							
						}
						//Segment 2-3
						if(tempObject.getxPoints()[j] < bound_x[2] && tempObject.getyPoints()[j] < bound_y[2] && tempObject.getyPoints()[j] > bound_y[3]) {
							segmentPointA.x = bound_x[2]; segmentPointA.y = bound_y[2];
							segmentPointB.x = bound_x[3]; segmentPointB.y = bound_y[3];
							getProjectionPoints(segmentPointA, segmentPointB, X[2], "reverse", tempObject);
							//Collision -->\
							if (maxProjected.x > segmentPointA.x) {
								isColliding = true;
								px[0] = segmentPointA.x - maxProjected.x;
								py[0] = segmentPointA.y - maxProjected.y;
							}
							//End of segment collision
							if(maxShip.y > segmentPointA.y) {
								isColliding = false;
								lineIntersection(segmentPointA, shipPoints[0], shipPoints[1]);
								lineIntersection(segmentPointA, shipPoints[1], shipPoints[3]);
							}
							//End of segment collision
							if(maxShip.y < segmentPointB.y) {
								isColliding = false;
								lineIntersection(segmentPointB, shipPoints[0], shipPoints[3]);
								lineIntersection(segmentPointB, shipPoints[1], shipPoints[3]);
							}
							//Collision <--\
							if(maxProjected.x < segmentPointA.x) {
								isColliding = false;
							}																		
						}
						//Segment 3-4
						if(tempObject.getxPoints()[j] < bound_x[4] && tempObject.getyPoints()[j] < bound_y[3] && tempObject.getyPoints()[j] > bound_y[4]) {
							segmentPointA.x = bound_x[3]; segmentPointA.y = bound_y[3];	
							segmentPointB.x = bound_x[4]; segmentPointB.y = bound_y[4];							
							getProjectionPoints(segmentPointA, segmentPointB, X[3], "right", tempObject);					
							//Collision -->/
							if (maxProjected.x > segmentPointA.x) {
								isColliding = true;
								px[0] = segmentPointA.x - maxProjected.x;
								py[0] = segmentPointA.y - maxProjected.y;
							}
							//End of segment collision
							if(maxShip.y > segmentPointA.y) {
								isColliding = false;
								lineIntersection(segmentPointA, shipPoints[0], shipPoints[1]);
								lineIntersection(segmentPointA, shipPoints[1], shipPoints[3]);
							}
							//End of segment collision
							if(maxShip.y < segmentPointB.y) {
								isColliding = false;
								lineIntersection(segmentPointB, shipPoints[0], shipPoints[3]);
								lineIntersection(segmentPointB, shipPoints[1], shipPoints[3]);
							}
							//Collision <--/
							if(maxProjected.x < segmentPointA.x ) {
								isColliding = false;
							}		
						}
						//Segment 4-5
						if(tempObject.getyPoints()[j] < bound_y[4] && tempObject.getxPoints()[j] < bound_x[5] && tempObject.getxPoints()[j] > bound_x[4]) {
							segmentPointA.x = bound_x[4]; segmentPointA.y = bound_y[4];	
							segmentPointB.x = bound_x[5]; segmentPointB.y = bound_y[5];						
							getProjectionPoints(segmentPointA, segmentPointB, X[4], "reverse", tempObject);
							//Collision -->|
							if (minProjected.y > segmentPointA.y) {
								isColliding = true;
								px[0] = segmentPointA.x - minProjected.x;
								py[0] = segmentPointA.y - minProjected.y;	
							}
							//End of segment collision
							if(minShip.x < segmentPointA.x ) {
								isColliding = false;
								lineIntersection(segmentPointA, shipPoints[0], shipPoints[1]);
								lineIntersection(segmentPointA, shipPoints[1], shipPoints[3]);
							}
							//End of segment collision
							if(minShip.x > segmentPointB.x) {
								isColliding = false;
								lineIntersection(segmentPointB, shipPoints[0], shipPoints[3]);
								lineIntersection(segmentPointB, shipPoints[1], shipPoints[3]);
							}
							//Collision <--|
							if(minProjected.y < segmentPointA.y) {
								isColliding = false;
							}
									
						}
						//Segment 5-0
						if(tempObject.getxPoints()[j] > bound_x[5] && tempObject.getyPoints()[j] > bound_y[5] && tempObject.getyPoints()[j] < bound_y[0]) {
							segmentPointA.x = bound_x[5]; segmentPointA.y = bound_y[5];
							segmentPointB.x = bound_x[0]; segmentPointB.y = bound_y[0];							
							getProjectionPoints(segmentPointA, segmentPointB, X[5], "reverse", tempObject);
							//Collision \<--
							if(minProjected.x < segmentPointA.x) {
								isColliding = true;
								px[0] = segmentPointA.x - minProjected.x; 
								py[0] = segmentPointA.y - minProjected.y;
							}
							//End of segment collision
							if(minShip.y < segmentPointA.y) {
								isColliding = false;
								lineIntersection(segmentPointA, shipPoints[0], shipPoints[1]);
								lineIntersection(segmentPointA, shipPoints[1], shipPoints[3]);
							}
							//End of segment collision
							if(minShip.y > segmentPointB.y) {
								isColliding = false;
								lineIntersection(segmentPointB, shipPoints[0], shipPoints[3]);
								lineIntersection(segmentPointB, shipPoints[1], shipPoints[3]);
							}
							//Collision \-->
							if (minProjected.x > segmentPointA.x) {
								isColliding = false;
							}						
						}
					} else {
						isColliding = false;
					}
				}
			}
		}
	}
	

	private void lineIntersection(Point2D.Float pointA, Point2D.Float shipA, Point2D.Float shipB) {
		float s1_x, s1_y, s2_x, s2_y;
		s1_x = x - pointA.x; s1_y = y - pointA.y;
		s2_x = shipB.x - shipA.x; s2_y = shipB.y - shipA.y;
		
		float s, t;
		s = (-s1_y * (pointA.x - shipA.x) + s1_x * (pointA.y - shipA.y)) / (-s2_x * s1_y + s1_x * s2_y);
		t = (s2_x * (pointA.y - shipA.y) - s2_y * (pointA.x - shipA.x)) / (-s2_x * s1_y + s1_x * s2_y);
		
		if(s >= 0 && s <= 1 && t >= 0 && t <= 1) {
			//collision
			isColliding = true;
			ipx = pointA.x + (t * s1_x);
			ipy = pointA.y + (t * s1_y);
			px[0] = pointA.x - ipx;
			py[0] = pointA.y - ipy;
		}
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
	
	private void buildHexagonBB() {
		X = new Point2D.Float[6];
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
		for(int i = 0; i < bound_x.length; i++) {
			if(i < 5) {
				X[i] = getPerpendicular(bound_x[i],bound_y[i],bound_x[i+1],bound_y[i+1], len);
			} if (i == 5) {
				X[i] = getPerpendicular(bound_x[i],bound_y[i],bound_x[0],bound_y[0], len);
			}
		}	
	}
	
	private void getProjectionPoints(Point2D.Float A, Point2D.Float B, Point2D.Float X, String dir, PlayerObject ship) {
		Point2D.Float shipPointsC = new Point2D.Float();
		Point2D.Float projectionOnPerpendicularE = new Point2D.Float();
		float px[] = new float[4];  float py[] = new float[4];
		float sx[] = new float[4];  float sy[] = new float[4];
		
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
	
	private Float[] getMaxValue(float[] points_x, float[] points_y, float[] shipPoints_x, float[] shipPoints_y, String dir) {
		Point2D.Float MaxValue[];
		MaxValue = new Point2D.Float[2];
		MaxValue[0] = new Point2D.Float(points_x[0],points_y[0]); 
		MaxValue[1] = new Point2D.Float(shipPoints_x[0],shipPoints_y[0]);;

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
		Point2D.Float MinValue[];
		MinValue = new Point2D.Float[2];
		MinValue[0] = new Point2D.Float(points_x[0],points_y[0]); 
		MinValue[1] = new Point2D.Float(shipPoints_x[0],shipPoints_y[0]);

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
			
	public float[] getxPoints() {return px;}
	public float[] getyPoints() {return py;}
	public boolean getCollision() {return isColliding;}
}
