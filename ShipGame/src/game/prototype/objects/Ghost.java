package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;
import java.util.Random;

import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.framework.Animation;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.GameStates;
import game.prototype.framework.TextureManager;

public class Ghost extends GameObject {

    private Handler handler;
    private GameStates gs;
    private TextureManager tex = Game.getTexInstance();
    private Animation idle, explosion[] = new Animation[5];
    private Random r = new Random();
    private int choose, runOnce, enemyHP = 100;
    private int ex, ey;
    private long firstTime;

    public Ghost(float x, float y, float w, float h, Handler handler, GameStates gs, ObjectId id) {
        super(x, y, w, h, id);
        this.setRenderPriority(2);
        this.handler = handler;
        this.gs = gs;
        ex = (int) x;
        ey = (int) y;
        firstTime = System.currentTimeMillis();

        idle = new Animation(tex.ghostIdle);
        explosion[0] = new Animation(tex.explosion);
        explosion[1] = new Animation(tex.explosion);
        explosion[2] = new Animation(tex.explosion);
        explosion[3] = new Animation(tex.explosion);
        explosion[4] = new Animation(tex.explosion);
    }

    public void update(LinkedList<GameObject> object) {
        enemyHP = Helper.clamp(enemyHP, 0, 100);
        if (enemyHP == 0) {
            if (runOnce == 0) {
                gs.setPoints(gs.getPoints()+250);
                runOnce = 1;
            }
            explosion[0].runAnimationOnce(6);
            explosion[1].runAnimationOnce(3);
            explosion[3].runAnimationOnce(5);
            if (explosion[1].isDone == true) {
                explosion[2].runAnimationOnce(3);
                explosion[4].runAnimationOnce(2);
            }
        } else {
            idle.runAnimation(4);
            randomMovement();
            if (handler.player.size() > 0)
                shootingTimer();
        }    
        projectileCollision();
    }

    public void render(Graphics2D g2) {
        if (enemyHP == 0) {
            explosion[0].drawAnimation(g2, (int) (ex-(w/3)/2), 
                                      (int) (ey-(h/3)/2), 
                                      (int) (w/3), (int) (h/3));

            explosion[1].drawAnimation(g2, (int) (ex-(w/3)/2)-10, 
                                      (int) (ey-(h/3)/2)-10, 
                                      (int) (w/3), (int) (h/3));

            explosion[3].drawAnimation(g2, (int) (ex-(w/3)/2)-10, 
                                      (int) (ey-(h/3)/2)+10, 
                                      (int) (w/3), (int) (h/3));
        }
        if (explosion[1].isDone == true) {
            explosion[2].drawAnimation(g2, (int) (ex-(w/3)/2)+10, 
                                      (int) (ey-(h/3)/2)+10, 
                                      (int) (w/3), (int) (h/3));

            explosion[4].drawAnimation(g2, (int) (ex-(w/3)/2)+10, 
                                      (int) (ey-(h/3)/2)-10,
                                      (int) (w/3), (int) (h/3));
        }
        if (enemyHP > 0)
            idle.drawAnimation(g2, (int) (ex-(w/3)/2), 
                              (int) (ey-(h/3)/2), 
                              (int) (w/3), (int) (h/3));
    }

    private void randomMovement() {
        ex += velX;
        ey += velY;
        choose = r.nextInt(10);

        if (ex < (x-w/2) || ey < (y-h/2) || 
            ex > (x+w/2) || ey > (y+h/2)) 
        {
            ex -= (int) (velX * 2);
            ey -= (int) (velY * 2);
            velX *= -1;
            velY *= -1;
        }
        if (choose == 0) {
            velX = (r.nextInt(8) - 4);
            velY = (r.nextInt(8) - 4);
        }
    }

    private void shootingTimer() {     
        long currentTime = System.currentTimeMillis();
        final long threshold = 1000;
        if (currentTime - firstTime > threshold) {
            handler.addObject(
                new Projectile(ex, ey, 5, 20, 2, "enemy", gs,
                              (float)(Helper.angle(new Point2D.Float(ex, ey), 
                                                   handler.player.get(0).points()[0]) 
                                                   + Math.toRadians(90)), 
                              ObjectId.Projectile));
            firstTime = currentTime;
        }
    }

    private void projectileCollision() {
        for (int i = 0; i < handler.object.size(); i++) {
            GameObject tempObject = handler.object.get(i);
            if (tempObject.getId() == ObjectId.Projectile) {
                if (tempObject.type() == "player" && 
                    tempObject.getX() > (ex-(w/3)/2)&& 
                    tempObject.getX() < (ex+(w/3)/2)&& 
                    tempObject.getY() > (ey-(h/3)/2)&& 
                    tempObject.getY() < (ey+(h/3)/2)&& 
                    !explosion[0].hasStarted) 
                {
                    enemyHP -= 50;
                    handler.removeObject(tempObject);
                }
            }
            if (explosion[4].isDone) {
                gs.setObjState(this.x, this.y);
                handler.removeObject(this);
            }
        }
    }

    public Float deltaPoints() {
        return null;
    }

    public boolean collision() {
        return false;
    }

    public String type() {
        return null;
    }
}
