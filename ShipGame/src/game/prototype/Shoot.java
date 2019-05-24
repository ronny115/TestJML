package game.prototype;

import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.GameStates;
import game.prototype.objects.Projectile;

public class Shoot {
    
    private Handler handler;
    private GameStates gs;
    private boolean spaceKey;
        
    public Shoot(Handler h, GameStates gs) {
        this.handler = h;
        this.gs = gs;
    }
    
    public void shooting() {
        if (gs.getShootMode() == 0) {
            if (spaceKey && gs.getHealth() > 0) {
                handler.addObject(new Projectile(handler.player.get(0).points()[1].x, 
                                                 handler.player.get(0).points()[1].y, 
                                                 5, 20, 20, "player", gs,
                                                 (float)(Helper.angle(handler.player.get(0).points()[0],
                                                                      handler.player.get(0).points()[1]) 
                                                 + Math.toRadians(90)), 
                                                 ObjectId.Projectile));
                spaceKey = !spaceKey;
            }
        } else if (gs.getShootMode() == 1) {
            if (spaceKey && gs.getHealth() > 0) {
                handler.addObject(new Projectile(handler.player.get(0).points()[2].x, 
                                                 handler.player.get(0).points()[2].y, 
                                                 5, 20, 20, "player", gs, 
                                                 (float)(Helper.angle(handler.player.get(0).points()[0],
                                                                      handler.player.get(0).points()[1]) 
                                                 + Math.toRadians(90)),
                                                 ObjectId.Projectile));

                handler.addObject(new Projectile(handler.player.get(0).points()[4].x, 
                                                 handler.player.get(0).points()[4].y, 
                                                 5, 20, 20, "player", gs, 
                                                 (float)(Helper.angle(handler.player.get(0).points()[0],
                                                                      handler.player.get(0).points()[1]) 
                                                 + Math.toRadians(90)),
                                                 ObjectId.Projectile));
                spaceKey = !spaceKey;
            }
        } else if (gs.getShootMode() == 2) {
            if (spaceKey && gs.getHealth() > 0) {
                handler.addObject(new Projectile(handler.player.get(0).points()[1].x, 
                                                 handler.player.get(0).points()[1].y, 
                                                 5, 20, 20, "player", gs,
                                                 (float)(Helper.angle(handler.player.get(0).points()[0],
                                                                      handler.player.get(0).points()[1]) 
                                                 + Math.toRadians(90)), 
                                                 ObjectId.Projectile));
                                                 
                handler.addObject(new Projectile(handler.player.get(0).points()[2].x, 
                                                 handler.player.get(0).points()[2].y, 
                                                 5, 20, 20, "player", gs, 
                                                 (float)(Helper.angle(handler.player.get(0).points()[0],
                                                                      handler.player.get(0).points()[1]) 
                                                 + Math.toRadians(90)),
                                                 ObjectId.Projectile));

                handler.addObject(new Projectile(handler.player.get(0).points()[4].x, 
                                                 handler.player.get(0).points()[4].y, 
                                                 5, 20, 20, "player", gs, 
                                                 (float)(Helper.angle(handler.player.get(0).points()[0],
                                                                      handler.player.get(0).points()[1]) 
                                                 + Math.toRadians(90)),
                                                 ObjectId.Projectile));
                spaceKey = !spaceKey;
            }
        }
    }
    
    public void setSpaceKey(boolean value) {
        if (value)
            spaceKey = value;
        else
            spaceKey = value;   
    }
}
