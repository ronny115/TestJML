package Main;

import javax.swing.*;

public class MainClass {
	public static void createWindow() {
		PlayerShip ps = new PlayerShip();
		JFrame frame = new JFrame("Window");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1420, 800);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.add(ps);
		frame.addKeyListener(ps);
	}
	
	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable() {
		public void run() {
			createWindow();
		}
	});
	}
}


