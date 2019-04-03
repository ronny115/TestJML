package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.prototype.framework.States;

public class Menu {
    
    private Font font;
    private States states;
    private FileManagement fileM;
    private int mIndex, fIndex;
    private boolean upKey, downKey, leftKey, rightKey, enterKey, f1Key;
    private boolean saveGame, loadGame, loadConfirm, resetV;
    private List<String> menuItem;
    private List<Boolean> mainMItem = new ArrayList<Boolean>();
    private List<Boolean> pauseItem = new ArrayList<Boolean>();
    private List<Boolean> gameOItem = new ArrayList<Boolean>();
    private List<Boolean> saveItem = new ArrayList<Boolean>();
    private List<Boolean> loadItem = new ArrayList<Boolean>();
    private List<Boolean> slotsItem = new ArrayList<Boolean>();
    
    public Menu(States s, FileManagement fm) {
        this.states = s;
        this.fileM = fm;
        menuItem = List.of("PAUSE", "CONTINUE?", "GAME OVER", "New Game", "Load Game", 
                           "Save Game", "Restart", "Continue", "Exit", "Quit Game", "Save",                          
                           "Load", "Delete", "Cancel", "Do you want to save?", "Select File");
        
        for (int i = 0; i < 3; i++)
            mainMItem.add(false);
        mainMItem.set(0, true);
        
        for (int i = 0; i < 4; i++)
            pauseItem.add(false);
        pauseItem.set(0, true);
        
        for (int i = 0; i < 2; i++)
            gameOItem.add(false);
        gameOItem.set(0, true);
        
        for (int i = 0; i < 3; i++) {
            saveItem.add(false);
            loadItem.add(false);
        }
        saveItem.set(0, true);
        loadItem.set(0, true);
        
        for(int i = 0; i < states.saveSlots().size(); i++) {
            slotsItem.add(false);
        }
        slotsItem.set(0, true);
    }
    
    public void update() {
        int mainSize = mainMItem.size();
        int pauseSize = pauseItem.size();
        int gameOSize = gameOItem.size();
        int saveSize = saveItem.size();
        int loadSize = slotsItem.size();

        if (upKey)
            if (!saveGame && !loadConfirm) {
                menuMove(mainSize, pauseSize, saveSize, gameOSize, loadSize, "Up");
                upKey = false;
            }

        if (downKey)
            if (!saveGame && !loadConfirm) {
                menuMove(mainSize, pauseSize, saveSize, gameOSize, loadSize, "Down");
                downKey = false;
            }

        if (leftKey)
            if (saveGame || loadGame && loadConfirm) {
                menuMove(mainSize, pauseSize, saveSize, gameOSize, loadSize, "Left");
                leftKey = false;
            }

        if (rightKey)
            if (saveGame || loadGame && loadConfirm) {
                menuMove(mainSize, pauseSize, saveSize, gameOSize, loadSize, "Right");
                rightKey = false;
            }

        if (enterKey) {
            menuOption();
            enterKey = false;
        }
    }
    
    private void menuMove(int mMSize, int pSize, int sSize, int gOSize, int lSize, String dir) {
        // Main menu navigation
        if (!Game.GameOn) {
            menuIndex(mMSize, dir, "Main");
        }
        // Pause menu navigation
        if (Game.Paused && Game.GameOn && !saveGame && !loadGame) {
            menuIndex(pSize, dir, "Pause");
        }
        // Save options
        if (Game.Paused && Game.GameOn && saveGame) {
            menuIndex(sSize, dir, "Save");
        }
        // Load slots
        if (Game.Paused && Game.GameOn && loadGame && !loadConfirm) {
            menuIndex(lSize, dir, "Load");
        }
        // Load options
        if (Game.Paused && Game.GameOn && loadGame && loadConfirm) {
            menuIndex(sSize, dir, "LoadConfirm");
        }
        // GameOver menu navigation
        if (Game.GameOver && Game.GameOn) {
            menuIndex(gOSize, dir, "GameOver");
        }
    }
    
