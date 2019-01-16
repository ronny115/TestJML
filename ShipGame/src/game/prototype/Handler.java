package game.prototype;

import java.awt.Graphics2D;
import java.util.LinkedList;

import game.prototype.framework.GameObject;
import game.prototype.framework.PlayerObject;

public class Handler {
    public LinkedList<PlayerObject> player = new LinkedList<PlayerObject>();
    private PlayerObject tempPlayer;
    public LinkedList<GameObject> object = new LinkedList<GameObject>();
    private GameObject tempObject;

    public void update() {
        if (player.size() > 0) {
            tempPlayer = player.get(0);
            tempPlayer.updatePlayer(player);
        }
        for (int i = 0; i < object.size(); i++) {
            tempObject = object.get(i);
            tempObject.update(object);
        } 
    }

    public void render(Graphics2D g2) {
        if (player.size() > 0) {
            tempPlayer = player.get(0);
            tempPlayer.renderPlayer(g2);
        }
        for (int i = 0; i < object.size(); i++) {
            tempObject = object.get(i);
            if (tempObject.getRenderPriority() == 1) {
                tempObject.render(g2);
            }
        }
        
        for (int i = 0; i < object.size(); i++) {
            tempObject = object.get(i);
            if (tempObject.getRenderPriority() == 2) {
                tempObject.render(g2);
            }
        }
        
        for (int i = 0; i < object.size(); i++) {
            tempObject = object.get(i);
            if (tempObject.getRenderPriority() == 3) {
                tempObject.render(g2);
            }           
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
}
