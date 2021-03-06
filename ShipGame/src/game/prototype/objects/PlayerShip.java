package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.framework.Animation;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.PlayerId;
import game.prototype.framework.PlayerObject;
import game.prototype.framework.GameStates;
import game.prototype.framework.TextureManager;

public class PlayerShip extends PlayerObject {

    private Handler handler;
    private GameStates gs;
    private TextureManager tex = Game.getTexInstance();
    private Animation exp, alert, hit;
    private AffineTransform at;
    private Point2D.Float[] sPoints = new Point2D.Float[5];
    
    private long firstTime;
    private boolean blinking;
    private int blinkingTime;
    
    public PlayerShip(float x, float y, float w, float h, float angle, Handler handler, 
                      GameStates gs, PlayerId id)
    {
        super(x, y, w, h, id);
        this.handler = handler;
        this.gs = gs;
        shipBounds(angle);
        gs.setAngle(0); 
        firstTime = System.currentTimeMillis();
        exp = new Animation(tex.playerExplosion);
        alert = new Animation(tex.playerAlert);
        hit = new Animation(tex.playerHit);
        handler.addObject(new PropulsionFX(sPoints[3].x, sPoints[3].y, 15, 20,
                                           handler, gs, ObjectId.Propulsion));
    }

    public void updatePlayer(LinkedList<PlayerObject> object) {
        if (gs.getHealth() > 35 && gs.getHealth() <= 45 ) 
            alert.runAnimation(5);                   
        else if (gs.getHealth() > 25 && gs.getHealth() <= 35 ) 
            alert.runAnimation(4);    
        else if (gs.getHealth() > 10 && gs.getHealth() <= 25 ) 
            alert.runAnimation(2);    
        else if (gs.getHealth() > 0 && gs.getHealth() <= 10 ) 
            alert.runAnimation(1);       
        else if (gs.getHealth() == 0) {
            velX = velY = 0;
            exp.runAnimationOnce(8);
        } 
        else 
            Game.GameOver = false;

        if (gs.getPlayerHit())
            hit.runAnimationOnce(5);

        if (gs.getPlayerHit() && hit.isDone) {
            gs.setPlayerHit(false);
            hit.isDone = false;
        }

        if (exp.isDone) {
            gs.setAngle(Helper.angle(sPoints[0], sPoints[1]) 
                                     + (float)Math.toRadians(90));
            handler.removePlayer(this);
            Game.GameOver = !Game.GameOver;
        }
        at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(Helper.angle(sPoints[0], sPoints[1]) + Math.toRadians(90));
        at.translate(-(w/2), -(h/2));
        at.scale((w / tex.player.getWidth()), (h / tex.player.getHeight()));
        playerReaction();
        shipMovement();
        blinkingTimer();
    }

    public void renderPlayer(Graphics2D g2) {       
        if (gs.getHealth() == 0 && !exp.isDone) 
            exp.drawAnimation(g2, (int)(x -w), (int)(y-h), (int)(w*2+(h-w)), (int)h*2);            
        else if (gs.getHealth() > 45) {                     
            if (blinking) {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(tex.player, at, null);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                if (gs.getPlayerHit())
                    hit.drawAnimation(g2, at);
            }
        } else if (gs.getHealth() <= 45) {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(tex.player, at, null);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            if (gs.getPlayerHit())
                hit.drawAnimation(g2, at);
            alert.drawAnimation(g2, at);          
        }

    }
    
    private void blinkingTimer() {
        long currentTime = System.currentTimeMillis();
        final long threshold = 100;
        if (currentTime - firstTime > threshold && blinkingTime < 11) {
            blinking = !blinking;
            firstTime = currentTime;
            blinkingTime++;
        }
    }
    
    private void playerReaction() {
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ObjectId.CollisionBlock) {
                if (gs.getBlockCollision()) {
                    playerVsTileCollision(tempObject.deltaPoints());
                }
            }
            if (tempObject.getId() == ObjectId.Chain) {
                if (gs.getBlockCollision()) 
                    playerVsTileCollision(tempObject.deltaPoints());
            }
            if (tempObject.getId() == ObjectId.Projectile && 
                tempObject.type() == "enemy") 
            {
                Point2D.Float bullet = new Point2D.Float();
                if (sPoints[0].x > tempObject.getX())
                    bullet = new Point2D.Float(tempObject.getX() + tempObject.getW(), 
                                               tempObject.getY());
                else
                    bullet = new Point2D.Float(tempObject.getX() - tempObject.getW(), 
                                               tempObject.getY());
                                               
                if (Helper.inside(bullet, points()) == true) {
                    if (gs.getShieldState()) {
                        gs.setShieldHealth(-5);
                        gs.setShieldHit(true);
                    } else {
                        gs.setHealth(-5); 
                        gs.setPlayerHit(true);                 
                    }
                    handler.removeObject(tempObject);
                }
            }
        }
    }

    private void playerVsTileCollision(Point2D.Float p) {
        for (int i = 0; i < sPoints.length; i++) {
            sPoints[i].x += p.x;
            sPoints[i].y += p.y;
        }            
    }

    private void shipMovement() {
        // Move forward and backward
        for (int i = 0; i < sPoints.length; i++)
            sPoints[i] = Helper.movement(velY, sPoints, i);
        // Rotate left and right from center
        for (int i = 0; i < sPoints.length - 1; i++)
            sPoints[i + 1] = Helper.rotation(velX, sPoints, i + 1);
        // Update object ship position
        x = sPoints[0].x;
        y = sPoints[0].y;
        gs.setBlockCollision(false);
    }
    
    private void shipBounds(float angle) {
        // Ship points respect the ship center point.
        sPoints[0] = new Point2D.Float(x, y);
        sPoints[1] = new Point2D.Float(x, y - (h / 2));
        sPoints[2] = new Point2D.Float(x - (w / 2), y + (h / 2));
        sPoints[3] = new Point2D.Float(x, y + (h / 3));
        sPoints[4] = new Point2D.Float(x + (w / 2), y + (h / 2));
        for (int i = 0; i < sPoints.length - 1; i++)
            sPoints[i + 1] = Helper.rotation(angle, sPoints, i + 1);
    }

    public Point2D.Float[] points() {
        Point2D.Float[] points = new Point2D.Float[5];
        for (int i = 0; i < points.length; i++)
            points[i] = sPoints[i];
        return points;
    }   
}