    private void menuIndex(int size, String dir, String menuType) {
        menuType(menuType);
        if (dir == "Up" || dir == "Left") {
            mIndex--;
            if (mIndex < 0)
                mIndex = size - 1;
            menuType(menuType);
        } else if (dir == "Down" || dir == "Right") {
            mIndex++;
            if (mIndex == size)
                mIndex = 0;
            menuType(menuType);
        }
    }
    
    private void menuType(String mType) {
        switch (mType) {
            case "Main":
                mainMItem.set(mIndex, !mainMItem.get(mIndex));
                break;
            case "Pause":
                pauseItem.set(mIndex, !pauseItem.get(mIndex));
                break;
            case "Save":
                saveItem.set(mIndex, !saveItem.get(mIndex));
                break;
            case "Load":
                slotsItem.set(mIndex, !slotsItem.get(mIndex));
                break;
            case "LoadConfirm":
                loadItem.set(mIndex, !loadItem.get(mIndex));
                break;
            case "GameOver":
                gameOItem.set(mIndex, !gameOItem.get(mIndex));
                break;
        }
    }
    
    private void menuOption() {
        //////////////
        // Main menu//
        //////////////

        // New Game
        if (!Game.GameOn && mainMItem.get(0)) {
            Game.GameOn = !Game.GameOn;
        }
        // Load Game

        // Quit game
        if (!Game.GameOn && mainMItem.get(2)) {
            System.exit(1);
        }
        ///////////////////////////
        // Continue and game over//
        ///////////////////////////
        if (Game.GameOver && gameOItem.get(0)) {
            if (states.getLife() > 0) {
                Game.Continued = !Game.Continued;
                resetV = true;
            }
            if (states.getLife() == 0) {
                Game.Reseted = !Game.Reseted;
                Game.GameOver = !Game.GameOver;
                resetV = true;
            }
        }
        // Exit
        if (Game.GameOver && gameOItem.get(1)) {
            Game.GameOn = !Game.GameOn;
            Game.Exited = !Game.Exited;
            resetPauseMenuItem(1);
        }
        ///////////////
        // Pause menu//
        ///////////////
        if(!Game.Paused) {
            loadConfirm = false;
            loadGame=false;
            saveGame = false;
            //saveConfirm = false;
        }
        // Reset
        if (Game.Paused && pauseItem.get(2)) {
            Game.Reseted = !Game.Reseted;
            resetPauseMenuItem(2);
            resetV = true;
        }
        // Exit
        else if (Game.Paused && pauseItem.get(3)) {
            Game.GameOn = !Game.GameOn;
            Game.Exited = !Game.Exited;
            resetPauseMenuItem(3);
        }
        // Save Game
        else if (Game.Paused && pauseItem.get(1)) {
            saveGame = true;
            resetPauseMenuItem(1);
        }
        //// Save Game sub-menu
        ///// Save
        else if (saveGame && saveItem.get(0)) {
            System.out.println("Saved");
            saveGame = false;
            mIndex = 0;
            resetV = true;
        }
        ////// Close
        else if (saveGame && saveItem.get(1)) {
            saveGame = false;
            saveItem.set(0, !saveItem.get(0));
            saveItem.set(1, !saveItem.get(1));
            mIndex = 0;
            resetV = true;
        }
        // Load Game
        else if (Game.Paused && pauseItem.get(0)) {
            pauseItem.set(0, !pauseItem.get(0));
            loadGame = true;
            fileM.loadFiles();
        }
        //// Load Game sub-menu   
        else if (loadGame && !loadConfirm && slotsItem.get(mIndex)) {
            fIndex = mIndex;  
            if (states.saveSlots().get(mIndex) == "Empty") {
                loadGame = false;
                pauseItem.set(0, !pauseItem.get(0));

                if (mIndex == 0) {
                    slotsItem.set(0, true);
                } else {
                    slotsItem.set(0, true);
                    slotsItem.set(mIndex, false);
                }
            } else {
                loadConfirm = true;
                if (mIndex == 0) {
                    slotsItem.set(0, true);
                } else {
                    slotsItem.set(0, true);
                    slotsItem.set(mIndex, false);
                }
            }
            mIndex = 0;
            resetV = true;
        }
        ////// Load
        else if (loadConfirm && loadItem.get(0)) {
            System.out.println("Loaded");
            System.out.println(states.saveSlots().get(fIndex));
            fileM.load(states.saveSlots().get(fIndex));
            pauseItem.set(0, !pauseItem.get(0));
            mIndex = 0;
            resetV = true;
            loadConfirm = false;
            loadGame = false;
            Game.Paused = !Game.Paused;
        }
        ////// Delete
        else if (loadConfirm && loadItem.get(1)) {
            System.out.println("deleted");
            fileM.deleteFile(fIndex);
            loadItem.set(0, !loadItem.get(0));
            loadItem.set(1, !loadItem.get(1));
            mIndex = 0;
            resetV = true;
            loadConfirm = false;
        }
        ////// Close
        else if (loadConfirm && loadItem.get(2)) {
            loadItem.set(0, !loadItem.get(0));
            loadItem.set(2, !loadItem.get(2));
            pauseItem.set(0, !pauseItem.get(0));
            mIndex = 0;
            resetV = true;           
            loadConfirm = false;
            loadGame = false;
        }
    }
    
