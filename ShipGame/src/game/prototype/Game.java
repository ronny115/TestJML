package game.prototype;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.prototype.framework.KeyInput;
import game.prototype.framework.ObjectId;
import game.prototype.framework.PlayerId;
import game.prototype.framework.TextureManager;
import game.prototype.objects.PlayerShip;
import game.prototype.objects.Shield;

public class Game extends Canvas implements Runnable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Thread thread;
    public static int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;
    private boolean isRunning, gameInit;
    private int fps, tickRate;
    
    private Handler handler;
    private Camera camera;
    private KeyInput keyinput;
    private HUD hud;
    private Menu menu;
    private ResourceLoader loader;
    private DynamicLoading dynamicLoading;
    private SaveLoad save;
    private static TextureManager tex;

    private ArrayList<int[]> colorRGB = new ArrayList<int[]>();
    private ArrayList<float[]> coords = new ArrayList<float[]>();
    
    private ArrayList<float[]> color = new ArrayList<float[]>();
   
    private float size;
    private Point2D.Float cameraPos = new Point2D.Float();
    private Point2D.Float initPlayerPos = new Point2D.Float();
    private Point2D.Float lastPlayerPos = new Point2D.Float();
    //Game states
    public static boolean Paused, Reseted, Continued, Exited, GameOver, GameOn;
    //Menu states
    public static boolean SaveMenu;

    private void init() {
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        loader = new ResourceLoader();
        handler = new Handler();        
        menu = new Menu();
        save = new SaveLoad();
        tex = new TextureManager();        
        keyinput = new KeyInput(handler, menu);
        // Max speed, speed increments
        keyinput.setSpeed(4.5f, 0.15f);
        this.addKeyListener(keyinput);      
        menu.setFont(loader.loadFont("/PressStart2P.ttf")); 
        mainMenuBackground();  
        System.gc(); 
    }
    
    private void gameInit() {  
        System.out.println("gameInit");
        handler.object.clear();
        coords.clear();
        colorRGB.clear();
        
        hud = new HUD();
        hud.setFont(loader.loadFont("/PressStart2P.ttf"));
        hud.setHealth(100);
        hud.setShieldHealth(100);
        hud.setPoints(0);
        hud.setLife(3);
        hud.setShieldState(false);
        
        loadData(loader.loadImage("/lvl_full.png"), 50);
        dynamicLoading = new DynamicLoading(handler);
        camera = new Camera(initPlayerPos.x - WIDTH / 2, initPlayerPos.y - HEIGHT / 2);
                    
        handler.addObject(new Shield(initPlayerPos.x-40, initPlayerPos.y-40, 60, 60,
                                     handler, hud, ObjectId.Shield));
        handler.addPlayer(new PlayerShip(initPlayerPos.x, initPlayerPos.y, 35, 45, 
                                         handler, hud, PlayerId.PlayerShip));  
        gameInit = !gameInit;
        
//        float[] lol1 = {0.0f,1.0f,2.0f};
//        float[] lol2 = {0.1f,1.1f,2.1f};
//        float[] lol3 = {0.2f,1.2f,2.2f};
//        color.add(lol1);
//        color.add(lol2);
//        color.add(lol3);
//        
//        save.saveFile(color);
//        color.clear();
    }
    public synchronized void start() {
        if (isRunning)
            return;
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void run() {
        init();
        double deltaT = 0;
        long initialTime = System.nanoTime();
        final int target_ticks = 60;
        final double optimal_time_frame = 1000000000 / target_ticks;
        int fpsCounter = 0, ticks = 0, frames = 0;;

        while (isRunning) {
            long currentTime = System.nanoTime();
            double frameTime = currentTime - initialTime;
            deltaT += frameTime / optimal_time_frame;
            initialTime = currentTime;
            render();
            if (deltaT >= 1) {
                update();
                ticks++;
                deltaT--;
            }
            fpsCounter += frameTime;
            frames++;
            if (fpsCounter >= 1000000000) {
                fps = frames;
                tickRate = ticks;
                fpsCounter = ticks = frames = 0;
            }
        }
    }

    private void render() {
        BufferStrategy bStrategy = this.getBufferStrategy();
        if (bStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics2D g2 = (Graphics2D) bStrategy.getDrawGraphics();
        //Background
        g2.setColor(new Color(238, 238, 238));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, 
                            RenderingHints.VALUE_STROKE_PURE);
        //Main Menu
        if (!GameOn) {
            handler.render(g2);
            menu.startMenuRender(g2);
        } else {
            if(!gameInit) 
                gameInit();
            //Game
            g2.translate(-camera.getX(), -camera.getY());
            handler.render(g2);
            g2.translate(camera.getX(), camera.getY());
            hud.render(g2);     
            //In-game Menus
            if(Paused) {
                menu.pauseRender(g2);
            } else if(GameOver && hud.getLife() > 0) {
                menu.continueRender(g2);
            } else if(GameOver && hud.getLife() == 0) {
                menu.gameOverRender(g2);
            }
        }
        //SysInfo
        if (menu.getSysInfo()) {
            menu.sysInfoRender(g2, 100, 100, fps, tickRate, handler.object.size());
        }
        //Show
        g2.dispose();
        bStrategy.show();
    }

    private void update() {
        if (!GameOn) {
            menu.startMenuUpdate();
            dynamicLoading.update(cameraPos, coords, colorRGB, size, hud);
            if (Exited)
                clear(); 
        } else {            
            if (!Paused) {
                handler.update();
                keyinput.updateInput(hud);
                hud.update();
                if (handler.player.size() > 0) {
                    lastPlayerPos.x = handler.player.get(0).getX();
                    lastPlayerPos.y = handler.player.get(0).getY();
                    camera.update(handler.player.get(0));
                }
                cameraPos.x = camera.getX();
                cameraPos.y = camera.getY();
                dynamicLoading.update(cameraPos, coords, colorRGB, size, hud);
                //GameOver reset
                if (Reseted) {
                    clear();
                    gameInit();
                }
                //Continue
                if (Continued)
                    cont();
            } else {
                menu.pauseUpdate();
                //Pause reset
                if (Reseted) {
                    clear();
                    gameInit();
                }
            }
        }
    }

    private void loadData(BufferedImage image, float size) {
        Point2D.Float scale = new Point2D.Float();
        int w = image.getWidth();
        int h = image.getHeight();
        this.size = size;

        scale.x = (size * 2 - (size / 2));
        scale.y = (float) (Math.sqrt(3) * size) / 2;

        for (int yy = 0; yy < h; yy++) {
            for (int xx = 0; xx < w; xx++) {
                int pixel = image.getRGB(xx, yy);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = (pixel) & 0xff;
                int[] rgb = { red, green, blue };
                colorRGB.add(rgb);

                if ((xx % 2) != 0) {
                    // Coords., load state, life state
                    float[] pixelCoords = { xx * scale.x, 
                                           (yy * scale.y * 2) + scale.y, 
                                           0, 0} ;

                    coords.add(pixelCoords);
                } else {
                    // Coords., load state, life state
                    float[] pixelCoords = { xx * scale.x, 
                                           (yy * scale.y * 2), 
                                           0, 0 };

                    coords.add(pixelCoords);
                }
                // Player location coords.
                if (red == 0 && green == 0 && blue == 255) {
                    initPlayerPos.x = (xx * scale.x);
                    initPlayerPos.y = (yy * scale.y * 2) + scale.y;
                }
            }
        }
    }
    
    private void clear() {
        System.out.println("clear");
        handler.object.clear();
        handler.player.clear();
        coords.clear();
        colorRGB.clear();
        hud = null;
        camera = null;
        mainMenuBackground();
        gameInit = !gameInit;
        if(Exited) Exited =! Exited;
        if(Paused) Paused = !Paused;
        if(GameOver) GameOver = !GameOver;
        if(Reseted) Reseted = !Reseted;
        System.gc();      
    }
        
    private void cont() {
        System.out.println("continue");
        handler.player.clear();
        hud.setHealth(100);
        hud.setLife(-1); 
        handler.addPlayer(new PlayerShip(lastPlayerPos.x, lastPlayerPos.y, 35, 45, 
                                         handler, hud, PlayerId.PlayerShip));
        camera.setX(lastPlayerPos.x - WIDTH / 2);
        camera.setY(lastPlayerPos.y - HEIGHT / 2);
        Continued = !Continued;        
        GameOver = !GameOver;      
    }
    
    private void mainMenuBackground() {
        cameraPos.x = cameraPos.y = 0;
        loadData(loader.loadImage("/menubg.png"), 50);
        dynamicLoading = new DynamicLoading(handler);
    }
    
    public static TextureManager getTexInstance() {
        return tex;
    }

    public static void main(String args[]) {
        new Window(WIDTH, HEIGHT, "Game Prototype", new Game());
    }
}
