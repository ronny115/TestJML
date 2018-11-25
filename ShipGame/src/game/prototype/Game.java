package game.prototype;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import game.prototype.framework.PlayerId;
import game.prototype.objects.PlayerShip;

public class Game extends Canvas implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Thread thread;
	public static int WIDTH = 1280, HEIGHT = WIDTH / 16 * 9;
	private boolean isRunning = false;
	private int fpsCounter;
	private BufferedImage level = null;
	//Objects
	private Handler handler;
	private Camera camera;
	private KeyInput keyinput;
	private HUD hud;
	private DynamicLoading dynamicLoading;
	
	private ArrayList<int[]> colorRGB = new ArrayList<int[]>();
	private ArrayList<float[]> coords = new ArrayList<float[]>();
	private float size;
	private Point2D.Float playerPos = new Point2D.Float();
	private Point2D.Float initPlayerPos = new Point2D.Float();
		
	private void init() {	
		camera = new Camera(0, 0);
		handler = new Handler();	
		keyinput = new KeyInput(handler);
		this.addKeyListener(keyinput);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		keyinput.setSpeed(3.5f, 0.1f);//Max speed, speed increments
		hud = new HUD();			
		BufferedImageLoader loader = new BufferedImageLoader();	
		level = loader.loadImage("/lvl.png"); //Load level
		loadData(level, 55);//Level, block size
		dynamicLoading = new DynamicLoading(handler, level.getWidth(), level.getHeight());
		handler.addPlayer(new PlayerShip(initPlayerPos.x, initPlayerPos.y, 30, 40, handler, PlayerId.PlayerShip));//Position(x,y), size(w,h)
	}
		
	public synchronized void start() {
		if(isRunning) {
			return;
		}
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
		
		while(isRunning) {
			long currentTime = System.nanoTime();
			double frameTime = currentTime - initialTime;
			deltaT += frameTime / optimal_time_frame;
			initialTime = currentTime;
			if(deltaT>=1) {
				update();
				ticks++;
				deltaT--;
			}
			render();
			fpsCounter += frameTime;
			frames++;
			if (fpsCounter >= 1000000000) {
				System.out.println("(ticks: "+ticks+") FPS: "+frames );
				fpsCounter = 0;
				ticks = 0;
				frames = 0;
			}
		}
	}
	
	private void render() {
		BufferStrategy bStrategy = this.getBufferStrategy();
		if(bStrategy == null)
		{
			this.createBufferStrategy(2);
			return;
		}
		Graphics2D g2 = (Graphics2D) bStrategy.getDrawGraphics();
	
		//Clear the screen
		g2.setColor(new Color(238, 238, 238));
		g2.fillRect(0,0,getWidth(),getHeight());
		
		//Quality options
		//g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		//Draw Here
		g2.translate(camera.getX(), camera.getY());
		handler.render(g2);
		g2.translate(-camera.getX(), -camera.getY());
				
		hud.render(g2);
		
		//Show
		g2.dispose();
		bStrategy.show();
	}
	
	private void update() {
		handler.update();
		keyinput.updateInput();
		hud.update();
		for (int i = 0 ; i < handler.player.size(); i++) {
			if(handler.player.get(i).getId() == PlayerId.PlayerShip) {
				camera.update(handler.player.get(i));
				playerPos.x = handler.player.get(i).getX();
				playerPos.y = handler.player.get(i).getY();
			}
		}
		dynamicLoading.update(playerPos, coords, colorRGB, size);		
	}

	private void loadData(BufferedImage image, float size) {
		Point2D.Float scale = new Point2D.Float();
		int w = image.getWidth();
		int h = image.getHeight();
		this.size = size;
		
		scale.x = (size*2 - (size/2));
		scale.y = (float) (Math.sqrt(3) * size)/2;

		for (int yy = 0; yy < h; yy++) {
			for (int xx = 0; xx < w; xx++) {
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;

				int[] rgb = {red, blue, green};
				colorRGB.add(rgb);
				if ((xx % 2) != 0) { //odd
					//calculate the coords
					float[] pixelCoords = {xx*scale.x, (yy*scale.y*2)+scale.y};
					coords.add(pixelCoords);
				} else { // even	
					//calculate the coords
					float[] pixelCoords = {xx*scale.x, (yy*scale.y*2)};
					coords.add(pixelCoords);
				}
				//Player location coords				
				if (red == 0 && green == 0 && blue == 255) {
					initPlayerPos.x = (xx*scale.x); initPlayerPos.y = (yy*scale.y*2)+scale.y;
				}
			}
		}

	}
	
	public static void main(String args[]) {
		new Window(Game.WIDTH, Game.HEIGHT, "Game Prototype", new Game());
	}
}
