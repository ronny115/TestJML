package game.prototype.framework;

import java.awt.image.BufferedImage;

import game.prototype.ResourceLoader;

public class TextureManager {
    private SpriteSheet pS, eS, explS;
    
    private BufferedImage player_sheet = null;
    private BufferedImage enemy_sheet = null;
    private BufferedImage explosion_sheet = null;
    
    public BufferedImage[] player = new BufferedImage[1];
    public BufferedImage[] explosiveMine = new BufferedImage[1];
    public BufferedImage[] ghostIdle = new BufferedImage[15];
    public BufferedImage[] bullet = new BufferedImage[3];
    public BufferedImage[] explosion = new BufferedImage[8];
    
    public TextureManager() {
		ResourceLoader loader = new ResourceLoader();
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
		player[0] = pS.grabImage(1, 1, 30, 40);//player ship
		bullet[0] = pS.grabImage(1, 4, 30, 40);//GreenBullet
		bullet[1] = pS.grabImage(2, 4, 30, 40);//RedBullet
		//Enemy
		for (int i = 0; i < 10; i++) {
		    ghostIdle[i] = eS.grabImage(i+1, 2, 50, 50);
		    }
		for (int i = 0; i < 5; i++) {
		    ghostIdle[10+i] = eS.grabImage(i+1, 3, 50, 50);
		    }
		explosiveMine[0] = eS.grabImage(1, 1, 50, 50); //mine
		for (int i = 0; i < 2; i++) { //explosion last frame invisible
		    for (int j = 0; j < 4; j++) {
		        explosion[count] = explS.grabImage(j+1, i+1, 60, 60);
		        count++;
		        }
		    }
		count = 0;
		}
    }
