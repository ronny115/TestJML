package game.prototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import game.prototype.framework.ObjectId;
import game.prototype.objects.Block;
import game.prototype.objects.CollisionBlock;

public class DynamicLoading {
	private Handler handler;
	private int[] isLoaded = new int[4096];
	
	public DynamicLoading(Handler handler) {
		this.handler = handler;
	}
	
	public void update(Point2D.Float playerPos, ArrayList<float[]> coords, ArrayList<int[]> colorRGB, float size) {
		//Load on screen objects
		for (int i = 0; i < coords.size(); i++) {
			if(coords.get(i)[0] > playerPos.x - Game.WIDTH/2 && coords.get(i)[0] < playerPos.x + Game.WIDTH/2 && 
			coords.get(i)[1] > playerPos.y - Game.HEIGHT/2 && coords.get(i)[1] < playerPos.y + Game.HEIGHT/2) {
				//load only blocks color
				if(colorRGB.get(i)[0] == 255 && colorRGB.get(i)[1]  == 255 && colorRGB.get(i)[2]  == 255 && isLoaded[i] == 0) {
					handler.addObject(new Block(coords.get(i)[0], coords.get(i)[1], size, size, ObjectId.Block));
					isLoaded[i] = 1;
				}
				if(colorRGB.get(i)[0] == 255 && colorRGB.get(i)[1]  == 0 && colorRGB.get(i)[2]  == 0 && isLoaded[i] == 0) {
					handler.addObject(new CollisionBlock(coords.get(i)[0], coords.get(i)[1], size, size, handler, ObjectId.CollisionBlock));
					isLoaded[i] = 1;
				}
			}			
		}
		//Delete off screen objects
		for (int i = 0; i < coords.size(); i++) {
			if(coords.get(i)[0] < playerPos.x - Game.WIDTH/2 || coords.get(i)[0] > playerPos.x + Game.WIDTH/2 && isLoaded[i] == 1) {
				for (int j = 0; j < handler.object.size(); j++) {
					if(handler.object.get(j).getX() < playerPos.x - Game.WIDTH/2 || handler.object.get(j).getX() > playerPos.x + Game.WIDTH/2) {
						handler.removeObject(handler.object.get(j));
					}	
				}
				isLoaded[i] = 0;
			}
			if(coords.get(i)[1] < playerPos.y - Game.HEIGHT/2 || coords.get(i)[1] > playerPos.y + Game.HEIGHT/2 && isLoaded[i] == 1) {
				for (int j = 0; j < handler.object.size(); j++) {
					if(handler.object.get(j).getY() < playerPos.y - Game.HEIGHT/2 || handler.object.get(j).getY() > playerPos.y + Game.HEIGHT/2) {
						handler.removeObject(handler.object.get(j));
					}	
				}
				isLoaded[i] = 0;
			}
		}
	//End Function
	}
//End Class
}