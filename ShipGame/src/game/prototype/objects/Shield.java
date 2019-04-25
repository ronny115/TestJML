package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.framework.Animation;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.GameStates;
import game.prototype.framework.TextureManager;

public class Shield extends GameObject {

    private Handler handler;
    private GameStates gs;
    private TextureManager tex = Game.getTexInstance();
    private Animation shieldAni;
    private AffineTransform at;
    private boolean isGrab;

    public Shield(float x, float y, float w, float h, Handler handler, GameStates gs, ObjectId id) {
        super(x, y, w, h, id);
        this.setRenderPriority(3);
        this.handler = handler;
        this.gs = gs;
        shieldAni = new Animation(tex.playerShield);
    }

    public void update(LinkedList<GameObject> object) {
        if (isGrab) {
            at = AffineTransform.getTranslateInstance(x, y);
            at.rotate(Helper.getAngle(handler.player.get(0).points()));
            at.translate(-(w / 2), -(h / 2));
            at.scale(w / tex.playerShield[0].getWidth(), h / (tex.playerShield[0].getHeight()));
            x = handler.player.get(0).points()[0].x;
            y = handler.player.get(0).points()[0].y;

            if (gs.getShieldHit()) {
                shieldAni.runAnimationOnce(2);
            }
            if (gs.getShieldHealth() > 0 && shieldAni.isDone == true) {
                gs.setShieldHit(false);
                shieldAni.isDone = false;
            }
        }
        itemGrab();
    }

    public void render(Graphics2D g2) {
        if (isGrab && gs.getShieldHit()) {
            shieldAni.drawAnimation(g2, at);
        } else if (!isGrab) {
            g2.drawImage(tex.shieldItem, (int) shieldItem().getMinX(), (int) shieldItem().getMinY(),
                        (int) shieldItem().getWidth(), (int) shieldItem().getHeight(), null);
        }
    }

    private void itemGrab() {
        if (handler.player.size() > 0) {
            for (int i = 1; i < handler.player.get(0).points().length; i++) {
                if (shieldItem().contains(handler.player.get(0).points()[i]) && !isGrab) {
                    if (gs.getShieldState()) {
                        if(gs.getShieldHealth() == 100) {
                            gs.setPoints(gs.getPoints() + 50);
                        }
                        gs.setShieldHealth(100);
                        handler.removeObject(this);
                    }
                    isGrab = true;
                    gs.setShieldState(true);
                    gs.setObjState(this.x, this.y);
                }
            }
            if (gs.getShieldHealth() == 0 && shieldAni.isDone == true) {
                gs.setShieldState(false);
                gs.setShieldHit(false);
                gs.setShieldHealth(100);
                isGrab = false;
                handler.removeObject(this);
            }
        }
    }

    private Rectangle2D shieldItem() {
        if (isGrab)
            return new Rectangle2D.Float(handler.player.get(0).points()[0].x - 2,
                                         handler.player.get(0).points()[0].y - 2, 4, 4);
        else
            return new Rectangle2D.Float(x - w / 2, y - h / 2, 30, 40);
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
