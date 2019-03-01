package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class Menu {
    
    private Font font;
    private boolean sysInfo, saveGameInput;
    private List<String> menuItem;
    private List<Boolean> MainMenuItem = new ArrayList<Boolean>();
    private List<Boolean> PauseItem = new ArrayList<Boolean>();
    private List<Boolean> GameOverItem = new ArrayList<Boolean>();
    private List<Boolean> SaveWindowItem = new ArrayList<Boolean>();
    
    public Menu() {
        menuItem = List.of("PAUSE", "CONTINUE?", "GAME OVER", "New Game", "Load Game", 
                           "Save Game", "Restart", "Continue", "Exit", "Quit Game", "Yes", 
                           "No", "Do you want to save?");
        
        for (int i = 0; i < 3; i++)
            MainMenuItem.add(false);
        MainMenuItem.set(0, true);
        
        for (int i = 0; i < 4; i++)
            PauseItem.add(false);
        PauseItem.set(0, true);
        
        for (int i = 0; i < 2; i++)
            GameOverItem.add(false);
        GameOverItem.set(0, true);
        
        for (int i = 0; i < 2; i++)
            SaveWindowItem.add(false);
        SaveWindowItem.set(0, true);
    }
       
    public void startMenuRender(Graphics2D g2) {
        //TODO Add Options
        g2.drawImage(Game.getTexInstance().title, 
                Game.getTexInstance().title.getWidth()+100,
                Game.getTexInstance().title.getHeight()+50, 
                400, 175, null);
        g2.setColor(Color.BLACK);
        verticalText(g2, menuItem.get(3), 0.55f, font.deriveFont(15f), MainMenuItem.get(0));
        verticalText(g2, menuItem.get(4), 0.65f, font.deriveFont(15f), MainMenuItem.get(1));
        verticalText(g2, menuItem.get(9), 0.75f, font.deriveFont(15f), MainMenuItem.get(2));
    }
    
    public void startMenuUpdate() {
        
    }
    
    public void pauseRender(Graphics2D g2) {
        //Menu title
        g2.setColor(Color.BLACK);
        verticalText(g2, menuItem.get(0), 0.25f, font.deriveFont(100f), false);
        //Items
        if(saveGameInput) {
            g2.drawImage(Game.getTexInstance().cristal, 
                         (Game.WIDTH-Game.getTexInstance().cristal.getWidth()-200)/2,
                         (Game.HEIGHT-Game.getTexInstance().cristal.getHeight()+200)/2, 
                         600,200, null);
            verticalText(g2, menuItem.get(12), 0.45f, font.deriveFont(25f), false);
            horizontalText(g2, menuItem.get(10), 0.55f, -50, font.deriveFont(15f), SaveWindowItem.get(0));
            horizontalText(g2, menuItem.get(11), 0.55f, 50, font.deriveFont(15f), SaveWindowItem.get(1));
        } else {
            verticalText(g2, menuItem.get(4), 0.55f, font.deriveFont(15f), PauseItem.get(0));
            verticalText(g2, menuItem.get(5), 0.65f, font.deriveFont(15f), PauseItem.get(1));
            verticalText(g2, menuItem.get(6), 0.75f, font.deriveFont(15f), PauseItem.get(2));
            verticalText(g2, menuItem.get(8), 0.85f, font.deriveFont(15f), PauseItem.get(3));
        }
        
    }
    
    public void pauseUpdate() {
        
    }
    
    
    public void gameOverRender(Graphics2D g2) {
        //Menu title
        g2.setColor(Color.BLACK);
        verticalText(g2, menuItem.get(2), 0.45f, font.deriveFont(100f), false);
        //Items
        verticalText(g2, menuItem.get(6), 0.65f, font.deriveFont(15f), GameOverItem.get(0));
        verticalText(g2, menuItem.get(8), 0.75f, font.deriveFont(15f), GameOverItem.get(1));
    }
    
    public void continueRender(Graphics2D g2) {
        //Menu title
        g2.setColor(Color.BLACK);
        verticalText(g2, menuItem.get(1), 0.45f, font.deriveFont(100f), false);
        //Items
        verticalText(g2, menuItem.get(7), 0.65f, font.deriveFont(15f), GameOverItem.get(0));
        verticalText(g2, menuItem.get(8), 0.75f, font.deriveFont(15f), GameOverItem.get(1));
    }
     
    private void verticalText(Graphics2D g2, String label, float height, Font fsize, boolean active) {
        int x = (Game.WIDTH - g2.getFontMetrics(fsize).stringWidth(label)) / 2;
        int y = (int) (Game.HEIGHT * height);
        
        if (active)
            g2.setColor(Color.GRAY);
        else
            g2.setColor(Color.BLACK);
        g2.setFont(fsize);        
        g2.drawString(label, x, y);
        g2.setColor(Color.BLACK);
    }
    
    private void horizontalText(Graphics2D g2, String label, float height, int dist, Font fsize, boolean active) {
        int x = (Game.WIDTH - g2.getFontMetrics(fsize).stringWidth(label)) / 2 + dist;
        int y = (int) (Game.HEIGHT * height);
        
        if (active)
            g2.setColor(Color.GRAY);
        else
            g2.setColor(Color.BLACK);
        g2.setFont(fsize);
        g2.drawString(label, x, y);
        g2.setColor(Color.BLACK);
    }
    
    public void sysInfoRender(Graphics2D g2, int x, int y, int fps, int tickrate, int objSize) {
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString("Fps: " + String.valueOf(fps), x, y);
        g2.drawString("Ticks: " + String.valueOf(tickrate), x, y + 10);
        g2.drawString("Nº obj: " + String.valueOf(objSize), x, y + 20);     
    }
    
    public void setFont(Font font) {
        this.font = font;
    }
    
    public void setSaveGameState(boolean saveGameInput) {
        this.saveGameInput = saveGameInput;
    }
    
    public boolean getSaveGameState() {
        return saveGameInput;
    }
    
    public void setSysInfo(boolean sysInfo) {
        this.sysInfo = sysInfo;
    }
    
    public boolean getSysInfo() {
        return sysInfo;
    }
    
    public List<Boolean> mainMenuItem() {
        return MainMenuItem;
    }
    
    public List<Boolean> pauseItem() {
        return PauseItem;
    }
    
    public List<Boolean> saveWindowItem(){
        return SaveWindowItem;
    }
    
    public List<Boolean> gameOverItem() {
        return GameOverItem;
    }
}
