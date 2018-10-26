package game.prototype;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import game.prototype.framework.ObjectId;
import game.prototype.objects.PlayerShip;
import game.prototype.objects.Tile;

public class Game extends Canvas implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Thread thread;
	Color backgroundColor = new Color(238,238,238);
	public static int WIDTH, HEIGHT;
	//Object
	Handler handler;
	private void init() {
		WIDTH = getWidth();
		HEIGHT = getHeight();
		handler = new Handler();
		//screen bounds width
		handler.createScreenBounds(10);
		//handler.addObject(new ScreenBounds(0,0,WIDTH,HEIGHT,ObjectId.ScreenBounds));
		//Arguments: Position, size
		handler.addObject(new Tile(700,700, 40, 40,handler, ObjectId.Tile));
		handler.addObject(new PlayerShip(850,250, 60,80, handler, ObjectId.PlayerShip));
		//Arguments: Max speed, speed increments
		handler.setSpeed(5f, 0.1f);
		this.addKeyListener(handler);
		this.setFocusable(true);
		this.setFocusTraversalKeysEnabled(false);
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
		
		while(isRunning)
		{
			long currentTime = System.nanoTime();
			double frameTime = currentTime - initialTime;
			deltaT += frameTime / optimal_time_frame;
			initialTime = currentTime;
				
			if(deltaT>=1) 
			{
				update();
				ticks++;
				deltaT--;
			}
			render();
			fpsCounter += frameTime;
			frames++;
			if (fpsCounter >= 1000000000) 
			{
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
	
	public static void main(String args[]) {
		new Window(1420, 800, "Game Prototype", new Game());	
	}
}
