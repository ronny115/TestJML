package game.prototype;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;

public class Handler extends KeyAdapter{

	public LinkedList<GameObject> object = new LinkedList <GameObject>();
	private GameObject tempObject;
	private float topSpeed = 0, deltaSpeed = 0, vely = 0, velx =0;
	private boolean upKeyPressed, downKeyPressed, leftKeyPressed, rightKeyPressed;
	private boolean upKeyReleased, downKeyReleased, leftKeyReleased, rightKeyReleased;
	
	public void update()
	{
		for(int i =0; i < object.size(); i++) {
			tempObject = object.get(i);
			tempObject.update(object);
		}
	}
	
	public void render(Graphics2D g2)
	{
		for(int i =0; i < object.size(); i++) {
			tempObject = object.get(i);
			tempObject.render(g2);
		}
	}
	
	public void updateInput() {
		//Pressed
		if(upKeyPressed == true) {
			movement('u');
			if(upKeyPressed == true && leftKeyPressed == true) {
				movement('u');
				movement('l');
			}
			else if(upKeyPressed == true && rightKeyPressed == true) {
				movement('u');
				movement('r');
			}
			else if(leftKeyReleased == true) {
				desacelerate('l');
			}
			else if(rightKeyReleased == true) {
				desacelerate('r');
			}
		}			
		if (downKeyPressed == true) {
			movement('d');
		}
		if (leftKeyPressed == true) {
			movement('l');
		}
		if (rightKeyPressed == true) {
			movement('r');
		}
		//Released
		if (upKeyReleased == true) {
			desacelerate('u');		
		}
		if (downKeyReleased == true) {
			desacelerate('d');		
		}
		if (leftKeyReleased == true && leftKeyPressed == false && rightKeyPressed == false) {	
			desacelerate('l');
		}
		if (rightKeyReleased == true && rightKeyPressed == false && leftKeyPressed == false) {	
			desacelerate('r');
		}
		
	}
		
	public void setSpeed(float topSpeed, float deltaSpeed) {
		this.topSpeed = topSpeed;
		this.deltaSpeed = deltaSpeed;
	}
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
	
	public void keyPressed(KeyEvent e) {
		for(int i = 0; i < object.size(); i++) {
			tempObject = object.get(i);
			if(tempObject.getId() == ObjectId.PlayerShip) {	
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					upKeyPressed = true;
					upKeyReleased = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					downKeyPressed = true;
					downKeyReleased = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					leftKeyPressed = true;	
					leftKeyReleased = false;	
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightKeyPressed = true;
					rightKeyReleased = false;
					
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					System.exit(1);
				}
			}
		}			
	}

	public void keyReleased(KeyEvent e) {	
		for(int i = 0; i < object.size(); i++) {
			tempObject = object.get(i);
			if(tempObject.getId() == ObjectId.PlayerShip) {	
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					upKeyPressed = false;
					upKeyReleased = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					downKeyPressed = false;
					downKeyReleased = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					leftKeyPressed = false;
					leftKeyReleased = true;	
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightKeyPressed = false;
					rightKeyReleased = true;		
				}
			}
		}
	}
	private void movement(char move) {
		switch(move) 
		{
		case 'u':
			vely += deltaSpeed;
			tempObject.setVelY(vely);
			if (vely >= topSpeed) {
				vely = (float) topSpeed;
				tempObject.setVelY(vely);
			}
			break;
		case 'd':
			vely += -deltaSpeed*2;
			tempObject.setVelY(vely);
			if (vely <= -topSpeed/10) {
				vely = (float) -topSpeed/10;
				tempObject.setVelY(vely);
			}
			break;
		case 'l':
			velx -= deltaSpeed;
			tempObject.setVelX(velx);
			if (velx <= -topSpeed/100) {
				velx = (float) (-topSpeed/100);
				tempObject.setVelX(velx);
			}	
			break;
		case 'r':
			velx += deltaSpeed;
			tempObject.setVelX(velx);
			if (velx >= topSpeed/100) {
				velx = (float) (topSpeed/100);
				tempObject.setVelX(velx);
			}
			break;
		}
	}
	private void desacelerate(char direction) {	
		switch(direction)
		{
		case 'u':	
			vely -= deltaSpeed/5;
			tempObject.setVelY(vely);
			if (vely < 0) {
				vely = 0;
				tempObject.setVelY(vely);
				upKeyReleased = false; 
			}
			break;
		case 'd':
			vely += deltaSpeed/5;
			tempObject.setVelY(vely);
			if (vely > 0) {
				vely = 0;
				tempObject.setVelY(vely);
				downKeyReleased = false;
			}
			break;
		case 'l':
			velx += deltaSpeed/50;
			tempObject.setVelX(velx);
			if(velx > 0) {
				velx = 0;
				tempObject.setVelX(velx);
				leftKeyReleased = false;
			}
			break;
		case 'r':
			velx -= deltaSpeed/50;
			tempObject.setVelX(velx);
			if(velx < 0) {
				velx = 0;
				tempObject.setVelX(velx);
				rightKeyReleased = false;
			}
		}
	}
}
