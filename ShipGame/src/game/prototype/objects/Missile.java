package game.prototype.objects;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Float;
import java.util.ArrayList;
import java.util.LinkedList;

import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;

public class Missile extends GameObject {

    private Handler handler;
    private Point2D.Float velocity, position, target;
    private ArrayList<Point2D.Float> list = new ArrayList<Point2D.Float>();

    public Missile(float x, float y, float w, float h, Handler handler, ObjectId id) {
        super(x, y, w, h, id);
        this.handler = handler;
        this.setRenderPriority(3);
        this.position = new Point2D.Float(x, y);
        this.target = new Point2D.Float(handler.player.get(0).getX(),handler.player.get(0).getY());
    }
    public void update(LinkedList<GameObject> object) {
        movement();
        if (handler.player.size() > 0)
            target = new Point2D.Float(handler.player.get(0).getX(),handler.player.get(0).getY());

        list.add(new Point2D.Float(x, y));
    }

    public void render(Graphics2D g2) {
        g2.drawOval((int)x-15, (int)y-15, 30, 30);

        for (int i = 0; i<list.size(); i++)
            g2.drawOval((int)list.get(i).x, (int)list.get(i).y, 1, 1);
    }

    private void movement() {
        velocity = Helper.vectorize(position, target);
        velocity.x *= 0.015f;
        velocity.y *= 0.015f;
        position.x += velocity.x;
        position.y += velocity.y;

        //update object position
        x = position.x;
        y = position.y;
        
    }

    public Float deltaPoints() {
        return null;
    }

    public String type() {
        return null;
    }

}
