package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    
    private Font font;
    List<String> menuItem;
    public static List<Boolean> PauseItem = new ArrayList<Boolean>();
    public static List<Boolean> GameOverItem = new ArrayList<Boolean>();
    public Menu() {
        menuItem = List.of("PAUSE", "GAME OVER", "Save Game", "Restart", "Exit");
        for (int i = 2; i < menuItem.size(); i++)
            PauseItem.add(false);
        PauseItem.set(0, true);
        for (int i = 3; i < menuItem.size(); i++)
            GameOverItem.add(false);
        GameOverItem.set(0, true);
    }
    
    public void pauseUpdate() {
        
    }
    
    public void pauseRender(Graphics2D g2) {
        //Menu title
        g2.setColor(Color.BLACK);
        centerText(g2, menuItem.get(0), 0.25f, font.deriveFont(100f), false);
        //Items
        centerText(g2, menuItem.get(2), 0.55f, font.deriveFont(15f), PauseItem.get(0));
        centerText(g2, menuItem.get(3), 0.65f, font.deriveFont(15f), PauseItem.get(1));
        centerText(g2, menuItem.get(4), 0.75f, font.deriveFont(15f), PauseItem.get(2));
    }
    
    public void gameOverRender(Graphics2D g2) {
        //Menu title
        g2.setColor(Color.BLACK);
        centerText(g2, menuItem.get(1), 0.45f, font.deriveFont(100f), false);
        //Items
        centerText(g2, menuItem.get(3), 0.65f, font.deriveFont(15f), GameOverItem.get(0));
        centerText(g2, menuItem.get(4), 0.75f, font.deriveFont(15f), GameOverItem.get(1));
    }
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    private void centerText(Graphics2D g2, String label, float height, Font fsize, boolean active) {
        if (active) g2.setColor(Color.GRAY);
        else g2.setColor(Color.BLACK);
        g2.setFont(fsize);
        int x = (Game.WIDTH - g2.getFontMetrics(fsize).stringWidth(label))/2;
        int y = (int) (Game.HEIGHT * height);
        g2.drawString(label, x, y);
        g2.setColor(Color.BLACK);
    }
}
