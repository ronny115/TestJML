package game.prototype;

import java.awt.Graphics2D;
import java.util.LinkedList;

import game.prototype.framework.GameObject;

public class Handler {

	public LinkedList<GameObject> object = new LinkedList <GameObject>();
	private GameObject tempObject;
	
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
	
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
}
