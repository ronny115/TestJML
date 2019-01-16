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
    private boolean isRunning = false;
    private int fpsCounter;

    private Handler handler;
    private Camera camera;
    private KeyInput keyinput;
    private HUD hud;
    private Menu menu;
    private ResourceLoader loader;
    private DynamicLoading dynamicLoading;
    private static TextureManager tex;

    private ArrayList<int[]> colorRGB = new ArrayList<int[]>();
    private ArrayList<float[]> coords = new ArrayList<float[]>();
    private float size;
    private Point2D.Float cameraPos = new Point2D.Float();
    private Point2D.Float initPlayerPos = new Point2D.Float();
    
    public static boolean Paused, Reseted, GameOver;

    private void init() {
        this.setFocusable(true);
        this.setFocusTraversalKeysEnabled(false);

        loader = new ResourceLoader();
        handler = new Handler();
        hud = new HUD();
        menu = new Menu();
        tex = new TextureManager();
        dynamicLoading = new DynamicLoading(handler);
        keyinput = new KeyInput(handler);
        // Max speed, speed increments
        keyinput.setSpeed(4.5f, 0.15f);
        this.addKeyListener(keyinput);

        hud.setFont(loader.loadFont("/PressStart2P.ttf"));
        menu.setFont(loader.loadFont("/PressStart2P.ttf"));
        loadData(loader.loadImage("/lvl_full.png"), 50);

        camera = new Camera(initPlayerPos.x - WIDTH / 2, initPlayerPos.y - HEIGHT / 2);

        handler.addObject(new Shield(initPlayerPos.x-40, initPlayerPos.y-40, 60, 60,
                                    handler, ObjectId.Shield));
        handler.addPlayer(new PlayerShip(initPlayerPos.x, initPlayerPos.y, 35, 45, 
                                         handler, PlayerId.PlayerShip));
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
        int ticks = 0, frames = 0;

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
                System.out.println("(ticks: " + ticks + ") FPS: " + frames);
                fpsCounter = 0;
                ticks = 0;
                frames = 0;
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
        g2.setColor(new Color(238, 238, 238));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, 
                            RenderingHints.VALUE_STROKE_PURE);
        // Draw Here 
        g2.translate(-camera.getX(), -camera.getY());
        handler.render(g2);
        g2.translate(camera.getX(), camera.getY());
        hud.render(g2);
        if(Paused) {
            menu.pauseRender(g2);
        }
        if(GameOver) {
            menu.gameOverRender(g2);
        } 
        // Show
        g2.dispose();
        bStrategy.show();
    }

    private void update() {
        if (!Paused) {
            handler.update();
            keyinput.updateInput();
            hud.update();
            if(handler.player.size() > 0)
                camera.update(handler.player.get(0));      
            cameraPos.x = camera.getX();
            cameraPos.y = camera.getY();
            dynamicLoading.update(cameraPos, coords, colorRGB, size);
            if(GameOver)
                if(Reseted) 
                    reset();
        } else {
            menu.pauseUpdate();   
            if(Reseted) {
                reset();
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
    
    private void reset() {
        //Clear object lists
        handler.object.clear();
        handler.player.clear();
        coords.clear();
        colorRGB.clear();
        //Reset HUD
        HUD.HEALTH = 100;
        HUD.POINTS = 0;
        //Reload data and player object
        loadData(loader.loadImage("/lvl_full.png"), 50);
        handler.addPlayer(new PlayerShip(initPlayerPos.x, initPlayerPos.y, 35, 45, 
                                         handler, PlayerId.PlayerShip));
        //Reset camera
        camera.setX(initPlayerPos.x - WIDTH / 2);
        camera.setY(initPlayerPos.y - HEIGHT / 2);
        //Reset done and un-pause
        Reseted = !Reseted;
        Paused = !Paused;
        if(GameOver) {
            GameOver = !GameOver;
            Paused = !Paused;
        }
    }
    
    public static TextureManager getTexInstance() {
        return tex;
    }

    public static void main(String args[]) {
        new Window(WIDTH, HEIGHT, "Game Prototype", new Game());
    }
}
