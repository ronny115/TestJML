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
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.PlayerId;
import game.prototype.framework.PlayerObject;

public class CollisionBlock extends GameObject {

    private Handler handler;
    private Path2D poly = new Path2D.Double();
    private float ipx, ipy;
    private Point2D.Float shipPoints[];
    private Point2D.Float bounds[];
    private Point2D.Float p = new Point2D.Float();
    private Point2D.Float minProjected = new Point2D.Float();
    private Point2D.Float maxProjected = new Point2D.Float();
    private Point2D.Float maxShip = new Point2D.Float();
    private Point2D.Float minShip = new Point2D.Float();
    private Point2D.Float X[];
    private boolean isColliding = false;

    public CollisionBlock(float x, float y, float w, float h, Handler handler, ObjectId id) {
        super(x, y, w, h, id);
        this.handler = handler;
        buildHexagonBB();
        poly = tile();
    }

    public void update(LinkedList<GameObject> object) {
        PlayerVsTile();
    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.RED);
        g2.draw(poly);
    }

    private void PlayerVsTile() {
        shipPoints = new Point2D.Float[5];
        boolean insideBB = false;

        for (int i = 0; i < handler.player.size(); i++) {
            PlayerObject tempObject = handler.player.get(i);
            if (tempObject.getId() == PlayerId.PlayerShip) {
                shipPoints = tempObject.points();
                for (int j = 0; j < shipPoints.length - 1; j++) {
                    if (shipPoints[j + 1].x > (x - w - (tempObject.getW() / 2))&& 
                        shipPoints[j + 1].x < (x + w + (tempObject.getW() / 2))&& 
                        shipPoints[j + 1].y > (y - h)&& 
                        shipPoints[j + 1].y < (y + h)) 
                    {
                        insideBB = true;
                        break;
                    } else
                        insideBB = false;
                }

                for (int j = 0; j < shipPoints.length - 1; j++) {
                    if (insideBB == true) {
                        // Segment 0-1
                        if (shipPoints[j + 1].y > bounds[0].y&&
                            shipPoints[j + 1].x > bounds[1].x&& 
                            shipPoints[j + 1].y < bounds[1].y) 
                        {
                            // Collision
                            getProjectionPoints(bounds[0], bounds[1], X[0], "right");
                            if (minProjected.x < bounds[0].x) {
                                isColliding = true;
                                p.x = bounds[0].x - minProjected.x;
                                p.y = bounds[0].y - minProjected.y;
                            }
                            if (minShip.y < bounds[0].y) {
                                isColliding = false;
                                lineIntersection(bounds[0], shipPoints[1], shipPoints[2]);
                                lineIntersection(bounds[0], shipPoints[2], shipPoints[4]);
                            }
                            if (minShip.y > bounds[1].y) {
                                isColliding = false;
                                lineIntersection(bounds[1], shipPoints[1], shipPoints[4]);
                                lineIntersection(bounds[1], shipPoints[2], shipPoints[4]);
                            }
                            if (minProjected.x > bounds[0].x)
                                isColliding = false;
                            // Segment 1-2
                        } else if (shipPoints[j + 1].x < bounds[1].x&& 
                                   shipPoints[j + 1].y > bounds[1].y&&
                                   shipPoints[j + 1].x > bounds[2].x) 
                        {
                            // Collision
                            getProjectionPoints(bounds[1], bounds[2], X[1], "right");
                            if (minProjected.y < bounds[1].y) {
                                isColliding = true;
                                p.x = bounds[1].x - minProjected.x;
                                p.y = bounds[1].y - minProjected.y;
                            }
                            if (maxShip.x > bounds[1].x) {
                                isColliding = false;
                                lineIntersection(bounds[1], shipPoints[1], shipPoints[2]);
                                lineIntersection(bounds[1], shipPoints[2], shipPoints[4]);
                            }
                            if (maxShip.x < bounds[2].x) {
                                isColliding = false;
                                lineIntersection(bounds[2], shipPoints[1], shipPoints[4]);
                                lineIntersection(bounds[2], shipPoints[2], shipPoints[4]);
                            }

                            if (minProjected.y > bounds[1].y)
                                isColliding = false;
                            // Segment 2-3
                        } else if (shipPoints[j + 1].x < bounds[2].x&& 
                                   shipPoints[j + 1].y < bounds[2].y&&
                                   shipPoints[j + 1].y > bounds[3].y) 
                        {
                            // Collision
                            getProjectionPoints(bounds[2], bounds[3], X[2], "reverse");
                            if (maxProjected.x > bounds[2].x) {
                                isColliding = true;
                                p.x = bounds[2].x - maxProjected.x;
                                p.y = bounds[2].y - maxProjected.y;
                            }
                            if (maxShip.y > bounds[2].y) {
                                isColliding = false;
                                lineIntersection(bounds[2], shipPoints[1], shipPoints[2]);
                                lineIntersection(bounds[2], shipPoints[2], shipPoints[4]);
                            }
                            if (maxShip.y < bounds[3].y) {
                                isColliding = false;
                                lineIntersection(bounds[3], shipPoints[1], shipPoints[4]);
                                lineIntersection(bounds[3], shipPoints[2], shipPoints[4]);
                            }
                            if (maxProjected.x < bounds[2].x)
                                isColliding = false;
                            // Segment 3-4
                        } else if (shipPoints[j + 1].x < bounds[4].x&&
                                   shipPoints[j + 1].y < bounds[3].y&&
                                   shipPoints[j + 1].y > bounds[4].y) 
                        {
                            // Collision
                            getProjectionPoints(bounds[3], bounds[4], X[3], "right");
                            if (maxProjected.x > bounds[3].x) {
                                isColliding = true;
                                p.x = bounds[3].x - maxProjected.x;
                                p.y = bounds[3].y - maxProjected.y;
                            }
                            if (maxShip.y > bounds[3].y) {
                                isColliding = false;
                                lineIntersection(bounds[3], shipPoints[1], shipPoints[2]);
                                lineIntersection(bounds[3], shipPoints[2], shipPoints[4]);
                            }
                            if (maxShip.y < bounds[4].y) {
                                isColliding = false;
                                lineIntersection(bounds[4], shipPoints[1], shipPoints[4]);
                                lineIntersection(bounds[4], shipPoints[2], shipPoints[4]);
                            }
                            if (maxProjected.x < bounds[3].x)
                                isColliding = false;
                            // Segment 4-5
                        } else if (shipPoints[j + 1].y < bounds[4].y&&
                                   shipPoints[j + 1].x < bounds[5].x&&
                                   shipPoints[j + 1].x > bounds[4].x) 
                        {
                            // Collision
                            getProjectionPoints(bounds[4], bounds[5], X[4], "reverse");
                            if (minProjected.y > bounds[4].y) {
                                isColliding = true;
                                p.x = bounds[4].x - minProjected.x;
                                p.y = bounds[4].y - minProjected.y;
                            }
                            if (minShip.x < bounds[4].x) {
                                isColliding = false;
                                lineIntersection(bounds[4], shipPoints[1], shipPoints[2]);
                                lineIntersection(bounds[4], shipPoints[2], shipPoints[4]);
                            }
                            if (minShip.x > bounds[5].x) {
                                isColliding = false;
                                lineIntersection(bounds[5], shipPoints[1], shipPoints[4]);
                                lineIntersection(bounds[5], shipPoints[2], shipPoints[4]);
                            }
                            if (minProjected.y < bounds[4].y)
                                isColliding = false;
                            // Segment 5-0
                        } else if (shipPoints[j + 1].x > bounds[5].x&&
                                   shipPoints[j + 1].y > bounds[5].y&&
                                   shipPoints[j + 1].y < bounds[0].y) 
                        {
                            // Collision
                            getProjectionPoints(bounds[5], bounds[0], X[5], "reverse");
                            if (minProjected.x < bounds[5].x) {
                                isColliding = true;
                                p.x = bounds[5].x - minProjected.x;
                                p.y = bounds[5].y - minProjected.y;
                            }
                            if (minShip.y < bounds[5].y) {
                                isColliding = false;
                                lineIntersection(bounds[5], shipPoints[1], shipPoints[2]);
                                lineIntersection(bounds[5], shipPoints[2], shipPoints[4]);
                            }
                            if (minShip.y > bounds[0].y) {
                                isColliding = false;
                                lineIntersection(bounds[0], shipPoints[1], shipPoints[4]);
                                lineIntersection(bounds[0], shipPoints[2], shipPoints[4]);
                            }
                            if (minProjected.x > bounds[5].x)
                                isColliding = false;
                        }
                    } else
                        isColliding = false;
                }
            }
        }
    }

    private void lineIntersection(Point2D.Float pointA, Point2D.Float shipA, 
                                  Point2D.Float shipB) 
    {
        float s1_x, s1_y, s2_x, s2_y;
        float s, t;

        s1_x = x - pointA.x;
        s1_y = y - pointA.y;
        s2_x = shipB.x - shipA.x;
        s2_y = shipB.y - shipA.y;

        s = (-s1_y * (pointA.x - shipA.x) + s1_x * (pointA.y - shipA.y)) 
            / (-s2_x * s1_y + s1_x * s2_y);
        t = (s2_x * (pointA.y - shipA.y) - s2_y * (pointA.x - shipA.x)) 
            / (-s2_x * s1_y + s1_x * s2_y);

        if (s >= 0 && s <= 1 && t >= 0 && t <= 1) {
            // Collision
            isColliding = true;
            ipx = pointA.x + (t * s1_x);
            ipy = pointA.y + (t * s1_y);
            p.x = pointA.x - ipx;
            p.y = pointA.y - ipy;
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
        X = new Point2D.Float[6];
        bounds = new Point2D.Float[6];
        ArrayList<float[]> points = new ArrayList<float[]>();
        float[] coords = new float[6];
        float len = 0.5f;

        for (PathIterator path = tile().getPathIterator(null); 
             !path.isDone(); path.next()) {
            path.currentSegment(coords);
            float[] pathpoints = { coords[0], coords[1] };
            points.add(pathpoints);
        }

        for (int i = 0; i < 6; i++) {
            bounds[i] = new Point2D.Float(points.get(i)[0], points.get(i)[1]);
        }

        for (int i = 0; i < bounds.length; i++) {
            if (i < 5)
                X[i] = getPerpendicular(bounds[i], bounds[i + 1], len);
            if (i == 5)
                X[i] = getPerpendicular(bounds[i], bounds[0], len);
        }
    }

    private void getProjectionPoints(Point2D.Float A, Point2D.Float B, 
                                     Point2D.Float X, String dir) 
    {
        Point2D.Float shipPointsC = new Point2D.Float();
        Point2D.Float projectionOnPerpendicularE = new Point2D.Float();

        float px[] = new float[4];
        float py[] = new float[4];
        float sx[] = new float[4];
        float sy[] = new float[4];

        for (int j = 0; j < shipPoints.length - 1; j++) {
            shipPointsC.x = shipPoints[j + 1].x;
            shipPointsC.y = shipPoints[j + 1].y;

            projectionOnPerpendicularE = projection(X, A, shipPointsC);
            projectionOnPerpendicularE.x = projectionOnPerpendicularE.x + X.x;
            projectionOnPerpendicularE.y = projectionOnPerpendicularE.y + X.y;

            px[j] = projectionOnPerpendicularE.x;
            py[j] = projectionOnPerpendicularE.y;

            sx[j] = shipPoints[j + 1].x;
            sy[j] = shipPoints[j + 1].y;
        }

        maxProjected = getMaxValue(px, py, sx, sy, dir)[0];
        maxShip = getMaxValue(px, py, sx, sy, dir)[1];
        minProjected = getMinValue(px, py, sx, sy, dir)[0];
        minShip = getMinValue(px, py, sx, sy, dir)[1];
    }

    private Float[] getMaxValue(float[] points_x, float[] points_y, 
                                float[] shipPoints_x, float[] shipPoints_y,
                                String dir) 
    {
        Point2D.Float MaxValue[];
        MaxValue = new Point2D.Float[2];
        MaxValue[0] = new Point2D.Float(points_x[0], points_y[0]);
        MaxValue[1] = new Point2D.Float(shipPoints_x[0], shipPoints_y[0]);
        ;

        for (int i = 0; i < points_x.length; i++) {
            switch (dir) {
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

    private Float[] getMinValue(float[] points_x, float[] points_y, 
                                float[] shipPoints_x, float[] shipPoints_y,
                                String dir) 
    {
        Point2D.Float MinValue[];
        MinValue = new Point2D.Float[2];
        MinValue[0] = new Point2D.Float(points_x[0], points_y[0]);
        MinValue[1] = new Point2D.Float(shipPoints_x[0], shipPoints_y[0]);

        for (int i = 0; i < points_x.length; i++) {
            switch (dir) {
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
                    if (points_y[i] > MinValue[0].y) {
                        MinValue[0].y = points_y[i];
                        MinValue[1].y = shipPoints_y[i];
                    }
                    break;
            }
        }
        return MinValue;
    }

    private Point2D.Float getPerpendicular(Point2D.Float p1, Point2D.Float p2, 
                                           float length) 
    {
        Point2D.Float pointA = new Point2D.Float();
        Point2D.Float pointB = new Point2D.Float();
        pointA.x = p1.x;
        pointA.y = p1.y;
        pointB.x = p2.x;
        pointB.y = p2.y;

        Point2D.Float vector = Helper.vectorize(pointB, pointA);
        Point2D.Float pVector = new Point2D.Float();
        pVector.x = vector.y;
        pVector.y = -vector.x;

        float x = p1.x - pVector.x * length;
        float y = p1.y - pVector.y * length;

        return new Point2D.Float(x, y);
    }

    private Float projection(Point2D.Float A, Point2D.Float B, Point2D.Float C) {
        Point2D.Float vector1 = Helper.vectorize(A, B);
        Point2D.Float vector2 = Helper.vectorize(A, C);

        float d = Helper.dotProduct(vector1, vector1);
        float dp = Helper.dotProduct(vector1, vector2);
        float multiplier = dp / d;
        float Dx = vector1.x * multiplier;
        float Dy = vector1.y * multiplier;

        return new Point2D.Float(Dx, Dy);
    }

    public Point2D.Float deltaPoints() {
        return p;
    }

    public boolean collision() {
        return isColliding;
    }

    public String type() {
        return null;
    }
}
