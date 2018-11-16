package game.prototype;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import game.prototype.framework.ObjectId;
import game.prototype.objects.PlayerShip;
import game.prototype.objects.Block;
import game.prototype.objects.CollisionBlock;

public class Game extends Canvas implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Thread thread;
	Color backgroundColor = new Color(238, 238, 238);
	public static int WIDTH, HEIGHT;
	private BufferedImage level = null;
	private int initPosX, initPosY;
	//Object
	Handler handler;
	private void init() {	
		WIDTH = getWidth();
		HEIGHT = getHeight();

		handler = new Handler();
		this.addKeyListener(handler);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
		
		BufferedImageLoader loader = new BufferedImageLoader();
		level = loader.loadImage("/lvl.png"); //load level
		LoadLevel(level);
		//Arguments: Screen bounds width
		handler.createScreenBounds(5);
		//Arguments: Position, size
		handler.addObject(new PlayerShip(initPosX, initPosY, 30, 40, handler, ObjectId.PlayerShip));
		//Arguments: Max speed, speed increments
		handler.setSpeed(3.5f, 0.1f);	
	}
	
	public boolean isRunning = false;
	int fpsCounter;
	
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
		 g2.setColor(backgroundColor);
		 g2.fillRect(0,0,getWidth(),getHeight());
		 //Draw Here
		 handler.render(g2);
		 //Show
		 g2.dispose();
		 bStrategy.show();
	}
	
	private void update() {
		handler.update();
		handler.updateInput();
	}
	
	private void LoadLevel(BufferedImage image) {
		int w = image.getWidth();
		int h = image.getHeight();
		
		for (int xx = 0; xx < h; xx++) {
			for (int yy = 0; yy < w; yy++) {
				int pixel = image.getRGB(xx, yy);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				//Arguments: Position, size
				if (red == 255 && green == 255 && blue == 255) handler.addObject(new Block(xx*82, yy*47, 55, 55, ObjectId.Block));
				if (red == 255 && green ==   0 && blue ==   0) handler.addObject(new CollisionBlock(xx*82, yy*47, 55, 55, handler, ObjectId.CollisionBlock));
				if (red == 0 && green == 0 && blue == 255) {initPosX = xx*75; initPosY = yy*43;}
			}
		}
	}
	
	public static void main(String args[]) {
		new Window(1420, 800, "Game Prototype", new Game());	
	}
}
