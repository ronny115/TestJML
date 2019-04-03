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
import game.prototype.framework.States;
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
    private Camera cam;
    private KeyInput kI;
    private Movement move;
    private Shoot shoot;
    private HUD hud;
    private States st;
    private Menu menu;
    private ResourceLoader loader;
    private DynamicLoading dLoader;
    private FileManagement save;
    private static TextureManager tex;
      
    private float size;
    private Point2D.Float camPos = new Point2D.Float();
    private Point2D.Float iPlayerPos = new Point2D.Float();
    private Point2D.Float lPlayerPos = new Point2D.Float();
    //Game states
    public static boolean Paused, Reseted, Continued, Exited, GameOver, GameOn;

    private void init() {
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        loader = new ResourceLoader();
        handler = new Handler();
        st = new States();
        tex = new TextureManager();
        
        save = new FileManagement(st);
        menu = new Menu(st, save);
        shoot = new Shoot(handler, st);
        move = new Movement(handler, menu);
        kI = new KeyInput(handler, menu, move, st, shoot);
        this.addKeyListener(kI);
        
        menu.setFont(loader.loadFont("/PressStart2P.ttf")); 
        mainMenuBackground();  
        System.gc(); 
    }
    
    private void gameInit() {  
        System.out.println("gameInit");
        
        move.setSpeed(4.5f, 0.15f);
        
        handler.object.clear();
        
        st.levelObjCoords().clear();
        st.levelRGB().clear();              
        st.setHealth(100);
        st.setShieldHealth(100);
        st.setPoints(0);
        st.setLife(3);
        st.setShieldState(false);
        
        hud = new HUD(st);
        hud.setFont(loader.loadFont("/PressStart2P.ttf"));        
        loadData(loader.loadImage("/lvl_full.png"), 50);    
        dLoader = new DynamicLoading(handler, st);
        cam = new Camera(iPlayerPos.x - WIDTH / 2, iPlayerPos.y - HEIGHT / 2);
                    
        handler.addObject(new Shield(iPlayerPos.x-40, iPlayerPos.y-40, 60, 60,
                                     handler, st, ObjectId.Shield));
        handler.addPlayer(new PlayerShip(iPlayerPos.x, iPlayerPos.y, 35, 45, 
                                         handler, st, PlayerId.PlayerShip)); 
        
        save.save(1);
        
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
            menu.startMenuRender(g2);
        } else {
            if(!gameInit) 
                gameInit();
            //Game
            g2.translate(-cam.getX(), -cam.getY());
            handler.render(g2);
            g2.translate(cam.getX(), cam.getY());
            hud.render(g2);     
            //In-game Menus
            if(Paused) {
                menu.pauseRender(g2);
            } else if(GameOver && st.getLife() > 0) {
                menu.continueRender(g2);
            } else if(GameOver && st.getLife() == 0) {
                menu.gameOverRender(g2);
            }
        }
        //SysInfo
        if (menu.getF1Key() && handler.player.size() > 0) {
            menu.statsDisplay(g2, 100, 100, fps, tickRate, handler.object.size(),
                              handler.player.get(0).getX(), handler.player.get(0).getY(),
                              handler.player.get(0).getVelY(), handler.player.get(0).getVelX());
        } else if (menu.getF1Key()) {
            menu.statsDisplay(g2, 100, 100, fps, tickRate, handler.object.size(), 0, 0,0, 0);
        }
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
            if (!Paused) {
                handler.update();
                move.update();
                shoot.shooting();
                st.update();
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
                st.levelRGB().add(rgb);

                if ((xx % 2) != 0) {
                    // Coords., load state, life state
                    float[] pixelCoords = { xx * scale.x, 
                                           (yy * scale.y * 2) + scale.y, 
                                           0, 0} ;

                    st.levelObjCoords().add(pixelCoords);
                } else {
                    // Coords., load state, life state
                    float[] pixelCoords = { xx * scale.x, 
                                           (yy * scale.y * 2), 
                                           0, 0 };

                    st.levelObjCoords().add(pixelCoords);
                }
                // Player location coords.
                if (red == 0 && green == 0 && blue == 255) {
                    iPlayerPos.x = (xx * scale.x);
                    iPlayerPos.y = (yy * scale.y * 2) + scale.y;
                }
            }
        }
        st.setLevel(st.levelRGB(), st.levelObjCoords());
    }
    
    private void clear() {
        System.out.println("clear");
        handler.object.clear();
        handler.player.clear();
        st.levelObjCoords().clear();
        st.levelRGB().clear();
        hud = null;
        cam = null;
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
        st.setHealth(100);
        st.setLife(st.getLife() - 1); 
        handler.addPlayer(new PlayerShip(lPlayerPos.x, lPlayerPos.y, 35, 45, 
                                         handler, st, PlayerId.PlayerShip));
        cam.setX(lPlayerPos.x - WIDTH / 2);
        cam.setY(lPlayerPos.y - HEIGHT / 2);
        Continued = !Continued;        
        GameOver = !GameOver;      
    }
    
    private void mainMenuBackground() {
        camPos.x = camPos.y = 0;
        loadData(loader.loadImage("/menubg.png"), 50);
        dLoader = new DynamicLoading(handler, st);
    }
    
    public static TextureManager getTexInstance() {
        return tex;
    }

    public static void main(String args[]) {
        new Window(WIDTH, HEIGHT, "Game Prototype", new Game());
    }
}
