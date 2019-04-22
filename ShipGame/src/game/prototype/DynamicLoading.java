package game.prototype;

import java.awt.geom.Point2D;

import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.framework.GameStates;
import game.prototype.objects.Block;
import game.prototype.objects.CollisionBlock;
import game.prototype.objects.ExplosiveMine;
import game.prototype.objects.Ghost;
import game.prototype.objects.Shield;

public class DynamicLoading {
    
    private Handler handler;
    private GameStates gs;

    public DynamicLoading(Handler h, GameStates gs) {
        this.handler = h;
        this.gs = gs;
    }

    public void update(Point2D.Float camPos, float s) 
    {
        
        for (int i = 0; i < gs.levelObjCoords().size(); i++) {
            // Load on screen objects
            if (gs.levelObjCoords().get(i)[0] > camPos.x && 
                gs.levelObjCoords().get(i)[0] < camPos.x + Game.WIDTH && 
                gs.levelObjCoords().get(i)[1] > camPos.y && 
                gs.levelObjCoords().get(i)[1] < camPos.y + Game.HEIGHT) 
            {
                // Color(RGB) and load state
                if (gs.levelRGB().get(i)[0] == 255 && 
                    gs.levelRGB().get(i)[1] == 255 && 
                    gs.levelRGB().get(i)[2] == 255 && 
                    gs.levelObjCoords().get(i)[2] == 0)

                {       
                    handler.addObject(new Block(gs.levelObjCoords().get(i)[0], 
                                                gs.levelObjCoords().get(i)[1], 
                                                s, s, ObjectId.Block));
                }
                if (gs.levelRGB().get(i)[0] == 255 && 
                    gs.levelRGB().get(i)[1] == 0 && 
                    gs.levelRGB().get(i)[2] == 0 && 
                    gs.levelObjCoords().get(i)[2] == 0) 
                {
                    handler.addObject(new CollisionBlock(gs.levelObjCoords().get(i)[0], 
                                                         gs.levelObjCoords().get(i)[1], 
                                                         s, s, handler, gs, ObjectId.CollisionBlock));
                }
                if (gs.levelRGB().get(i)[0] == 0 && 
                    gs.levelRGB().get(i)[1] == 255 && 
                    gs.levelRGB().get(i)[2] == 0 && 
                    gs.levelObjCoords().get(i)[2] == 0 && gs.levelObjCoords().get(i)[3] == 0) 
                {
                    handler.addObject(new ExplosiveMine(gs.levelObjCoords().get(i)[0], 
                                                        gs.levelObjCoords().get(i)[1], 
                                                        s, s, handler, gs, ObjectId.ExplosiveMine));
                }
                if (gs.levelRGB().get(i)[0] == 255 && 
                    gs.levelRGB().get(i)[1] == 255 && 
                    gs.levelRGB().get(i)[2] == 0 && 
                    gs.levelObjCoords().get(i)[2] == 0 && gs.levelObjCoords().get(i)[3] == 0) 
                {
                    handler.addObject(new Ghost(gs.levelObjCoords().get(i)[0], 
                                                gs.levelObjCoords().get(i)[1], 
                                                150, 150, handler, gs, ObjectId.Ghost));
                }
                if (gs.levelRGB().get(i)[0] == 0 && 
                    gs.levelRGB().get(i)[1] == 255 && 
                    gs.levelRGB().get(i)[2] == 255 && 
                    gs.levelObjCoords().get(i)[2] == 0 && gs.levelObjCoords().get(i)[3] == 0) 
                {
                    handler.addObject(new Shield(gs.levelObjCoords().get(i)[0], 
                                                 gs.levelObjCoords().get(i)[1], 
                                                 60, 60, handler, gs, ObjectId.Shield));
                }
                
                gs.levelObjCoords().get(i)[2] = 1;

                if (gs != null && gs.getObjState().x == gs.levelObjCoords().get(i)[0] 
                                && gs.getObjState().y == gs.levelObjCoords().get(i)[1]) 
                {
                    gs.levelObjCoords().get(i)[3] = 1;
                }
  
            } else
                gs.levelObjCoords().get(i)[2] = 0;
            // Delete off screen objects
            if (gs.levelObjCoords().get(i)[2] != 0) {
                for (int j = 0; j < handler.object.size(); j++) {
                    GameObject tempObject = handler.object.get(j);

                    if (tempObject.getX() < camPos.x || 
                        tempObject.getX() > camPos.x + Game.WIDTH) 
                    {
                        handler.removeObject(tempObject);
                    }
                    if (tempObject.getY() < camPos.y || 
                        tempObject.getY() > camPos.y + Game.HEIGHT) 
                    {
                        handler.removeObject(tempObject);
                    }
                }
            }
        }
    }
}