    private void resetPauseMenuItem(int index) {
        pauseItem.set(0, !pauseItem.get(0));
        pauseItem.set(index, !pauseItem.get(index));
        mIndex = 0;
        resetV = true;
    }
           
    public void startMenuRender(Graphics2D g2) {
        //TODO Add Options
        g2.drawImage(Game.getTexInstance().title, 
                Game.getTexInstance().title.getWidth()+100,
                Game.getTexInstance().title.getHeight()+50, 
                400, 175, null);
        g2.setColor(Color.BLACK);
        vDisplay(g2, menuItem.get(3), 0.55f, font.deriveFont(15f), mainMItem.get(0));
        vDisplay(g2, menuItem.get(4), 0.65f, font.deriveFont(15f), mainMItem.get(1));
        vDisplay(g2, menuItem.get(9), 0.75f, font.deriveFont(15f), mainMItem.get(2));
    }
    
    
    public void pauseRender(Graphics2D g2) {
        //Pause menu title
        g2.setColor(Color.BLACK);
        vDisplay(g2, menuItem.get(0), 0.25f, font.deriveFont(100f), false);
        // Save menu
        if(saveGame) {
            g2.drawImage(Game.getTexInstance().cristal, 
                         (Game.WIDTH-Game.getTexInstance().cristal.getWidth()-200)/2,
                         (Game.HEIGHT-Game.getTexInstance().cristal.getHeight()+200)/2, 
                         600,200, null);
            vDisplay(g2, menuItem.get(14), 0.45f, font.deriveFont(25f), false);
            hDisplay(g2, menuItem.get(10), 0.55f, -100, font.deriveFont(15f), saveItem.get(0));
            hDisplay(g2, menuItem.get(12), 0.55f, 0, font.deriveFont(15f), saveItem.get(1));
            hDisplay(g2, menuItem.get(13), 0.55f, 110, font.deriveFont(15f), saveItem.get(2));
            
        } 
        // Load menu
        else if(loadGame) {
            vDisplay(g2, menuItem.get(15), 0.45f, font.deriveFont(25f), false);
            
            for(int i = 0; i < states.saveSlots().size(); i++) {
                vDisplay(g2, states.saveSlots().get(i), 0.55f +(i*0.05f), font.deriveFont(10f), slotsItem.get(i));
                //System.out.println("render: " + states.saveSlots().get(i));
            }
            if (loadConfirm) {
                g2.drawImage(Game.getTexInstance().cristal, 
                        (Game.WIDTH-Game.getTexInstance().cristal.getWidth()-200)/2,
                        (Game.HEIGHT-Game.getTexInstance().cristal.getHeight()+200)/2, 
                        600,200, null);              
                hDisplay(g2, menuItem.get(11), 0.55f, -100, font.deriveFont(15f), loadItem.get(0));
                hDisplay(g2, menuItem.get(12), 0.55f, 0, font.deriveFont(15f), loadItem.get(1));
                hDisplay(g2, menuItem.get(13), 0.55f, 110, font.deriveFont(15f), loadItem.get(2));              
            }                        
        } 
        // Pause menu items
        else {
            vDisplay(g2, menuItem.get(4), 0.55f, font.deriveFont(15f), pauseItem.get(0));
            vDisplay(g2, menuItem.get(5), 0.65f, font.deriveFont(15f), pauseItem.get(1));
            vDisplay(g2, menuItem.get(6), 0.75f, font.deriveFont(15f), pauseItem.get(2));
            vDisplay(g2, menuItem.get(8), 0.85f, font.deriveFont(15f), pauseItem.get(3));
        }        
    }
   
