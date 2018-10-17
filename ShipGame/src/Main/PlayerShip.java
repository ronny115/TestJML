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
	double topSpeed = 20, deltaSpeed = 0.01;
	double linearVel = 0, rotationVel = 0;
	//Ship points respect the ship center point.
	Point2D.Double firstPoint = new Point2D.Double(shipCenterPoint.x,                shipCenterPoint.y-shipSize[0]/2);
	Point2D.Double secondPoint = new Point2D.Double(shipCenterPoint.x-shipSize[1]/2, shipCenterPoint.y+shipSize[0]/2);
	Point2D.Double thirdPoint = new Point2D.Double(shipCenterPoint.x,                shipCenterPoint.y+shipSize[1]/3);
	Point2D.Double fourthPoint = new Point2D.Double(shipCenterPoint.x+shipSize[1]/2, shipCenterPoint.y+shipSize[0]/2);
	
	int keyCode;
	Thread thread = new Thread(this);
	
	double velx=0, vely=0;
	
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
	//TODO improve the movement logic
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
		if (rotationVel >= topSpeed/1000) {
			rotationVel = topSpeed/1000;
		}
	}
	public void left() {
		rotationVel += -deltaSpeed;
		if (rotationVel <= -topSpeed/1000) {
			rotationVel = -topSpeed/1000;
		}
	}
	public void rightReleased() {
		while(rotationVel != 0)
		{
			rotationVel -= 0.01;
			if (rotationVel <= 0 ) {
				rotationVel = 0;
				break;
			}
		}
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

	public void run() {
		while(true)
		{
			try 
			{	
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
						
				repaint();
				Thread.sleep(5);
			
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
