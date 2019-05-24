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
import game.prototype.framework.GameStates;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class LaserUp extends GameObject {

    private Handler handler;
    private TextureManager tex = Game.getTexInstance();
    private Animation laserUp;
    private AffineTransform at;
    private GameStates gs;
    private boolean isGrab;

    public LaserUp(float x, float y, float w, float h, Handler handler, 
                   GameStates gs, ObjectId id) {
        super(x, y, w, h, id);
        this.setRenderPriority(3);
        this.handler = handler;
        this.gs = gs;
        laserUp = new Animation(tex.laserUp);
    }

    public void update(LinkedList<GameObject> object) {
        if (isGrab) {
            at = AffineTransform.getTranslateInstance(x - (w/2)*1.5, y - (h/2)*1.5);
            at.scale(1.5, 1.5);
            laserUp.runAnimationOnce(5); 
            x = handler.player.get(0).points()[0].x;
            y = handler.player.get(0).points()[0].y;
            if (gs.getShootMode() == 0)
                gs.setShootMode(1);
        }
        itemGrab();
    }

    public void render(Graphics2D g2) {
        if (isGrab) 
            laserUp.drawAnimation(g2, at);
        else 
            g2.drawImage(tex.laserItem, (int)laserItem().getMinX(), 
                        (int)laserItem().getMinY()-5, (int)w, (int)h, null);
        

    }

    private void itemGrab() {
        if (handler.player.size() > 0) {
            for (int i = 1; i < handler.player.get(0).points().length; i++) {
                if (laserItem().contains(handler.player.get(0).points()[i])) {
                    isGrab = true;
                    gs.setObjState(this.x, this.y);
                    if (gs.getShootMode() == 1) 
                        gs.setShootMode(2);
            }
        }
    }
    if (laserUp.isDone)
        handler.removeObject(this);
}

private Rectangle2D laserItem() {
    if (isGrab)
        return new Rectangle2D.Float(handler.player.get(0).points()[0].x - 2,
                                     handler.player.get(0).points()[0].y - 2, 4, 4);
    else
        return new Rectangle2D.Float(x - w/2, (y - h / 2)+5, 31, 31);
}

    public Float deltaPoints() {
        return null;
    }

    public String type() {
        return null;
    }

}