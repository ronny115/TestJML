package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.awt.geom.Rectangle2D;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.framework.Animation;
import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.framework.PlayerObject;
import game.prototype.framework.GameStates;
import game.prototype.framework.TextureManager;

public class ExplosiveMine extends GameObject {

    private Handler handler;
    private GameStates gs;
    private TextureManager tex = Game.getTexInstance();
    private Animation explosion;
    private boolean isHit;
    private int player1;

    public ExplosiveMine(float x, float y, float w, float h, Handler handler, 
                         GameStates gs, ObjectId id) {
        super(x, y, w, h, id);
        this.setRenderPriority(2);
        this.handler = handler;
        this.gs = gs;
        explosion = new Animation(tex.explosion);
    }

    public void update(LinkedList<GameObject> object) {
        player1 = handler.player.size();
        projectileCollision();
        if (player1 > 0) 
            playerCollision();
        if (isHit) 
            explosion.runAnimationOnce(3);
        if (explosion.isDone) {
            gs.setObjState(this.x, this.y);
            handler.removeObject(this);
        }
    }

    public void render(Graphics2D g2) {
        if (isHit)
            explosion.drawAnimation(g2, (int) (x - w/2) - 10, (int) (y - h/2) - 10, 
                                    (int) w + 20, (int) h + 20);
        else
            g2.drawImage(tex.mine, (int) (x - w/2), (int) (y- h/2), 
                         (int) w, (int) h, null);
    }

    public Rectangle2D bounds() {
        return new Rectangle2D.Float(x - w/2, y - h/2, w, h);
    }

    private void projectileCollision() {
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ObjectId.Projectile) {
                Point2D.Float bullet = new Point2D.Float(tempObject.getX(),
                                                        tempObject.getY());
                if (bounds().contains(bullet) && !isHit && 
                    tempObject.type() == "player") {
                    isHit = true;
                    gs.setPoints(gs.getPoints() + 100);
                    handler.removeObject(tempObject);
                }
            }
        }
    }

    private void playerCollision() {
        PlayerObject player = handler.player.get(0);
        for (int i = 0; i < player.points().length; i++) {
            if (bounds().contains(player.points()[i])) 
            {
                isHit = true;
                if (gs.getShieldState() && !explosion.hasStarted) {
                    gs.setShieldHealth(-50);
                    gs.setShieldHit(true);
                } else if (!explosion.hasStarted) {
                    gs.setHealth(-10);
                    gs.setPlayerHit(true);
                }
            }
        }
    }

    public Float deltaPoints() {
        return null;
    }

    public String type() {
        return null;
    }
}
