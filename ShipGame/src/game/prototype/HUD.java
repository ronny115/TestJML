package game.prototype;

import java.awt.Color;
import java.awt.Graphics2D;

public class HUD {
	
	public static int HEALTH = 100;
	
	public void update() {
		
	}
	
	public void render(Graphics2D g2) {
		//TODO set a proper health bar
		g2.setColor(Color.GREEN);
		g2.fillRect(20, 20, 100, 50);
	}

}
