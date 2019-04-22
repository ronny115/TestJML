package game.prototype.framework;

import java.awt.image.BufferedImage;

import game.prototype.ResourceLoader;

public class TextureManager {
    private SpriteSheet pS, eS, explS;
    
    
    private BufferedImage player_sheet = null;
    private BufferedImage enemy_sheet = null;
    private BufferedImage explosion_sheet = null;

    public BufferedImage title = null;
    public BufferedImage cristal = null;
    public BufferedImage window = null;
    public BufferedImage player = null;
    public BufferedImage shieldItem = null;
    public BufferedImage healthItem = null;
    
    public BufferedImage[] propulsion = new BufferedImage[10];
    public BufferedImage[] alert = new BufferedImage[20];
    public BufferedImage[] explosiveMine = new BufferedImage[1];
    public BufferedImage[] ghostIdle = new BufferedImage[15];
    public BufferedImage[] bullet = new BufferedImage[3];
    public BufferedImage[] explosion = new BufferedImage[8];
    public BufferedImage[] playerExplosion = new BufferedImage[8];
    public BufferedImage[] playerShield = new BufferedImage[7];
    public BufferedImage[] healthUp = new BufferedImage[10];

    public TextureManager() {
        ResourceLoader loader = new ResourceLoader();
        try {
            player_sheet = loader.loadImage("/player_sheet.png");
            enemy_sheet = loader.loadImage("/mine.png");
            explosion_sheet = loader.loadImage("/explosion_sheet.png");
            title = loader.loadImage("/title.png");
            cristal = loader.loadImage("/acrylic.png");
            window = loader.loadImage("/confirm625_265.png");
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
        
        player = pS.grabImage(1, 1, 30, 40);
        bullet[0] = pS.grabImage(1, 5, 30, 40);// GreenBullet
        bullet[1] = pS.grabImage(2, 5, 30, 40);// RedBullet
        shieldItem = pS.grabImage(1, 6, 30, 40);
        healthItem = pS.grabImage(2, 6, 30, 40);
        // Shield
        for (int i = 0; i < 7; i++) {
            playerShield[i] = pS.grabImage(i+2, 1, 30, 40); 
        } 
        for (int i = 0; i < 10; i++) {
            ghostIdle[i] = eS.grabImage(i+1, 2, 50, 50);
            propulsion[i] = pS.grabImage(i+1, 2, 30, 40);
            alert[i] = pS.grabImage(i+1, 3, 30, 40);
            alert[i+10] = pS.grabImage(i+1, 4, 30, 40);
            healthUp[i] = pS.grabImage(i+1, 7, 30, 40);
        }
        for (int i = 0; i < 5; i++) {
            ghostIdle[10+i] = eS.grabImage(i+1, 3, 50, 50);
        }
        explosiveMine[0] = eS.grabImage(1, 1, 50, 50); // mine
        
        for (int i = 0; i < 2; i++) { // explosion last frame invisible
            for (int j = 0; j < 4; j++) {
                explosion[count] = explS.grabImage(j+1, i+1, 60, 60);
                count++;
            }
        }
        count = 0;
        for (int i = 0; i < 2; i++) { // explosion 1 last frame invisible
            for (int j = 0; j < 4; j++) {
                playerExplosion[count] = explS.grabImage(j+1, i+3, 60, 60);
                count++;
            }
        }
        count = 0;
    }
}
