package Main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.*;

import javax.swing.JPanel;

public class PlayerShip extends JPanel implements KeyListener, Runnable{

	private static final long serialVersionUID = 1L;
	//Ship position, speed and size.
	Point2D.Double shipCenterPoint = new Point2D.Double(200,100);
	int shipSize[] = {80,60};
	double topSpeed = 5, deltaSpeed = 0.1;
	double linearVel = 0, rotationVel = 0;
	//Ship points respect the ship center point.
	Point2D.Double firstPoint = new Point2D.Double(shipCenterPoint.x,                shipCenterPoint.y-shipSize[0]/2);
	Point2D.Double secondPoint = new Point2D.Double(shipCenterPoint.x-shipSize[1]/2, shipCenterPoint.y+shipSize[0]/2);
	Point2D.Double thirdPoint = new Point2D.Double(shipCenterPoint.x,                shipCenterPoint.y+shipSize[1]/3);
	Point2D.Double fourthPoint = new Point2D.Double(shipCenterPoint.x+shipSize[1]/2, shipCenterPoint.y+shipSize[0]/2);
	
	int keyCode;
	boolean keyR;
	Thread thread = new Thread(this);
	
	public PlayerShip() {
		thread.start();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}
	
	public void paintComponent(Graphics g) 
	{
		double xpoints[] = {firstPoint.x,secondPoint.x,thirdPoint.x,fourthPoint.x};
		double ypoints[] = {firstPoint.y,secondPoint.y,thirdPoint.y,fourthPoint.y};
		Path2D polygon = new Path2D.Double();
		polygon.moveTo(xpoints[0], ypoints[0]);
		for(int i = 1; i < xpoints.length; ++i) {
			polygon.lineTo(xpoints[i], ypoints[i]);
		}
		polygon.closePath();
		//TODO draw a better ship
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		//Enable AA
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(polygon);
		//g2.fill(polygon);
		g2.setColor(Color.RED);
		g2.drawOval((int)shipCenterPoint.x, (int)shipCenterPoint.y, 1, 1);
	}
	// add in map constructor
	public Dimension getPreferredSize() {
        return new Dimension(1420, 800);
    }
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			up();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			down();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			left();
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			right();
		}
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			leftReleased();
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			rightReleased();
		}
	}
	public void keyTyped(KeyEvent e) {}
	
	public void up() {
		linearVel += deltaSpeed;
		if (linearVel >= topSpeed) {
			linearVel = topSpeed;
		}

	}
	public void down() {
		linearVel += -deltaSpeed;
		if (linearVel <= -topSpeed) {
			linearVel = -topSpeed;
		}
	}
	public void right() {
		rotationVel += deltaSpeed;
		if (rotationVel >= topSpeed/100) {
			rotationVel = topSpeed/100;
		}
	}
	public void left() {
		rotationVel += -deltaSpeed;
		if (rotationVel <= -topSpeed/100) {
			rotationVel = -topSpeed/100;
		}
	}	
	public void rightReleased() {
		rotationVel = 0;
	}
	public void leftReleased() {
		rotationVel = 0;
	}
	
	private Point2D.Double movePoint(double speed, Point2D.Double point) {
		double xnew = point.x, ynew = point.y;
		//Use the first point of the ship to calculate the direction of movement
		xnew += speed * Math.sin(Math.atan2(firstPoint.x - shipCenterPoint.x, firstPoint.y - shipCenterPoint.y));
		ynew += speed * Math.cos(Math.atan2(firstPoint.x - shipCenterPoint.x, firstPoint.y - shipCenterPoint.y));
	return new Point2D.Double(xnew,ynew);	
	}
		
	private Point2D.Double rotatePoint(Point2D.Double center, double angle, Point2D.Double point) {

		point.x -= center.x;
		point.y -= center.y;
		
		double xnew = point.x * Math.cos(angle) -point.y * Math.sin(angle);
		double ynew = point.x * Math.sin(angle) +point.y * Math.cos(angle);
		
		point.x = xnew + center.x;
		point.y = ynew + center.y;
		
		return new Point2D.Double(point.x,point.y);
	}
	
	private boolean isRunning = true;
	int fpsCounter;
	
	public void run() {
		
		double deltaT = 0;
		long initialTime = System.nanoTime();
		final int target_fps = 60;
		final double optimal_time_frame = 1000000000 / target_fps;
	
		int frames = 0;
		while(isRunning)
		{
			long currentTime = System.nanoTime();
			double frameTime = currentTime - initialTime;
			deltaT += frameTime / optimal_time_frame;
			initialTime = currentTime;
			
			if(deltaT>=1) {
				update();
				paintImmediately(0, 0,1420, 900);
				frames++;
				deltaT--;
			}

			fpsCounter += frameTime;
			if (fpsCounter >= 1000000000) {
				System.out.println("(FPS: "+frames+")");
				fpsCounter = 0;
			    frames = 0;
				}
		}
	}
	
	public void update() {
		//Move forward and backwards
		shipCenterPoint = movePoint(linearVel,shipCenterPoint);
		firstPoint = movePoint(linearVel,firstPoint);
		secondPoint = movePoint(linearVel,secondPoint);
		thirdPoint = movePoint(linearVel,thirdPoint);
		fourthPoint = movePoint(linearVel,fourthPoint);
		//Rotate left and right
		firstPoint = rotatePoint(shipCenterPoint, rotationVel, firstPoint);
		secondPoint = rotatePoint(shipCenterPoint, rotationVel, secondPoint);
		thirdPoint = rotatePoint(shipCenterPoint, rotationVel, thirdPoint);
		fourthPoint = rotatePoint(shipCenterPoint, rotationVel, fourthPoint);
	}
}
