package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;

import game.prototype.Animation;
import game.prototype.Game;
import game.prototype.HUD;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class Shield extends GameObject {
    
    private Handler handler;
    private TextureManager tex = Game.getTexInstance();
    private Animation shieldAnimation;
    private AffineTransform at;
    public static boolean HIT, SHIELD;
    
    public Shield(float x, float y, float w, float h, Handler handler, ObjectId id) {
        super(x, y, w, h, id);
        this.setRenderPriority(3);
        this.handler = handler;
        shieldAnimation = new Animation(2, tex.shield);
    }

    public void update(LinkedList<GameObject> object) {      
        if (SHIELD == true) {
            at = AffineTransform.getTranslateInstance(x, y);
            at.rotate(Helper.getAngle(handler.player.get(0).points()));
            at.translate(-(w / 2), -(h / 2));
            at.scale(w / tex.shield[0].getWidth(), h / (tex.shield[0].getHeight()));
            x = handler.player.get(0).points()[0].x;
            y = handler.player.get(0).points()[0].y;
      
            if (HIT == true) {
                shieldAnimation.runAnimationOnce();
            }
            if (HUD.SHIELD_HEALTH > 0 && shieldAnimation.isDone == true) {
                HIT = false;
                shieldAnimation.isDone = false;
            }
        }
        itemGrab(); 
    }

    public void render(Graphics2D g2) {
        if (SHIELD == false) {
            g2.draw(shieldItem()); 
        } else if(SHIELD == true && HIT == true) {
            shieldAnimation.drawAnimation(g2, at);
        } 
    }
    
    private void itemGrab() {
        if(handler.player.size() > 0) {
            for (int i = 0; i < handler.player.get(0).points().length; i++) {
                if (shieldItem().contains(handler.player.get(0).points()[i])) {
                    SHIELD = true;
                }
            }
            if(HUD.SHIELD_HEALTH == 0 && shieldAnimation.isDone == true) {
                SHIELD = false;
                handler.removeObject(this);
            } 
        }
    }
    
    private Rectangle2D shieldItem() {
        return new Rectangle2D.Float(x-w/2, y-h/2, w, h);
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
