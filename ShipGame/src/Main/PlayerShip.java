package Main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.*;

import javax.swing.JPanel;

public class PlayerShip extends JPanel implements KeyListener, Runnable{
//TODO Improve the drawing of the ship
	private static final long serialVersionUID = 1L;
	Point2D shipCenterPoint = new Point2D.Double(100,100);
	Point2D.Double firstPoint = new Point2D.Double(shipCenterPoint.getX(),shipCenterPoint.getY()-20);
	Point2D.Double secondPoint = new Point2D.Double(shipCenterPoint.getX()-15,shipCenterPoint.getY()+20);
	Point2D.Double thirdPoint = new Point2D.Double(shipCenterPoint.getX()-15,shipCenterPoint.getY()+20);
	Point2D.Double fourthPoint = new Point2D.Double(shipCenterPoint.getX(),shipCenterPoint.getY()+10);
	Point2D.Double fithPoint = new Point2D.Double(shipCenterPoint.getX(),shipCenterPoint.getY()+10);
	Point2D.Double sixthPoint = new Point2D.Double(shipCenterPoint.getX()+15,shipCenterPoint.getY()+20);
	Point2D.Double seventhPoint = new Point2D.Double(shipCenterPoint.getX(),shipCenterPoint.getY()-20);
	Point2D.Double eighthPoint = new Point2D.Double(shipCenterPoint.getX()+15,shipCenterPoint.getY()+20);
	int keyCode;
	Thread thread = new Thread(this);
	double x = shipCenterPoint.getX(), y = shipCenterPoint.getY(), velx=0, vely=0;
	double x1 = shipCenterPoint.getX(), y1 = shipCenterPoint.getY();
	double x2 = shipCenterPoint.getX(), y2 = shipCenterPoint.getY();
	double x3 = shipCenterPoint.getX(), y3 = shipCenterPoint.getY();
	double x4 = shipCenterPoint.getX(), y4 = shipCenterPoint.getY();
	double x5 = shipCenterPoint.getX(), y5 = shipCenterPoint.getY();
	double x6 = shipCenterPoint.getX(), y6 = shipCenterPoint.getY();
	double x7 = shipCenterPoint.getX(), y7 = shipCenterPoint.getY();
	double shipSize = 40;
	
	public PlayerShip() {
		thread.start();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}
	
	public void paintComponent(Graphics g) 
	{
		//TODO draw a better ship
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(new Line2D.Double(x, y, x1, y1));
		g2.draw(new Line2D.Double(x2, y2, x3, y3));
		g2.draw(new Line2D.Double(x4, y4, x5, y5));
		g2.draw(new Line2D.Double(x6, y6, x7, y7));
		
		g2.setColor(Color.RED);
		g2.drawOval((int)shipCenterPoint.getX(), (int)shipCenterPoint.getY(), 1, 1);
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
		velx += 1.5;
		vely = 0;
	}
	public void left() {
		velx += -1.5;
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
			
			x4 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), velx, fithPoint).x;
			y4 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), vely, fithPoint).y;	
			x5 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), velx, sixthPoint).x;
			y5 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), vely, sixthPoint).y;
			
			x6 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), velx, seventhPoint).x;
			y6 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), vely, seventhPoint).y;	
			x7 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), velx, eighthPoint).x;
			y7 = rotatePoint(shipCenterPoint.getX(),shipCenterPoint.getY(), vely, eighthPoint).y;

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
