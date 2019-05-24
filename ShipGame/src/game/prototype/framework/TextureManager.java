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
    public BufferedImage laserItem = null;
    public BufferedImage chainBase = null;
    public BufferedImage mine = null;
    
    public BufferedImage[] propulsion = new BufferedImage[10];
    public BufferedImage[] playerAlert = new BufferedImage[20];   
    public BufferedImage[] ghostIdle = new BufferedImage[15];
    public BufferedImage[] bullet = new BufferedImage[4];
    public BufferedImage[] explosion = new BufferedImage[8];
    public BufferedImage[] playerHit = new BufferedImage[4];
    public BufferedImage[] playerExplosion = new BufferedImage[8];
    public BufferedImage[] playerShield = new BufferedImage[7];
    public BufferedImage[] healthUp = new BufferedImage[10];
    public BufferedImage[] shieldUp = new BufferedImage[10];
    public BufferedImage[] laserUp = new BufferedImage[10];
    public BufferedImage[] electricArc = new BufferedImage[10];

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
        shieldItem = pS.grabImage(1, 5, 30, 40);
        healthItem = pS.grabImage(2, 5, 30, 40);
        laserItem = pS.grabImage(3, 5, 30, 40);
        mine = eS.grabImage(1, 1, 50, 50); // mine
        chainBase = eS.grabImage(2, 1, 50, 50);
        
        for (int i = 0; i < 7; i++) 
            playerShield[i] = pS.grabImage(i+1, 9, 30, 40); 

        for (int i = 0; i < 10; i++) {
            ghostIdle[i] = eS.grabImage(i+1, 2, 50, 50);
            propulsion[i] = pS.grabImage(i+1, 2, 30, 40);
            playerAlert[i] = pS.grabImage(i+1, 3, 30, 40);
            playerAlert[i+10] = pS.grabImage(i+1, 4, 30, 40);
            healthUp[i] = pS.grabImage(i+1, 6, 30, 40);
            shieldUp[i] = pS.grabImage(i+1, 7, 30, 40);
            laserUp[i] = pS.grabImage(i+1, 8, 30, 40);
            electricArc[i] = eS.grabImage2(i+1, 4, 50, 50, 200);
        }
        for (int i = 0; i < 5; i++) 
            ghostIdle[10+i] = eS.grabImage(i+1, 3, 50, 50);
             
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
                bullet[j] = pS.grabImage(j+4, 5, 30, 40); // Bullets
                playerHit[j] = pS.grabImage(j+2, 1, 30, 40); // Player hit
                count++;
            }
        }
        count = 0;
    }
}
