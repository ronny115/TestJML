package game.prototype.objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.HUD;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.PlayerId;
import game.prototype.framework.PlayerObject;
import game.prototype.framework.TextureManager;

public class PlayerShip extends PlayerObject {

	private Handler handler;
	private TextureManager tex = Game.getTexInstance();
	private AffineTransform at;
	private Point2D.Float[] shipPoints = new Point2D.Float[5];
	private Point2D.Float newP = new Point2D.Float();
	private boolean isColliding = false;
	
	public PlayerShip(float x, float y, float w, float h, Handler handler, PlayerId id) {
		super(x, y, w, h, id);
		this.handler = handler;
		//Ship points respect the ship center point.
		shipPoints[0] = new Point2D.Float(x, y);
		shipPoints[1] = new Point2D.Float(x,       y-(h/2));
		shipPoints[2] = new Point2D.Float(x-(w/2), y+(h/2));
		shipPoints[3] = new Point2D.Float(x,       y+(h/3));
		shipPoints[4] = new Point2D.Float(x+(w/2), y+(h/2));
	}
	
	public void updatePlayer(LinkedList<PlayerObject> object) {
		at = AffineTransform.getTranslateInstance(x, y);
		at.rotate(Math.toRadians(Helper.getAngle(shipPoints[1],shipPoints[0])-90));
	    at.translate(-(w/2), -(h/2));
	    at.scale((w/tex.player[0].getWidth()), (h/tex.player[0].getHeight()));
	    
		PlayerVsTile();
		shipMovement();	
		
		handler.addObject(new TrailFx(shipPoints[3].x, shipPoints[3].y, w, h, 0.06f, Color.GRAY, handler, ObjectId.TrailFx));
	}

	public void renderPlayer(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(tex.player[0], at, null);
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
		//g2.setColor(Color.RED);
		//g2.draw(ship());
	}
	
	private void PlayerVsTile() {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);		
			if(tempObject.getId() == ObjectId.CollisionBlock) {
				newP = tempObject.getP();
				isColliding = tempObject.getCollision();
				if (isColliding == true) {
					HUD.HEALTH -= 1;
					PlayerVsTileReaction(newP);
				}
			}
		}		
	}
	
	private void PlayerVsTileReaction(Point2D.Float p) {
		shipPoints[0].x += p.x;
		shipPoints[0].y += p.y;
		 
		shipPoints[1].x += p.x;
		shipPoints[1].y += p.y;
		
		shipPoints[2].x += p.x;
		shipPoints[2].y += p.y;
		
		shipPoints[3].x += p.x;
		shipPoints[3].y += p.y;
		
		shipPoints[4].x += p.x;
		shipPoints[4].y += p.y;
	}
	
	private Path2D ship() {
		Path2D polygon = new Path2D.Double();
		float xpoints[] = new float[4];
		float ypoints[] = new float[4];	
		
		for (int i = 0; i < xpoints.length; i++) {
			xpoints[i] = shipPoints[i+1].x;
			ypoints[i] = shipPoints[i+1].y;
		}		
		polygon.moveTo(xpoints[0], ypoints[0]);
		for(int i = 0; i < xpoints.length; ++i) polygon.lineTo(xpoints[i], ypoints[i]);
		polygon.closePath();
		return polygon;
	}
	
	private void shipMovement() {
		//Move forward and backward
		for (int i = 0; i < shipPoints.length; i++) 
			shipPoints[i] = Helper.movement(velY, shipPoints, i);
			
		//Rotate left and right from center
		for (int i = 0; i < shipPoints.length-1; i++) 
			shipPoints[i+1] = Helper.rotation(velX, shipPoints, i+1);

		//Update object ship position
		x = shipPoints[0].x; y = shipPoints[0].y;		
	}
	
	public Point2D.Float[] getPoints() {
		Point2D.Float[] points = new Point2D.Float[5];
		for (int i = 0; i < points.length; i++) points[i] = shipPoints[i];
		return points;
	}
}
