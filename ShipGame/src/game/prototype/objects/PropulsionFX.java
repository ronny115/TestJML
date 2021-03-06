package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
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

public class PropulsionFX extends GameObject {
    
    private Handler handler;
    private GameStates gs;
    private TextureManager tex = Game.getTexInstance();
    private Animation propulsion;
    private AffineTransform at;
    
    private long firstTime;
    private boolean blinking;
    private int blinkingTime;
    
    public PropulsionFX(float x, float y, float w, float h, Handler handler,
                        GameStates gs, ObjectId id) 
    {
        super(x, y, w, h, id);
        this.setRenderPriority(2);
        this.handler = handler;
        this.gs = gs;
        firstTime = System.currentTimeMillis();
        propulsion = new Animation(tex.propulsion);     
    }

    public void update(LinkedList<GameObject> object) {
        if (handler.player.size() == 0) {
            handler.object.remove(this);
        } else {
            at = AffineTransform.getTranslateInstance(x, y);
            at.rotate(Helper.angle(handler.player.get(0).points()[0], 
                                   handler.player.get(0).points()[1]) 
                                   + Math.toRadians(90));
            at.translate(-(w / 2), +(h / 5));
            at.scale(w / (tex.propulsion[0].getWidth()), 
                     h / (tex.propulsion[0].getHeight() + 20));
            propulsion.runAnimation(3);
            x = handler.player.get(0).points()[3].x;
            y = handler.player.get(0).points()[3].y;
            blinkingTimer();
        }
    }

    public void render(Graphics2D g2) {
        if(gs.getPropulsionState() && gs.getHealth() > 0) {
            if(blinking || blinkingTime > 10) {
            propulsion.drawAnimation(g2, at);
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
