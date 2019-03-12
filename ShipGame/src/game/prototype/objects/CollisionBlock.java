package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedList;

import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class CollisionBlock extends GameObject {

    private Handler handler;
    private Path2D poly = new Path2D.Double();
    private float ipx, ipy;
    private Point2D.Float tileBounds[];
    private Point2D.Float p = new Point2D.Float();
    private boolean isColliding = false;

    public CollisionBlock(float x, float y, float w, float h, Handler handler, ObjectId id) {
        super(x, y, w, h, id);
        this.handler = handler;
        this.setRenderPriority(1);
        buildHexagonBB();
        poly = tile();
    }

    public void update(LinkedList<GameObject> object) {
        shapeOverlap_Diagonals();
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.draw(poly);
    }

    private void shapeOverlap_Diagonals() {
        if(handler.player.size()>0) {
            Point2D.Float shipBounds[] = new Point2D.Float[3];
            shipBounds[0] = handler.player.get(0).points()[1];
            shipBounds[1] = handler.player.get(0).points()[2];
            shipBounds[2] = handler.player.get(0).points()[4];
            
            for(int i =0; i<6;i++) {
                for(int j= 0; j<3;j++) {
                    
                    if(j==2) {
                        lineIntersection(new Point2D.Float(x,y), tileBounds[i], shipBounds[j], shipBounds[0], 1);                    
                    }else {
                        lineIntersection(new Point2D.Float(x,y), tileBounds[i], shipBounds[j], shipBounds[j+1], 1); 
                    }
                    if(i == 5) {
                        lineIntersection(handler.player.get(0).points()[0],shipBounds[j],tileBounds[i],tileBounds[0], -1);                  
                    } else {
                        lineIntersection(handler.player.get(0).points()[0],shipBounds[j],tileBounds[i],tileBounds[i+1], -1); 
                    }
                    
                }          
            }
            
            if(isColliding) {
                p.x += ipx;
                p.y += ipy;
            } else {
                p.x = 0;
                p.y = 0;
            }            
        }   
    }


    private void lineIntersection(Point2D.Float s1PointA, Point2D.Float s1PointB, 
                                  Point2D.Float s2PointA, Point2D.Float s2PointB, int dir) 
    {
        float s1_x = s1PointA.x - s1PointB.x;
        float s1_y = s1PointA.y - s1PointB.y;
        float s2_x = s2PointB.x - s2PointA.x;
        float s2_y = s2PointB.y - s2PointA.y;
        float h = (-s2_x * s1_y + s1_x * s2_y);
        
        float t1 = (s2_x * (s1PointB.y - s2PointA.y) - s2_y * (s1PointB.x - s2PointA.x)) / h;
        float t2 = (-s1_y * (s1PointB.x - s2PointA.x) + s1_x * (s1PointB.y - s2PointA.y)) / h;
        
               
        if (t1 >= 0.0f && t1 < 1.0f && t2 >= 0.0f && t2 < 1.0f)
        {
            isColliding = true; 
            if(dir < 0) {
                ipx = s1PointB.x - (s1PointB.x + (t1 * (s1PointB.x - s1PointA.x)));
                ipy = s1PointB.y - (s1PointB.y + (t1 * (s1PointB.y - s1PointA.y)));              
            } else {
                ipx = s1PointB.x - (s1PointB.x + (t1 * (s1PointA.x - s1PointB.x)));
                ipy = s1PointB.y - (s1PointB.y + (t1 * (s1PointA.y - s1PointB.y)));           
            }                      
        }
    }

    private Path2D tile() {
        Point2D.Double center = new Point2D.Double(x, y);
        Point2D.Double radius = new Point2D.Double(w, h);
        double xpoints[] = new double[6];
        double ypoints[] = new double[6];
        Path2D polygon = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            xpoints[i] = center.x + radius.x * Math.cos(i * 2 * Math.PI / 6);
            ypoints[i] = center.y + radius.y * Math.sin(i * 2 * Math.PI / 6);
        }
        polygon.moveTo(xpoints[0], ypoints[0]);
        for (int i = 1; i < xpoints.length; ++i) {
            polygon.lineTo(xpoints[i], ypoints[i]);
        }
        polygon.closePath();
        return polygon;
    }

    private void buildHexagonBB() {
        tileBounds = new Point2D.Float[6];
        ArrayList<float[]> points = new ArrayList<float[]>();
        float[] coords = new float[6];

        for (PathIterator path = tile().getPathIterator(null); 
             !path.isDone(); path.next()) {
            path.currentSegment(coords);
            float[] pathpoints = { coords[0], coords[1] };
            points.add(pathpoints);
        }

        for (int i = 0; i < 6; i++) {
            tileBounds[i] = new Point2D.Float(points.get(i)[0], points.get(i)[1]);
        }

    }
    
    public Point2D.Float deltaPoints() {
        return p;
    }
    
    public void setCollision(boolean collisionState) {
        this.isColliding = collisionState;
    }

    public boolean getCollision() {
        return isColliding;
    }

    public String type() {
        return null;
    }
}
