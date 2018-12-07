package game.prototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.objects.Block;
import game.prototype.objects.CollisionBlock;
import game.prototype.objects.ExplosiveMine;

public class DynamicLoading {
	private Handler handler;

	public DynamicLoading(Handler handler) {
		this.handler = handler;
	}
	
	public void update(Point2D.Float playerPos, ArrayList<float[]> coords, ArrayList<int[]> colorRGB, float size) {		
		for (int i = 0; i < coords.size(); i++) {
			//Load on screen objects
			if(coords.get(i)[0] > playerPos.x - Game.WIDTH/2 && coords.get(i)[0] < playerPos.x + Game.WIDTH/2 && 
			coords.get(i)[1] > playerPos.y - Game.HEIGHT/2 && coords.get(i)[1] < playerPos.y + Game.HEIGHT/2) {
				
				if(colorRGB.get(i)[0] == 255 && colorRGB.get(i)[1]  == 255 && colorRGB.get(i)[2]  == 255 && coords.get(i)[2] == 0) {
					handler.addObject(new Block(coords.get(i)[0], coords.get(i)[1], size, size, ObjectId.Block));
				}
				if(colorRGB.get(i)[0] == 255 && colorRGB.get(i)[1]  == 0 && colorRGB.get(i)[2]  == 0 && coords.get(i)[2] == 0) {
					handler.addObject(new CollisionBlock(coords.get(i)[0], coords.get(i)[1], size, size, handler, ObjectId.CollisionBlock));
				}
				if(colorRGB.get(i)[0] == 0 && colorRGB.get(i)[1]  == 255 && colorRGB.get(i)[2]  == 0 && coords.get(i)[2] == 0) {
					handler.addObject(new ExplosiveMine(coords.get(i)[0], coords.get(i)[1], size, size, handler, ObjectId.EnemyType1));
				}
				
				coords.get(i)[2] = 1;
			} else {
				coords.get(i)[2] = 0;
			}
			//Delete off screen objects
			if(coords.get(i)[2] != 0) {	
				for(int j = 0; j < handler.object.size(); j++) {
					GameObject tempObject = handler.object.get(j);
					if(tempObject.getX() < playerPos.x - Game.WIDTH/2 || tempObject.getX() > playerPos.x + Game.WIDTH/2) {		
						handler.removeObject(tempObject);
					}					
					if(tempObject.getY() < playerPos.y - Game.HEIGHT/2 || tempObject.getY() > playerPos.y + Game.HEIGHT/2) {
						handler.removeObject(tempObject);
					}
				}			
			}			
		}
	}
}