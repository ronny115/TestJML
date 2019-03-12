package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.Animation;
import game.prototype.Game;
import game.prototype.HUD;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.PlayerId;
import game.prototype.framework.PlayerObject;
import game.prototype.framework.TextureManager;

public class PlayerShip extends PlayerObject {

    private Handler handler;
    private HUD hud;
    private TextureManager tex = Game.getTexInstance();
    private Animation explosion;
    private AffineTransform at;
    private Point2D.Float[] shipPoints = new Point2D.Float[5];
    
    private long firstTime;
    private boolean blinking;
    private int blinkingTime;
    
    public PlayerShip(float x, float y, float w, float h, Handler handler, 
                      HUD hud, PlayerId id)
    {
        super(x, y, w, h, id);
        this.handler = handler;
        this.hud = hud;
        shipBounds();
        firstTime = System.currentTimeMillis();
        explosion = new Animation(8, tex.playerExplosion);     
        handler.addObject(new PropulsionFX(shipPoints[3].x, shipPoints[3].y, 15, 20,
                                           handler, hud, ObjectId.Propulsion));
    }

    public void updatePlayer(LinkedList<PlayerObject> object) {
        if(hud.getHealth() == 0) {
            velX = velY = 0;
            explosion.runAnimationOnce();
        } else {
            Game.GameOver = false;
        }
        if(explosion.isDone) {
            handler.player.clear();
            Game.GameOver = !Game.GameOver;
        }
        at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(Helper.getAngle(points()));
        at.translate(-(w/2), -(h/2));
        at.scale((w / tex.player.getWidth()), (h / tex.player.getHeight()));
        playerHealth();
        shipMovement();
        blinkingTimer();
    }

    public void renderPlayer(Graphics2D g2) {       
        if (hud.getHealth() == 0 && !explosion.isDone) {
            explosion.drawAnimation(g2, (int)(x -w), (int)(y-h), (int)(w*2+(h-w)), (int)h*2);
            
        } else if(hud.getHealth() > 0) {                     
            if(blinking || blinkingTime > 10) {
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(tex.player, at, null);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, 
                                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            }
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
    
    private void playerHealth() {
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ObjectId.CollisionBlock) {
                if (tempObject.getCollision() == true) {
                    hud.setHealth(-1);
                    playerVsTileCollision(tempObject.deltaPoints());
                    tempObject.setCollision(false);
                }
            }
            if (tempObject.getId() == ObjectId.ExplosiveMine) {
                if (tempObject.getCollision() == true) {          
                    if (hud.getShieldState()) {
                        hud.setShieldHealth(-50);
                        hud.setShieldHit(true);
                    } else {
                        hud.setHealth(-10);
                    }
                    
                }
            }
            if (tempObject.getId() == ObjectId.Projectile && 
                tempObject.type() == "enemy") 
            {
                Point2D.Float bullet = new Point2D.Float();
                if (shipPoints[0].x > tempObject.getX())
                    bullet = new Point2D.Float(tempObject.getX()
                                              + tempObject.getW(), tempObject.getY());
                else
                    bullet = new Point2D.Float(tempObject.getX()
                                              - tempObject.getW(), tempObject.getY());
                if (Helper.inside(bullet, points()) == true) {
                    if (hud.getShieldState()) {
                        hud.setShieldHealth(-5);
                        hud.setShieldHit(true);
                    } else {
                        hud.setHealth(-5);
                    }
                    handler.removeObject(tempObject);
                }
            }
        }
    }

    private void playerVsTileCollision(Point2D.Float p) {
        for (int i = 0; i < shipPoints.length; i++) {
            shipPoints[i].x += p.x;
            shipPoints[i].y += p.y;
        }       
    }

    private void shipMovement() {
        // Move forward and backward
        for (int i = 0; i < shipPoints.length; i++)
            shipPoints[i] = Helper.movement(velY, shipPoints, i);
        // Rotate left and right from center
        for (int i = 0; i < shipPoints.length - 1; i++)
            shipPoints[i + 1] = Helper.rotation(velX, shipPoints, i + 1);
        // Update object ship position
        x = shipPoints[0].x;
        y = shipPoints[0].y;
    }
    
    private void shipBounds() {
        // Ship points respect the ship center point.
        shipPoints[0] = new Point2D.Float(x, y);
        shipPoints[1] = new Point2D.Float(x, y - (h / 2));
        shipPoints[2] = new Point2D.Float(x - (w / 2), y + (h / 2));
        shipPoints[3] = new Point2D.Float(x, y + (h / 3));
        shipPoints[4] = new Point2D.Float(x + (w / 2), y + (h / 2));
    }

    public Point2D.Float[] points() {
        Point2D.Float[] points = new Point2D.Float[5];
        for (int i = 0; i < points.length; i++)
            points[i] = shipPoints[i];
        return points;
    }
    
}
