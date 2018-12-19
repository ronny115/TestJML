package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class Block extends GameObject {
    private Path2D poly = new Path2D.Double();

    public Block(float x, float y, float w, float h, ObjectId id) {
        super(x, y, w, h, id);
        poly = tile();
    }

    public void update(LinkedList<GameObject> object) {

    }

    public void render(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.draw(poly);
    }

    private Path2D tile() {
        Point2D.Double center = new Point2D.Double(x, y);
        Point2D.Double radius = new Point2D.Double(w, h);
        double xpoints[] = new double[6];
        double ypoints[] = new double[6];
        Path2D polygon = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            xpoints[i] = center.x + radius.x * Math.cos(i * 2 * Math.PI / 6D);
            ypoints[i] = center.y + radius.y * Math.sin(i * 2 * Math.PI / 6D);
        }
        polygon.moveTo(xpoints[0], ypoints[0]);
        for (int i = 1; i < xpoints.length; ++i) {
            polygon.lineTo(xpoints[i], ypoints[i]);
        }
        polygon.closePath();
        return polygon;
    }

    public Point2D.Float deltaPoints() {
        return null;
    }

    public boolean collision() {
        return false;
    }

    public String type() {
        return null;
    }
}
