package game.prototype.framework;

import java.awt.geom.Point2D;

public class Helper {
    public static Point2D.Float vectorize(Point2D.Float pointA, Point2D.Float pointB) {
        Point2D.Float vector = new Point2D.Float();
        vector.x = (pointB.x - pointA.x);
        vector.y = (pointB.y - pointA.y);
        return vector;
        }
    
    public static float dotProduct(Point2D.Float vec1, Point2D.Float vec2) {
        float dp = ((vec1.x * vec2.x) + (vec1.y * vec2.y));
        return dp;
        }
    
    public static int clamp(int var, int min, int max) {
        if (var >= max)
            return var = max;
        else if (var <= min)
            return var = min;
        else
            return var;
        }
    
    public static Point2D.Float movement(float speed, Point2D.Float[] points, int index) {
        float xnew = points[index].x, ynew = points[index].y;
        xnew += speed * Math.sin(Math.atan2(points[1].x - points[0].x,
                                            points[1].y - points[0].y));
        ynew += speed * Math.cos(Math.atan2(points[1].x - points[0].x,
                                            points[1].y - points[0].y));
        return new Point2D.Float(xnew,ynew);
        }
    
    public static Point2D.Float rotation(float angle, Point2D.Float[] points, int index) {
        points[index].x -= points[0].x;
        points[index].y -= points[0].y;
        
        float xnew = (float) (points[index].x * Math.cos(angle)
                              - points[index].y * Math.sin(angle));
        
        float ynew = (float) (points[index].x * Math.sin(angle)
                              + points[index].y * Math.cos(angle));
        
        points[index].x = xnew + points[0].x;
        points[index].y = ynew + points[0].y;
        
        return new Point2D.Float(points[index].x,points[index].y);
        }
    
    public static float angle(Point2D.Float center, Point2D.Float target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - center.y,
                                                        target.x - center.x));
        if (angle < 0)
            angle += 360;
        return angle;
        }
    
    public static float bulletAngle(Point2D.Float[] points) {
        return (float) Math.toRadians((Helper.angle(points[1],
                                                    points[0]))-90);
        }
    
    public static boolean inside(Point2D.Float point, Point2D.Float[] poly) {
	    // ray-casting algorithm based on
	    // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        float x = point.x, y = point.y;
        boolean inside = false;
        
        for (int i = 1, j = poly.length - 1; i < poly.length; j = i++) {
            float xi = poly[i].x, yi = poly[i].y;
            float xj = poly[j].x, yj = poly[j].y;
            
            if (((yi > y) != (yj > y))  && (x < (xj - xi) * (y - yi) / (yj - yi) + xi))
                inside = !inside;
            }
        return inside;
        }
    }
