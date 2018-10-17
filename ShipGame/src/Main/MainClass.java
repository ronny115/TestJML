package Main;

import java.awt.Dimension;

import javax.swing.*;

public class MainClass {
	public static void createWindow() {
		PlayerShip ps = new PlayerShip();
		JFrame frame = new JFrame("Window");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(ps);
		frame.addKeyListener(ps);
		frame.pack();
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			createWindow();
		}
	});
	}
}


