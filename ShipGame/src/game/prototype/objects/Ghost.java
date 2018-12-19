package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;
import java.util.Random;

import game.prototype.Animation;
import game.prototype.Game;
import game.prototype.HUD;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class Ghost extends GameObject {

    private Handler handler;
    private TextureManager tex = Game.getTexInstance();
    private Animation idle, explosion, explosion1, explosion2, explosion3, explosion4;
    private Random r = new Random();
    private int choose = 0, enemyHP = 100;
    private int ex, ey;
    private long firstTime;

    public Ghost(float x, float y, float w, float h, Handler handler, ObjectId id) {
        super(x, y, w, h, id);
        this.handler = handler;
        ex = (int) (x + w / 2);
        ey = (int) (y + h / 2);
        firstTime = System.currentTimeMillis();

        idle = new Animation(4, tex.ghostIdle);
        explosion = new Animation(6, tex.explosion);
        explosion1 = new Animation(3, tex.explosion);
        explosion2 = new Animation(3, tex.explosion);
        explosion3 = new Animation(5, tex.explosion);
        explosion4 = new Animation(2, tex.explosion);
    }

    public void update(LinkedList<GameObject> object) {
        enemyHP = Helper.clamp(enemyHP, 0, 100);
        if (enemyHP == 0) {
            HUD.POINTS = 250;
            explosion.runAnimationOnce();
            explosion1.runAnimationOnce();
            explosion3.runAnimationOnce();
            if (explosion1.isDone == true) {
                explosion2.runAnimationOnce();
                explosion4.runAnimationOnce();
            }
        } else {
            idle.runAnimation();
            randomMovement();
            shootingTimer();
        }
        projectileCollision();
    }

    public void render(Graphics2D g2) {
        if (enemyHP == 0) {
            explosion.drawAnimation(g2, (int) (ex-(w/3)/2), 
                                    (int) (ey-(h/3)/2), 
                                    (int) (w/3), (int) (h/3));

            explosion1.drawAnimation(g2, (int) (ex-(w/3)/2)-10, 
                                    (int) (ey-(h/3)/2)-10, 
                                    (int) (w/3), (int) (h/3));

            explosion3.drawAnimation(g2, (int) (ex-(w/3)/2)-10, 
                                    (int) (ey-(h/3)/2)+10, 
                                    (int) (w/3), (int) (h/3));
        }
        if (explosion1.isDone == true) {
            explosion2.drawAnimation(g2, (int) (ex-(w/3)/2)+10, 
                                    (int) (ey-(h/3)/2)+10, 
                                    (int) (w/3), (int) (h/3));

            explosion4.drawAnimation(g2, (int) (ex-(w/3)/2)+10, 
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

        if (ex < x || ey < y || (ex < x && ey < y) || (ex < x && ey > y + h) || 
            ex > x + w || ey > y + h || (ex > x + w && ey < y) || (ex > x + w && 
            ey > y + h)) 
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
            Point2D.Float[] points = new Point2D.Float[2];

            points[0] = new Point2D.Float(ex, ey);
            points[1] = handler.player.get(0).points()[0];

            handler.addObject(new Projectile(ex, ey, 5, 20, 2, "enemy",
                                            Helper.bulletAngle(points), 
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
                    explosion.hasStarted == false) 
                {
                    enemyHP -= 50;
                    handler.removeObject(tempObject);
                }
            }
            if (enemyHP == 0 && explosion4.isDone == true) {
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