    public void gameOverRender(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        vDisplay(g2, menuItem.get(2), 0.45f, font.deriveFont(100f), false);
        // Items
        vDisplay(g2, menuItem.get(6), 0.65f, font.deriveFont(15f), gameOItem.get(0));
        vDisplay(g2, menuItem.get(8), 0.75f, font.deriveFont(15f), gameOItem.get(1));
    }
    
    public void continueRender(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        vDisplay(g2, menuItem.get(1), 0.45f, font.deriveFont(100f), false);
        // Items
        vDisplay(g2, menuItem.get(7), 0.65f, font.deriveFont(15f), gameOItem.get(0));
        vDisplay(g2, menuItem.get(8), 0.75f, font.deriveFont(15f), gameOItem.get(1));
    }
    
    public void statsDisplay(Graphics2D g2, int x, int y, int fps, int tickrate, 
                            int objSize, float posX, float posY, float velY, float velX ) 
    {
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString("Fps: " + String.valueOf(fps), x, y);
        g2.drawString("Ticks: " + String.valueOf(tickrate), x, y + 10);
        g2.drawString("Nº obj: " + String.valueOf(objSize), x, y + 20);     
        g2.drawString("X: " + String.valueOf(posX), x, y + 30);
        g2.drawString("Y: " + String.valueOf(posY), x, y + 40);
        g2.drawString("Velocity: " + String.valueOf(velY), x, y + 50);
        g2.drawString("Rot. Vel.: " + String.valueOf(velX), x, y + 60);
        g2.drawString("Health: " + String.valueOf(states.getHealth()), x, y + 70);
    }
     
    private void vDisplay(Graphics2D g2, String label, float height, Font fsize, boolean active) {
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
    
    private void hDisplay(Graphics2D g2, String label, float height, int dist, Font fsize, boolean active) {
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
    
    public boolean getRsetV() {
        return resetV;
    }
    
    public void setRsetV(boolean value) {
        this.resetV = value;
    }
    
    public void setF1Key(boolean value) {
        if (value)
            f1Key = value;
        else
            f1Key = value;        
    }
    
    public boolean getF1Key() {
        return f1Key;
    }
    
    public void setEnterKey(boolean value) {
        if (value)
            enterKey = value;
        else
            enterKey = value;   
    }
    
    public void setUpKey(boolean value) {
        if (value)
            upKey = value;
        else
            upKey = value;
    }

    public void setDownKey(boolean value) {
        if (value)
            downKey = value;
        else
            downKey = value;
    }

    public void setLeftKey(boolean value) {
        if (value)
            leftKey = value;
        else
            leftKey = value;
    }

    public void setRightKey(boolean value) {
        if (value)
            rightKey = value;
        else
            rightKey = value;
    }
    
    public void setFont(Font font) {
        this.font = font;
    }

}
