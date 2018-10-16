package Main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener 
{
	char key;

	//KeyListener
	public void keyTyped(KeyEvent e) 
	{
	}
	public void keyPressed(KeyEvent e) 
	{
	    //System.out.println("Key pressed code=" + e.getKeyCode() + ", char=" + e.getKeyChar());
	    key = e.getKeyChar();
	}
	public void keyReleased(KeyEvent e) 
	{
	}
	
	public void keyP()
	{
	if (key == 'w') 
		{
		System.out.println("hola");
		}
	}

}