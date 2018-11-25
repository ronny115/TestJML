package game.prototype;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.framework.GameObject;
import game.prototype.framework.PlayerObject;

public class Handler {
	public LinkedList<PlayerObject> player = new LinkedList <PlayerObject>();
	private PlayerObject tempPlayer;
	public LinkedList<GameObject> object = new LinkedList <GameObject>();
	private GameObject tempObject;

	public void update() {
		for(int i =0; i < player.size(); i++) {
			tempPlayer = player.get(i);
			tempPlayer.updatePlayer(player);
		}
		for(int i =0; i < object.size(); i++) {
			tempObject = object.get(i);
			tempObject.update(object);
		}
	}
	
	public void render(Graphics2D g2) {
		for(int i =0; i < player.size(); i++) {
			tempPlayer = player.get(i);
			tempPlayer.renderPlayer(g2);
		}
		for(int i =0; i < object.size(); i++) {
			tempObject = object.get(i);
			tempObject.render(g2);
		}
	}
	
	public void addPlayer(PlayerObject player) {
		this.player.add(player);
	}
	
	public void removePlayer(PlayerObject player) {
		this.player.remove(player);
	}
	
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
	
	public Point2D.Float vectorize(Point2D.Float pointA, Point2D.Float pointB) {
		Point2D.Float vector = new Point2D.Float();
		vector.x = (pointB.x - pointA.x);
		vector.y = (pointB.y - pointA.y);
		return vector;	
	}
	
	public float dotProduct(Point2D.Float vec1, Point2D.Float vec2) {
		float dp = ((vec1.x * vec2.x) + (vec1.y * vec2.y));
		return dp;
	}
	
	public static int clamp(int var, int min, int max) {
		if(var >= max) {
			return var = max;
		} else if (var <= min) {
			return var = min;
		} else {
			return var;
		}
	}
}
