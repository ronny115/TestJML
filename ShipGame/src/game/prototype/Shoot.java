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
        if (spaceKey && gs.getHealth() > 0) {
            handler.addObject(new Projectile(handler.player.get(0).points()[1].x, 
                                             handler.player.get(0).points()[1].y, 
                                             5, 20, 20, "player",
                                             Helper.getAngle(handler.player.get(0).points()), 
                                             ObjectId.Projectile));
            spaceKey = !spaceKey;
        }
    }
    
    public void setSpaceKey(boolean value) {
        if (value)
            spaceKey = value;
        else
            spaceKey = value;   
    }
}
