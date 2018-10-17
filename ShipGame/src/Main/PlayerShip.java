package Main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.*;

import javax.swing.JPanel;

public class PlayerShip extends JPanel implements KeyListener, Runnable{

	private static final long serialVersionUID = 1L;
	//Ship position and size.
	Point2D.Double shipCenterPoint = new Point2D.Double(200,100);
	int shipSize[] = {80,60};
	double speed = 1.5;
	//Ship points respect the ship center point.
	Point2D.Double firstPoint = new Point2D.Double(shipCenterPoint.x,                shipCenterPoint.y-shipSize[0]/2);
	Point2D.Double secondPoint = new Point2D.Double(shipCenterPoint.x-shipSize[1]/2, shipCenterPoint.y+shipSize[0]/2);
	Point2D.Double thirdPoint = new Point2D.Double(shipCenterPoint.x,                shipCenterPoint.y+shipSize[1]/3);
	Point2D.Double fourthPoint = new Point2D.Double(shipCenterPoint.x+shipSize[1]/2, shipCenterPoint.y+shipSize[0]/2);
	
	int keyCode;
	Thread thread = new Thread(this);
	
	double velx=0, vely=0;
	double x, y, x1, y1, x2, y2, x3, y3;

	
	public PlayerShip() {
		thread.start();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}
	
	public void paintComponent(Graphics g) 
	{
		double xpoints[] = {x,x1,x2,x3};
		double ypoints[] = {y,y1,y2,y3};
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
		shipCenterPoint = movePoint(speed,shipCenterPoint);
		firstPoint = movePoint(speed,firstPoint);
		secondPoint = movePoint(speed,secondPoint);
		thirdPoint = movePoint(speed,thirdPoint);
		fourthPoint = movePoint(speed,fourthPoint);
		//velx = 0;
	}
	public void down() {
		shipCenterPoint = movePoint(-speed,shipCenterPoint);
		firstPoint = movePoint(-speed,firstPoint);
		secondPoint = movePoint(-speed,secondPoint);
		thirdPoint = movePoint(-speed,thirdPoint);
		fourthPoint = movePoint(-speed,fourthPoint);
		//velx = 0;
	}
	public void right() {
		velx += 0.1;
		if (velx >= 0.3 ) {
			velx = 0.3;
		}
		//vely = 0;
	}
	public void left() {
		velx += -0.1;
		if (velx <= -0.3 ) {
			velx = -0.3;
		}
		//vely = 0;
	}
	public void rightReleased() {
		velx = 0;
		vely = 0;
	}
	public void leftReleased() {
		velx = 0;
		vely = 0;
	}
	
	private Point2D.Double movePoint(double speed, Point2D.Double point) {
		double xnew = point.x, ynew = point.y;
		//Use the first point of the ship to calculate the direction of movement
		xnew += speed * Math.sin(Math.atan2(firstPoint.x - shipCenterPoint.x, firstPoint.y - shipCenterPoint.y));
		ynew += speed * Math.cos(Math.atan2(firstPoint.x - shipCenterPoint.x, firstPoint.y - shipCenterPoint.y));
	return new Point2D.Double(xnew,ynew);	
	}
		
	private Point2D.Double rotatePoint(Point2D.Double center, double angle, Point2D.Double point) {
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		
		point.x -= center.x;
		point.y -= center.y;
		
		double xnew = point.x * c -point.y * s;
		double ynew = point.x * s +point.y * c;
		
		point.x = xnew + center.x;
		point.y = ynew + center.y;
		
		return new Point2D.Double(point.x,point.y);
	}

	public void run() {
		while(true)
		{
			try 
			{			
				x = rotatePoint(shipCenterPoint, velx, firstPoint).x;
				y = rotatePoint(shipCenterPoint, vely, firstPoint).y;
				
				x1 = rotatePoint(shipCenterPoint, velx, secondPoint).x;
				y1 = rotatePoint(shipCenterPoint, vely, secondPoint).y;
		
				x2 = rotatePoint(shipCenterPoint, velx, thirdPoint).x;
				y2 = rotatePoint(shipCenterPoint, vely, thirdPoint).y;
					
				x3 = rotatePoint(shipCenterPoint, velx, fourthPoint).x;
				y3 = rotatePoint(shipCenterPoint, vely, fourthPoint).y;
						
				repaint();
				Thread.sleep(50);
			
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
	}
}
