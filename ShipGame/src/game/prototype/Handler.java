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
			up();
			if(upKeyPressed == true && leftKeyPressed == true) {
				up();
				left();
				if(leftKeyReleased == true) {
					rotationStop();
				}		
			}
			else if(upKeyPressed == true && rightKeyPressed == true) {
				up();
				right();
			}
			else if(leftKeyReleased == true) {
				rotationStop();
			}
			else if(rightKeyReleased == true) {
				rotationStop();
			}
		}			
		if (downKeyPressed == true) {
			down();
		}
		if (leftKeyPressed == true) {
			left();
		}
		if (rightKeyPressed == true) {
			right();
		}
		//Released
		if (upKeyReleased == true) {
			desacelerate(-1);		
		}
		if (downKeyReleased == true) {
			desacelerate(1);		
		}
		if (leftKeyReleased == true && leftKeyPressed == false && rightKeyPressed == false) {	
			rotationStop();
		}
		if (rightKeyReleased == true && rightKeyPressed == false && leftKeyPressed == false) {	
			rotationStop();
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
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					downKeyPressed = true;
					downKeyReleased = false;
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					leftKeyPressed = true;	
					leftKeyReleased = false;	
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightKeyPressed = true;
					rightKeyReleased = false;
					
				}
				else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
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
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					downKeyPressed = false;
					downKeyReleased = true;
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					leftKeyPressed = false;
					leftKeyReleased = true;	
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					rightKeyPressed = false;
					rightKeyReleased = true;		
				}
			}
		}
	}
	
	private void up() {
		vely += deltaSpeed;
		tempObject.setVelY(vely);
		if (vely >= topSpeed) {
			vely = (float) topSpeed;
			tempObject.setVelY(vely);
		}
	}
	private void down() {
		vely += -deltaSpeed*2;
		tempObject.setVelY(vely);
		if (vely <= -topSpeed/10) {
			vely = (float) -topSpeed/10;
			tempObject.setVelY(vely);
		}
	}
	private void left() {
		velx -= deltaSpeed;
		tempObject.setVelX(velx);
		if (velx <= -topSpeed/100) {
			velx = (float) (-topSpeed/100);
			tempObject.setVelX(velx);
		}	
	}
	private void right() {
		velx += deltaSpeed;
		tempObject.setVelX(velx);
		if (velx >= topSpeed/100) {
			velx = (float) (topSpeed/100);
			tempObject.setVelX(velx);
		}
	}
	private void rotationStop() {
		velx = 0;
		tempObject.setVelX(velx);
	}
	//TODO implement rotation desaceleration
	private void desacelerate(int direction) {
		if(direction < 0) {
			vely += deltaSpeed/5 * direction;
			tempObject.setVelY(vely);
			if (vely < 0) {
				vely = 0;
				tempObject.setVelY(vely);
				upKeyReleased = false; 
			}
		}
		if(direction > 0) {
			vely += deltaSpeed/5 * direction;
			tempObject.setVelY(vely);
			if (vely > 0) {
				vely = 0;
				tempObject.setVelY(vely);
				downKeyReleased = false;
			}
		}
	}
}
