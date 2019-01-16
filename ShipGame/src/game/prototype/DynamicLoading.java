package game.prototype;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.objects.Block;
import game.prototype.objects.CollisionBlock;
import game.prototype.objects.ExplosiveMine;
import game.prototype.objects.Ghost;

public class DynamicLoading {
    private Handler handler;
    public static Point2D.Float deleteObj = new Point2D.Float();

    public DynamicLoading(Handler handler) {
        this.handler = handler;
    }

    public void update(Point2D.Float cameraPos, ArrayList<float[]> coords, 
                       ArrayList<int[]> colorRGB, float size) 
    {
        for (int i = 0; i < coords.size(); i++) {
            // Load on screen objects
            if (coords.get(i)[0] > cameraPos.x && 
                coords.get(i)[0] < cameraPos.x + Game.WIDTH && 
                coords.get(i)[1] > cameraPos.y && 
                coords.get(i)[1] < cameraPos.y + Game.HEIGHT) 
            {
                // Color(RGB) and load state
                if (colorRGB.get(i)[0] == 255 && 
                    colorRGB.get(i)[1] == 255 && 
                    colorRGB.get(i)[2] == 255 && 
                    coords.get(i)[2] == 0)

                {
                    handler.addObject(new Block(coords.get(i)[0], coords.get(i)[1], size, 
                                                size, ObjectId.Block));
                }
                if (colorRGB.get(i)[0] == 255 && 
                    colorRGB.get(i)[1] == 0 && 
                    colorRGB.get(i)[2] == 0 && 
                    coords.get(i)[2] == 0) 
                {
                    handler.addObject(new CollisionBlock(coords.get(i)[0], coords.get(i)[1], 
                                                        size, size, handler, 
                                                        ObjectId.CollisionBlock));
                }
                if (colorRGB.get(i)[0] == 0 && 
                    colorRGB.get(i)[1] == 255 && 
                    colorRGB.get(i)[2] == 0 && 
                    coords.get(i)[2] == 0 && coords.get(i)[3] == 0) 
                {
                    handler.addObject(new ExplosiveMine(coords.get(i)[0], coords.get(i)[1], 
                                                        size, size, handler, 
                                                        ObjectId.ExplosiveMine));
                }
                if (colorRGB.get(i)[0] == 255 && 
                    colorRGB.get(i)[1] == 255 && 
                    colorRGB.get(i)[2] == 0 && 
                    coords.get(i)[2] == 0 && coords.get(i)[3] == 0) 
                {
                    handler.addObject(new Ghost(coords.get(i)[0], coords.get(i)[1], 150, 150, 
                                                handler, ObjectId.Ghost));
                }
                
                coords.get(i)[2] = 1;
                if (deleteObj.x == coords.get(i)[0] && deleteObj.y == coords.get(i)[1]) {
                    coords.get(i)[3] = 1;
                }
  
            } else
                coords.get(i)[2] = 0;
            // Delete off screen objects
            if (coords.get(i)[2] != 0) {
                for (int j = 0; j < handler.object.size(); j++) {
                    GameObject tempObject = handler.object.get(j);

                    if (tempObject.getX() < cameraPos.x || 
                        tempObject.getX() > cameraPos.x + Game.WIDTH) 
                    {
                        handler.removeObject(tempObject);
                    }
                    if (tempObject.getY() < cameraPos.y || 
                        tempObject.getY() > cameraPos.y + Game.HEIGHT) 
                    {
                        handler.removeObject(tempObject);
                    }
                }
            }
        }
    }
}