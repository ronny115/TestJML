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
import game.prototype.framework.States;
import game.prototype.framework.TextureManager;

public class ExplosiveMine extends GameObject {

    private Handler handler;
    private States states;
    private TextureManager tex = Game.getTexInstance();
    private Animation explosion;
    private boolean isHit;
    private boolean isDamaged;
    private int player1;

    public ExplosiveMine(float x, float y, float w, float h, Handler handler, 
                         States states, ObjectId id) {
        super(x, y, w, h, id);
        this.setRenderPriority(2);
        this.handler = handler;
        this.states = states;
        explosion = new Animation(tex.explosion);
    }

    public void update(LinkedList<GameObject> object) {
        player1 = handler.player.size();
        if (isHit == true) {
            explosion.runAnimationOnce(3);
        }
        if (explosion.isDone == true) {
            isHit = false;
            isDamaged = false;
        }
        projectileCollision();
        if(player1 > 0) {
            playerCollision();
        }
    }

    public void render(Graphics2D g2) {
        if (isHit == true)
            explosion.drawAnimation(g2, (int) x - 10, (int) y - 10, 
                                    (int) w + 20, (int) h + 20);
        else
            g2.drawImage(tex.explosiveMine[0], (int) x, (int) y, 
                         (int) w, (int) h, null);
    }

    public Rectangle2D bounds() {
        return new Rectangle2D.Float(x, y, w, h);
    }

    private void projectileCollision() {
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ObjectId.Projectile) {
                Point2D.Float bullet = new Point2D.Float(tempObject.getX(),
                                                        tempObject.getY());
                if (bounds().contains(bullet) == true && isHit == false && 
                    tempObject.type() == "player") 
                {
                    isHit = true;
                    states.setPoints(states.getPoints() + 100);
                    handler.removeObject(tempObject);
                }
            }
            if (explosion.isDone == true && isHit == false) {
                states.setObjState(this.x, this.y);
                handler.removeObject(this);
                explosion.isDone = false;
            }
        }
    }

    private void playerCollision() {
        PlayerObject player = handler.player.get(0);
        for (int i = 0; i < player.points().length; i++) {
            if (bounds().contains(player.points()[i]) && 
                explosion.hasStarted == false) 
            {
                isHit = true;
                isDamaged = true;
            }
        }
    }

    public Float deltaPoints() {
        return null;
    }

    public boolean getCollision() {
        if (explosion.hasStarted == true)
            return false;
        return isDamaged;
    }

    public String type() {
        return null;
    }
}
