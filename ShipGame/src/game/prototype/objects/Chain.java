package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.framework.Animation;
import game.prototype.framework.GameObject;
import game.prototype.framework.GameStates;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class Chain extends GameObject {

    private Handler handler;
    private GameStates gs;
    private TextureManager tex = Game.getTexInstance();
    private Animation electricArc;
    private AffineTransform at, at2, at3;
    private float fSpeed, ipx, ipy;
    private Point2D.Float p = new Point2D.Float();
    private Point2D.Float[] nPoints, mPoints, jPoints;

    public Chain(float x, float y, float w, float h, int n, int m, int j, float speed, 
                 Handler handler, GameStates gs, ObjectId id) {
        super(x, y, w, h, id);
        this.setRenderPriority(3);
        this.handler = handler;
        this.gs = gs;
        this.fSpeed = speed;
        electricArc = new Animation(tex.electricArc);
        buildChain(n, m, j);
    }

    public void update(LinkedList<GameObject> object) {
        chainMove();
        electricArc.runAnimation(7);
        if (nPoints.length > 1)
            at = affineCalc(nPoints); 
        if (mPoints.length > 1) 
            at2 = affineCalc(mPoints);
        if (jPoints.length > 1) 
            at3 = affineCalc(jPoints);     
        chainCollision();
    }

    public void render(Graphics2D g2) {
        g2.drawImage(tex.chainBase, (int)chainBox().getMinX(), (int)chainBox().getMinY(),
                    (int)chainBox().getWidth(), (int)chainBox().getHeight(), null);
        if (nPoints.length > 1)
            electricArc.drawAnimation(g2, at);
        if (mPoints.length > 1) 
            electricArc.drawAnimation(g2, at2);
        if (jPoints.length > 1) 
            electricArc.drawAnimation(g2, at3);
    }

    private void buildChain(int n, int m, int j) {
        nPoints = new Point2D.Float[n + 1];
        mPoints = new Point2D.Float[m + 1];
        jPoints = new Point2D.Float[j + 1];
        nPoints[0] = mPoints[0] = jPoints[0] = new Point2D.Float(x, y);
        if (n == 0) {
            for (int i = 1; i < mPoints.length; i++) 
                mPoints[i] = new Point2D.Float(mPoints[i - 1].x - w, 
                                               mPoints[i - 1].y);           
            for (int i = 1; i < jPoints.length; i++) 
                jPoints[i] = new Point2D.Float(jPoints[i - 1].x + w, 
                                               jPoints[i - 1].y);           
        } else if (m == 0) {
            for (int i = 1; i < nPoints.length; i++) 
                nPoints[i] = new Point2D.Float(nPoints[i - 1].x - w, 
                                               nPoints[i - 1].y);      
            for (int i = 1; i < jPoints.length; i++) 
                jPoints[i] = new Point2D.Float(jPoints[i - 1].x + w, 
                                               jPoints[i - 1].y);      
        } else if (j == 0) {
            for (int i = 1; i < nPoints.length; i++) 
                nPoints[i] = new Point2D.Float(nPoints[i - 1].x - w, 
                                               nPoints[i - 1].y);    
            for (int i = 1; i < mPoints.length; i++) 
                mPoints[i] = new Point2D.Float(mPoints[i - 1].x + w, 
                                               mPoints[i - 1].y);     
        } else {
            for (int i = 1; i < nPoints.length; i++) 
                nPoints[i] = new Point2D.Float((float) (nPoints[i - 1].x
                                             - (Math.cos(Math.toRadians(30)) * w)),
                                               (float) (nPoints[i - 1].y
                                             + (Math.tan(Math.toRadians(30)) * w)));         
            for (int i = 1; i < mPoints.length; i++) 
                mPoints[i] = new Point2D.Float((float) (mPoints[i - 1].x
                                             + (Math.cos(Math.toRadians(30)) * w)),
                                               (float) (mPoints[i - 1].y
                                             + (Math.tan(Math.toRadians(30)) * w)));           
            for (int i = 1; i < jPoints.length; i++) 
                jPoints[i] = new Point2D.Float(jPoints[i - 1].x, 
                                               (float) (jPoints[i - 1].y
                                             - (Math.tan(Math.toRadians(30)) * w * 1.8f)));           
        }      
    }

    private void chainMove() {
        for (int i = 0; i < nPoints.length - 1; i++) 
            nPoints[i + 1] = Helper.rotation(fSpeed, nPoints, i + 1);      
        for (int i = 0; i < mPoints.length - 1; i++) 
            mPoints[i + 1] = Helper.rotation(fSpeed, mPoints, i + 1);       
        for (int i = 0; i < jPoints.length - 1; i++) 
            jPoints[i + 1] = Helper.rotation(fSpeed, jPoints, i + 1);
    }

    private void chainCollision() {
        Point2D.Float baseChain[] = new Point2D.Float[4];
        Point2D.Float shipBounds[] = new Point2D.Float[3];
        baseChain[0] = new Point2D.Float(x + w/2, y - h/2);
        baseChain[1] = new Point2D.Float(x + w/2, y + h/2);
        baseChain[2] = new Point2D.Float(x - w/2, y + h/2);
        baseChain[3] = new Point2D.Float(x - w/2, y - h/2);
        if (handler.player.size() > 0) {
            shipBounds[0] = handler.player.get(0).points()[1];
            shipBounds[1] = handler.player.get(0).points()[2];
            shipBounds[2] = handler.player.get(0).points()[4];
            
            chainCheck(nPoints);
            chainCheck(mPoints);
            chainCheck(jPoints);

            for (int i = 0; i < baseChain.length; i++) {
                for (int j = 0; j < shipBounds.length; j++) {                
                    if (j == 2) 
                        lineIntersection(new Point2D.Float(x, y), baseChain[i], 
                                         shipBounds[j], shipBounds[0], false);
                    else 
                        lineIntersection(new Point2D.Float(x, y), baseChain[i], 
                                         shipBounds[j], shipBounds[j + 1], false);
                    if (i == 3)
                        lineIntersection(handler.player.get(0).points()[0], shipBounds[j], 
                                         baseChain[i], baseChain[0], true);
                    else                        
                        lineIntersection(handler.player.get(0).points()[0], shipBounds[j], 
                                         baseChain[i], baseChain[i + 1], true);
                }
            }
            if (gs.getBlockCollision()) {
                p.x += ipx;
                p.y += ipy;
                ipx = ipy = 0;
            } else {
                p.x = 0;
                p.y = 0;
            }    
        }      
    }

    private void chainCheck(Point2D.Float[] points) {
        for (int i = 0; i < handler.player.get(0).points().length; i++) {
            for (int j = 1; j < points.length; j++) 
                if (chainEllipse(j, points)
                                .contains(handler.player.get(0).points()[i])) {
                    if (gs.getShieldState()) {
                        gs.setShieldHealth(-1);
                        gs.setShieldHit(true);
                    } else {
                        gs.setHealth(-1);
                        gs.setPlayerHit(true);
                    }
                }
        }
    }

    private AffineTransform affineCalc(Point2D.Float[] points){
        float fDist;
        AffineTransform affineT;
        fDist = Helper.distance(points[0], points[1]);
        affineT = AffineTransform.getTranslateInstance(x, y);
        affineT.rotate(Helper.angle(points[0], points[points.length - 1]) - Math.toRadians(90));
        affineT.scale(w / tex.electricArc[0].getWidth(), 
                     (fDist * (points.length - 0.5f) / tex.electricArc[0].getHeight()));
        affineT.translate(-tex.electricArc[0].getWidth() / 2, 0);
        return affineT;
    }

    private Ellipse2D chainEllipse(int i, Point2D.Float points[]) {
        return new Ellipse2D.Float((points[i].x - w / 2), 
                                   (points[i].y - h / 2), w, h);
    }

    private Rectangle2D chainBox() {
        return new Rectangle2D.Float((x - w / 2), 
                                     (y - h / 2), w, h);
    }

    private void lineIntersection(Point2D.Float s1PointA, Point2D.Float s1PointB, 
                                  Point2D.Float s2PointA, Point2D.Float s2PointB, 
                                  boolean s_vs_b) {
        float s1_x = s1PointA.x - s1PointB.x;
        float s1_y = s1PointA.y - s1PointB.y;
        float s2_x = s2PointB.x - s2PointA.x;
        float s2_y = s2PointB.y - s2PointA.y;
        float h = (-s2_x * s1_y + s1_x * s2_y);
        
        float t1 = (s2_x * (s1PointB.y - s2PointA.y) - s2_y * (s1PointB.x - s2PointA.x)) / h;
        float t2 = (-s1_y * (s1PointB.x - s2PointA.x) + s1_x * (s1PointB.y - s2PointA.y)) / h;     
               
        if (t1 >= 0.0f && t1 < 1.0f && t2 >= 0.0f && t2 < 1.0f) {
            gs.setBlockCollision(true);
            if (s_vs_b) {
                ipx = s1PointB.x - (s1PointB.x + (t1 * (s1PointB.x - s1PointA.x)));
                ipy = s1PointB.y - (s1PointB.y + (t1 * (s1PointB.y - s1PointA.y)));
            } else {
                ipx = s1PointB.x - (s1PointB.x - (t1 * (s1PointB.x - s1PointA.x)));
                ipy = s1PointB.y - (s1PointB.y - (t1 * (s1PointB.y - s1PointA.y)));
            }
        }
    }

    public Float deltaPoints() {
        return p;
    }

    public String type() {
        return null;
    }
}