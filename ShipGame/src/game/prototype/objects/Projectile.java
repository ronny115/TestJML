package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.framework.GameObject;
import game.prototype.framework.GameStates;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class Projectile extends GameObject {

    private TextureManager tex = Game.getTexInstance();
    private GameStates gs;
    private int speed;
    private float angle;
    private String type;

    public Projectile(float x, float y, float w, float h, int speed, String type, 
                      GameStates gs, float angle, ObjectId id) 
    {
        super(x, y, w, h, id);
        this.setRenderPriority(3);
        this.speed = speed;
        this.angle = angle;
        this.type = type;
        this.gs = gs;
    }

    public void update(LinkedList<GameObject> object) {
        x += (float) speed * Math.sin(angle);
        y -= (float) speed * Math.cos(angle);
    }

    public void render(Graphics2D g2) {
        AffineTransform at = AffineTransform.getTranslateInstance(x, y);
        at.rotate(angle);
        at.translate(-(w / 2), -h);
        if (type == "player") {
            if (gs.getShootMode() == 0) {
                at.scale((w / tex.bullet[0].getWidth()), (h / tex.bullet[0].getHeight()));
                g2.drawImage(tex.bullet[0], at, null);
            } else if (gs.getShootMode() == 1) {
                at.scale((w / tex.bullet[2].getWidth()), (h / tex.bullet[2].getHeight()));
                g2.drawImage(tex.bullet[2], at, null);
            } else if (gs.getShootMode() == 2) {
                at.scale((w / tex.bullet[3].getWidth()), (h / tex.bullet[2].getHeight()));
                g2.drawImage(tex.bullet[3], at, null);
            }     
        }
        if (type == "enemy") {
            at.scale((w / tex.bullet[1].getWidth()), (h / tex.bullet[1].getHeight()));
            g2.drawImage(tex.bullet[1], at, null);
        }

    }

    public String type() {
        return type;
    }

    public Float deltaPoints() {
        return null;
    }

    public boolean collision() {
        return false;
    }
}
