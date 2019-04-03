package game.prototype;

import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.States;
import game.prototype.objects.Projectile;

public class Shoot {
    
    private Handler handler;
    private States states;
    private boolean spaceKey;
        
    public Shoot(Handler h, States s) {
        this.handler = h;
        this.states = s;
    }
    
    public void shooting() {
        if (spaceKey && states.getHealth() > 0) {
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
