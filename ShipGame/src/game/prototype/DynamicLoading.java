package game.prototype;

import java.awt.geom.Point2D;

import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.framework.States;
import game.prototype.objects.Block;
import game.prototype.objects.CollisionBlock;
import game.prototype.objects.ExplosiveMine;
import game.prototype.objects.Ghost;

public class DynamicLoading {
    
    private Handler handler;
    private States states;

    public DynamicLoading(Handler h, States s) {
        this.handler = h;
        this.states = s;
    }

    public void update(Point2D.Float camPos, float s) 
    {
        
        for (int i = 0; i < states.levelObjCoords().size(); i++) {
            // Load on screen objects
            if (states.levelObjCoords().get(i)[0] > camPos.x && 
                states.levelObjCoords().get(i)[0] < camPos.x + Game.WIDTH && 
                states.levelObjCoords().get(i)[1] > camPos.y && 
                states.levelObjCoords().get(i)[1] < camPos.y + Game.HEIGHT) 
            {
                // Color(RGB) and load state
                if (states.levelRGB().get(i)[0] == 255 && 
                    states.levelRGB().get(i)[1] == 255 && 
                    states.levelRGB().get(i)[2] == 255 && 
                    states.levelObjCoords().get(i)[2] == 0)

                {       
                    handler.addObject(new Block(states.levelObjCoords().get(i)[0], 
                                                states.levelObjCoords().get(i)[1], 
                                                s, s, ObjectId.Block));
                }
                if (states.levelRGB().get(i)[0] == 255 && 
                    states.levelRGB().get(i)[1] == 0 && 
                    states.levelRGB().get(i)[2] == 0 && 
                    states.levelObjCoords().get(i)[2] == 0) 
                {
                    handler.addObject(new CollisionBlock(states.levelObjCoords().get(i)[0], 
                                                         states.levelObjCoords().get(i)[1], 
                                                        s, s, handler, ObjectId.CollisionBlock));
                }
                if (states.levelRGB().get(i)[0] == 0 && 
                    states.levelRGB().get(i)[1] == 255 && 
                    states.levelRGB().get(i)[2] == 0 && 
                    states.levelObjCoords().get(i)[2] == 0 && states.levelObjCoords().get(i)[3] == 0) 
                {
                    handler.addObject(new ExplosiveMine(states.levelObjCoords().get(i)[0], 
                                                        states.levelObjCoords().get(i)[1], 
                                                        s, s, handler, states, ObjectId.ExplosiveMine));
                }
                if (states.levelRGB().get(i)[0] == 255 && 
                    states.levelRGB().get(i)[1] == 255 && 
                    states.levelRGB().get(i)[2] == 0 && 
                    states.levelObjCoords().get(i)[2] == 0 && states.levelObjCoords().get(i)[3] == 0) 
                {
                    handler.addObject(new Ghost(states.levelObjCoords().get(i)[0], 
                                                states.levelObjCoords().get(i)[1], 
                                                150, 150, handler, states, ObjectId.Ghost));
                }
                
                states.levelObjCoords().get(i)[2] = 1;

                if (states != null && states.getObjState().x == states.levelObjCoords().get(i)[0] 
                                && states.getObjState().y == states.levelObjCoords().get(i)[1]) 
                {
                    states.levelObjCoords().get(i)[3] = 1;
                }
  
            } else
                states.levelObjCoords().get(i)[2] = 0;
            // Delete off screen objects
            if (states.levelObjCoords().get(i)[2] != 0) {
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