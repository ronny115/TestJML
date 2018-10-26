package game.prototype;

import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import game.prototype.framework.GameObject;
import game.prototype.framework.ObjectId;
import game.prototype.objects.ScreenBounds;

public class Handler extends KeyAdapter{

	public LinkedList<GameObject> object = new LinkedList <GameObject>();
	private GameObject tempObject;
	private int screenBoundsWidth;
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
			move("up");
			if(upKeyPressed == true && leftKeyPressed == true) {
				move("up");
				move("left");
			}
			else if(upKeyPressed == true && rightKeyPressed == true) {
				move("up");
				move("right");
			}
			else if(leftKeyReleased == true) {
				move("decelerateLeft");
			}
			else if(rightKeyReleased == true) {
				move("decelerateRight");
			}
		}			
		if (downKeyPressed == true) {
			move("down");
		}
		if (leftKeyPressed == true) {
			move("left");
		}
		if (rightKeyPressed == true) {
			move("right");
		}
		//Released
		if (upKeyReleased == true) {
			move("decelerateUp");		
		}
		if (downKeyReleased == true) {
			move("decelerateDown");		
		}
		if (leftKeyReleased == true && leftKeyPressed == false && rightKeyPressed == false) {	
			move("decelerateLeft");
		}
		if (rightKeyReleased == true && rightKeyPressed == false && leftKeyPressed == false) {	
			move("decelerateRight");
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
	
	public void createScreenBounds(int width) {
		screenBoundsWidth = width;
		addObject(new ScreenBounds(0,0,Game.WIDTH,Game.HEIGHT,width,ObjectId.ScreenBounds));
	}
	public int getScreenBoundsWidth() {
		return screenBoundsWidth;
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
	private void move(String move) {
		switch(move) 
		{
		case "up":
			vely += deltaSpeed;
			tempObject.setVelY(vely);
			if (vely >= topSpeed) {
				vely = (float) topSpeed;
				tempObject.setVelY(vely);
			}
			break;
		case "down":
			vely += -deltaSpeed*2;
			tempObject.setVelY(vely);
			if (vely <= -topSpeed/10) {
				vely = (float) -topSpeed/10;
				tempObject.setVelY(vely);
			}
			break;
		case "left":
			velx -= deltaSpeed;
			tempObject.setVelX(velx);
			if (velx <= -topSpeed/100) {
				velx = (float) (-topSpeed/100);
				tempObject.setVelX(velx);
			}	
			break;
		case "right":
			velx += deltaSpeed;
			tempObject.setVelX(velx);
			if (velx >= topSpeed/100) {
				velx = (float) (topSpeed/100);
				tempObject.setVelX(velx);
			}
			break;
		case "decelerateUp":	
			vely -= deltaSpeed/5;
			tempObject.setVelY(vely);
			if (vely < 0) {
				vely = 0;
				tempObject.setVelY(vely);
				upKeyReleased = false; 
			}
			break;
		case "decelerateDown":
			vely += deltaSpeed/5;
			tempObject.setVelY(vely);
			if (vely > 0) {
				vely = 0;
				tempObject.setVelY(vely);
				downKeyReleased = false;
			}
			break;
		case "decelerateLeft":
			velx += deltaSpeed/50;
			tempObject.setVelX(velx);
			if(velx > 0) {
				velx = 0;
				tempObject.setVelX(velx);
				leftKeyReleased = false;
			}
			break;
		case "decelerateRight":
			velx -= deltaSpeed/50;
			tempObject.setVelX(velx);
			if(velx < 0) {
				velx = 0;
				tempObject.setVelX(velx);
				rightKeyReleased = false;
			}
			break;
		}
	}
	
	public Point2D.Float vectorize(Point2D.Float pointA, Point2D.Float pointB) {
		Point2D.Float vector = new Point2D.Float();
		vector.x = (pointB.x - pointA.x);
		vector.y = (pointB.y - pointA.y);
		return vector;	
	}
	
	public float dotProduct(Point2D.Float vec1, Point2D.Float vec2) {
		float dp = ((vec1.x * vec2.x) + (vec1.y * vec2.y));
		return dp;
	}
}
