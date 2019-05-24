package game.prototype;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import game.prototype.framework.KeyInput;
import game.prototype.framework.ObjectId;
import game.prototype.framework.PlayerId;
import game.prototype.framework.GameStates;
import game.prototype.framework.TextureManager;
import game.prototype.objects.Chain;
import game.prototype.objects.Missile;
import game.prototype.objects.PlayerShip;

public class Game extends Canvas implements Runnable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public static int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;
    private boolean isRunning, gameInit;
    private int fps, tickRate;
    private float size;
    private Point2D.Float camPos = new Point2D.Float();
    private Point2D.Float iPlayerPos = new Point2D.Float();
    private Point2D.Float lPlayerPos = new Point2D.Float();

    private Thread thread; 
    private Handler handler;
    private Camera cam;
    private KeyInput kI;
    private Movement move;
    private Shoot shoot;
    private HUD hud;
    private GameStates gs;
    private Menu menu;
    private ResourceLoader loader;
    private DynamicLoading dLoader;
    private FileManagement fileMgmt;
    private static TextureManager tex;
      
    //Game states
    public static boolean Reseted, Continued, Exited, GameOver, GameOn;

    private void init() {
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        loader = new ResourceLoader();
        handler = new Handler();
        gs = new GameStates();
        tex = new TextureManager();        
        fileMgmt = new FileManagement(gs);
        menu = new Menu(gs, fileMgmt);
        shoot = new Shoot(handler, gs);
        move = new Movement(handler, menu, gs);
        kI = new KeyInput(menu, move, gs, shoot);
        this.addKeyListener(kI);
        
        mainMenuBackground();  
        System.gc(); 
    }
    
    private void gameInit() {  
        System.out.println("gameInit");        
        move.setSpeed(4.5f, 0.15f);       
        handler.object.clear();
        
        gs.levelObjCoords().clear();
        gs.levelRGB().clear();              
        gs.setHealth(100);
        gs.setShieldHealth(100);
        gs.setPoints(0);
        gs.setLife(3);
        gs.setShieldState(false);
        gs.setShootMode(0);
        
        hud = new HUD(gs);
        hud.setFont(loader.loadFont("/PressStart2P.ttf"));        
        loadData(loader.loadImage("/lvl_full.png"), 50);    
        dLoader = new DynamicLoading(handler, gs);
        cam = new Camera(iPlayerPos.x - WIDTH / 2, iPlayerPos.y - HEIGHT / 2);   
        
        //handler.addObject(new Chain(iPlayerPos.x -200, iPlayerPos.y -40, 30, 30, 5, 5, 5, 0.02f, handler, gs, ObjectId.Chain));
        
        handler.addPlayer(new PlayerShip(iPlayerPos.x, iPlayerPos.y, 35, 45, 
                                        (float)Math.toRadians(-90),
                                        handler, gs, PlayerId.PlayerShip));  
                                        
        handler.addObject(new Missile(iPlayerPos.x -200, iPlayerPos.y -40, 30, 30, handler, ObjectId.Missile));
 
        gameInit = !gameInit;
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

        } else {
            if (!gameInit) 
                gameInit();
            //Game
            g2.translate(-cam.getX(), -cam.getY());
            handler.render(g2);
            g2.translate(cam.getX(), cam.getY());
            hud.render(g2);     

        }
        menu.render(g2);  
        //Show
        g2.dispose();
        bStrategy.show();
    }

    private void update() {
        if (!GameOn) {
            dLoader.update(camPos, size);
            if (Exited)
                clear(); 
        } else {            
            if (!gs.getGamePaused()) {
                handler.update();
                move.update();
                shoot.shooting();
                gs.update();
                if (handler.player.size() > 0) {
                    lPlayerPos.x = handler.player.get(0).getX();
                    lPlayerPos.y = handler.player.get(0).getY();
                    cam.update(handler.player.get(0));
                }
                camPos.x = cam.getX();
                camPos.y = cam.getY();
                dLoader.update(camPos, size);
                //GameOver reset
                if (Reseted) {
                    clear();
                    gameInit();
                }
                //Continue
                if (Continued)
                    cont();
            } else {
                //Pause reset
                if (Reseted) {
                    clear();
                    gameInit();
                }
            }
        }
        menu.update();
        if (handler.player.size() > 0) 
            menu.statsDisplay(100, 100, fps, tickRate, handler.object.size(),
                              handler.player.get(0).getX(), handler.player.get(0).getY(),
                              handler.player.get(0).getVelY(), handler.player.get(0).getVelX());
        else
            menu.statsDisplay(100, 100, fps, tickRate, handler.object.size(), 0, 0,0, 0);
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
                gs.levelRGB().add(rgb);

                if ((xx % 2) != 0) {
                    // Coords., load state, life state
                    float[] pixelCoords = { xx * scale.x, 
                                           (yy * scale.y * 2) + scale.y, 
                                           0, 0} ;

                    gs.levelObjCoords().add(pixelCoords);
                } else {
                    // Coords., load state, life state
                    float[] pixelCoords = { xx * scale.x, 
                                           (yy * scale.y * 2), 
                                           0, 0 };

                    gs.levelObjCoords().add(pixelCoords);
                }
                // Player location coords.
                if (red == 0 && green == 0 && blue == 255) {
                    iPlayerPos.x = (xx * scale.x);
                    iPlayerPos.y = (yy * scale.y * 2) + scale.y;
                }
            }
        }
        gs.setLevel(gs.levelRGB(), gs.levelObjCoords());
    }
    
    private void clear() {
        System.out.println("clear");
        handler.object.clear();
        handler.player.clear();
        gs.levelObjCoords().clear();
        gs.levelRGB().clear();
 
        hud = null;
        cam = null;
        mainMenuBackground();
        gameInit = !gameInit;
        if(Exited) Exited =! Exited;
        if(gs.getGamePaused()) gs.setGamePaused(!gs.getGamePaused());
        if(GameOver) GameOver = !GameOver;
        if(Reseted) Reseted = !Reseted;
        System.gc();      
    }
        
    private void cont() {
        System.out.println("continue");
        handler.player.clear();
        gs.setHealth(100);
        gs.setLife(gs.getLife() - 1);
        handler.addPlayer(new PlayerShip(lPlayerPos.x, lPlayerPos.y, 35, 45, gs.getAngle(), 
                                         handler, gs, PlayerId.PlayerShip));
        cam.setX(lPlayerPos.x - WIDTH / 2);
        cam.setY(lPlayerPos.y - HEIGHT / 2);
        Continued = !Continued;        
        GameOver = !GameOver;      
    }
    
    private void mainMenuBackground() {
        camPos.x = camPos.y = 0;
        loadData(loader.loadImage("/menubg.png"), 50);
        dLoader = new DynamicLoading(handler, gs);
    }
    
    public static TextureManager getTexInstance() {
        return tex;
    }

    public static void main(String args[]) {
        new Window(WIDTH, HEIGHT, "Game Prototype", new Game());
    }
}
