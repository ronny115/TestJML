package game.prototype.framework;

import java.awt.image.BufferedImage;

import game.prototype.BufferedImageLoader;

public class TextureManager {
	
	private SpriteSheet pS, eS, explS;
	private BufferedImage player_sheet = null;
	private BufferedImage enemy_sheet = null;
	private BufferedImage explosion_sheet = null;
	public BufferedImage[] player = new BufferedImage[1];
	public BufferedImage[] enemyType1 = new BufferedImage[1];
	public BufferedImage bullet;
	public BufferedImage[] explosion = new BufferedImage[74];
	
	public TextureManager() {
		BufferedImageLoader loader = new BufferedImageLoader();
		try {
			player_sheet = loader.loadImage("/player_sheet.png");
			enemy_sheet = loader.loadImage("/mine.png");
			explosion_sheet = loader.loadImage("/explosion_sheet.png");
		} catch (Exception e) {
			e.printStackTrace();
		}

		pS = new SpriteSheet(player_sheet);
		eS = new SpriteSheet(enemy_sheet);
		explS = new SpriteSheet(explosion_sheet);
		getTextures();
	}

	private void getTextures() {
		int count = 0;
		player[0] = pS.grabImage(1, 1, 30, 40); //player ship
		bullet = pS.grabImage(1, 4, 30, 40);//Bullet
		enemyType1[0] = eS.grabImage(1, 1, 50, 50); //player ship
		for(int i = 0; i < 8; i++) {
			for(int j = 0; j < 9; j++) {
				explosion[count] = explS.grabImage(j+1, i+1, 100, 100);
				count++;	
			}
		}
		count = 0;
		explosion[72] = explS.grabImage(1, 9, 100, 100);
		explosion[73] = explS.grabImage(2, 9, 100, 100);
	}
	
}
