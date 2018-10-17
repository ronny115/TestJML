package Main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.*;

import javax.swing.JPanel;

public class PlayerShip extends JPanel implements KeyListener, Runnable{

	private static final long serialVersionUID = 1L;
	
	Point2D shipCenterPoint = new Point2D.Double(200,100);
	int shipSize[] = {80,60};
	//Ship points respect the ship center point.
	Point2D.Double firstPoint = new Point2D.Double(shipCenterPoint.getX(),shipCenterPoint.getY()-shipSize[0]/2);
	Point2D.Double secondPoint = new Point2D.Double(shipCenterPoint.getX()-shipSize[1]/2,shipCenterPoint.getY()+shipSize[0]/2);
	Point2D.Double thirdPoint = new Point2D.Double(shipCenterPoint.getX(),shipCenterPoint.getY()+shipSize[1]/3);
	Point2D.Double fourthPoint = new Point2D.Double(shipCenterPoint.getX()+shipSize[1]/2,shipCenterPoint.getY()+shipSize[0]/2);
	
	int keyCode;
	
	Thread thread = new Thread(this);
	
	double velx=0, vely=0;
	double x, y , x1, y1, x2, y2, x3, y3;

	
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
		g2.drawOval((int)shipCenterPoint.getX(), (int)shipCenterPoint.getY(), 1, 1);
	}
	// add in map constructor
	public Dimension getPreferredSize() {
        return new Dimension(1420, 800);
    }
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		if (code == KeyEvent.VK_UP) {
			up();
		}
		if (code == KeyEvent.VK_DOWN) {
			down();
		}
		if (code == KeyEvent.VK_LEFT) {
			left();
		}
		if (code == KeyEvent.VK_RIGHT) {
			right();
		}
	}

	public void keyReleased(KeyEvent e) {}
	public void keyTyped(KeyEvent e) {}
	//TODO improve the movement logic
	public void up() {
		vely += -1.5;
		velx = 0;
	}
	public void down() {
		vely += 1.5;
		velx = 0;
	}
	public void right() {
		velx += 0.1;
		vely = 0;
	}
	public void left() {
		velx += -0.1;
		vely = 0;
	}
	
	private Point2D.Double rotatePoint(double centerX, double centerY, double angle, Point2D.Double point) {
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		
		point.x -= centerX;
		point.y -= centerY;
		
		double xnew = point.x * c -point.y * s;
		double ynew = point.x * s +point.y * c;
		
		point.x = xnew + centerX;
		point.y = ynew + centerY;
		
		return new Point2D.Double(point.x,point.y);
	}

	public void run() {
		while(true)
		{
			try 
			{			
				x = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), velx, firstPoint).x;
				y = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), vely, firstPoint).y;
				
				x1 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), velx, secondPoint).x;
				y1 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), vely, secondPoint).y;
		
				x2 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), velx, thirdPoint).x;
				y2 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), vely, thirdPoint).y;
					
				x3 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), velx, fourthPoint).x;
				y3 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), vely, fourthPoint).y;
						
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
