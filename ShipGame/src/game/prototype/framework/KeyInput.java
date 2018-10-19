package game.prototype.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.prototype.Handler;

public class KeyInput extends KeyAdapter {
	
	Handler handler;
	float topSpeed = 0, deltaSpeed = 0, vely = 0, velx =0;
	
	public KeyInput(Handler handler, float topSpeed, float deltaSpeed) {
		this.handler = handler;
		this.topSpeed = topSpeed;
		this.deltaSpeed = deltaSpeed;
	 }
	
	public void keyPressed(KeyEvent e) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
	
			if(tempObject.getId() == ObjectId.PlayerShip)
			{
				if(e.getKeyCode() == KeyEvent.VK_UP) {
					vely += deltaSpeed;
					tempObject.setVelY(vely);
					if (vely >= topSpeed) {
						vely = (float) topSpeed;
						tempObject.setVelY(vely);
					}
				}

				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					vely += -deltaSpeed;
					tempObject.setVelY(vely);
					if (vely <= -topSpeed) {
						vely = (float) -topSpeed;
						tempObject.setVelY(vely);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					velx += -deltaSpeed;
					tempObject.setVelX(velx);
					if (velx <= -topSpeed/100) {
						velx = (float) (-topSpeed/100);
						tempObject.setVelX(velx);
					}
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					velx += deltaSpeed;
					tempObject.setVelX(velx);
					if (velx >= topSpeed/100) {
						velx = (float) (topSpeed/100);
						tempObject.setVelX(velx);
					}
				}
			}
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ObjectId.PlayerShip)
			{
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					velx = 0;
					tempObject.setVelX(velx);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					velx = 0;
					tempObject.setVelX(velx);
				}
			}
		}
	}
}
