package Main;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.*;

import javax.swing.JPanel;

public class PlayerShip extends JPanel implements KeyListener, Runnable{

	private static final long serialVersionUID = 1L;
	Point2D initialPoint = new Point2D.Double(100,100);
	int keyCode;
	Thread thread = new Thread(this);
	double x =initialPoint.getX(), y=initialPoint.getY(), velx=0, vely=0;
	
	public PlayerShip() {
		thread.start();
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
	}
	
	public void paintComponent(Graphics g) 
	{
		//TODO draw the ship
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		//g2.setStroke(new BasicStroke(3.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		g2.draw(new Line2D.Double((int)initialPoint.getX(), (int)initialPoint.getY()-20, (int)initialPoint.getX()-15,(int)initialPoint.getY()+20));
		
		g2.draw(new Line2D.Double((int)initialPoint.getX()-15,(int)initialPoint.getY()+20, (int)initialPoint.getX(),(int)initialPoint.getY()+10));
		g2.draw(new Line2D.Double((int)initialPoint.getX(),(int)initialPoint.getY()+10, (int)initialPoint.getX()+15,(int)initialPoint.getY()+20));
		
		g2.draw(new Line2D.Double((int)initialPoint.getX(), (int)initialPoint.getY()-20, (int)initialPoint.getX()+15,(int)initialPoint.getY()+20));
		
		g2.setColor(Color.RED);
		g2.drawOval((int)initialPoint.getX(), (int)initialPoint.getY(), 10, 10);
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

	public void run() {
		while(true)
		{
			try 
			{
			x = initialPoint.getX() + velx;
			y = initialPoint.getY() + vely;
			initialPoint.setLocation(x,y);	
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
