package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;

import game.prototype.framework.GameStates;
import game.prototype.menu.ContinueOverMenu;
import game.prototype.menu.LoadMenu;
import game.prototype.menu.MainMenu;
import game.prototype.menu.PauseMenu;
import game.prototype.menu.SaveMenu;

public class Menu {

    private Font font;
    private GameStates gs;
    private FileManagement fileM;
    private ResourceLoader loader;
    private int mIndex, xStats, yStats, fps, tick, objSize;
    private float fPosX, fPosY, fVelY, fVelX;
    private boolean upKey, downKey, leftKey, rightKey, enterKey, escKey, f1Key;
    public boolean saveGame, saveConfirm, saveOverWrite, resetV;
    public boolean loadGame, loadConfirm;

    private MainMenu mm;
    private PauseMenu pm;
    private LoadMenu lm;
    private SaveMenu sm;
    private ContinueOverMenu cm;

    private List<String> mmItem = List.of("New Game", "Load Game", "Quit Game");
    private List<String> pmItem = List.of("PAUSE", "Load Game", "Save Game", "Restart", "Exit");
    private List<String> lmItem = List.of("PAUSE", "Select File", "Load this File?", "Load", "Delete", "Cancel");
    private List<String> smItem = List.of("PAUSE", "Save File", "Do you want to save?", "Overwrite File?", "Yes", "No", "Delete");
    private List<String> coItem = List.of("CONTINUE?", "GAME OVER", "Continue", "Restart", "Exit");

    public Menu(GameStates gs, FileManagement fm) {
        this.gs = gs;
        this.fileM = fm;
        loader = new ResourceLoader();
        this.font = loader.loadFont("/PressStart2P.ttf");
        this.lm = new LoadMenu(this, gs, lmItem, 0.55f, 0.05f, 10f);
        this.sm = new SaveMenu(this, gs, smItem, 0.55f, 0.05f, 10f);
        this.mm = new MainMenu(this, gs, mmItem, 0.55f, 0.10f, 15f);
        this.pm = new PauseMenu(this, gs, pmItem, 0.55f, 0.10f, 15f);
        this.cm = new ContinueOverMenu(this, gs, coItem, 0.65f, 0.10f, 15f);

        mm.setFont(this.font);
        pm.setFont(this.font);
        lm.setFont(this.font);
        sm.setFont(this.font);
        cm.setFont(this.font);
    }

    public void update() {
        // Escape handle
        if (escKey && Game.GameOn && !Game.GameOver 
            && !loadGame && !loadConfirm
            && !saveGame && !saveConfirm) {
            gs.setGamePaused(!gs.getGamePaused());
            pm.resetPauseMenu();
            resetIndex();
            escKey = false;
        } else if (escKey && loadGame && !loadConfirm) {
            pm.resetPauseMenu();
            resetIndex();   
            loadGame = false;
            escKey = false;
        } else if (escKey && saveGame && !saveConfirm && !saveOverWrite) {    
            pm.resetPauseMenu();
            resetIndex();   
            saveGame = false;
            escKey = false;   
        } else if (escKey && loadConfirm) {
            lm.resetLoadMenu();
            resetIndex();
            loadConfirm = false;
            loadGame = true;    
            escKey = false;
        } else if (escKey && saveConfirm) {
            sm.resetSaveMenu();
            resetIndex();
            saveConfirm = false;
            saveGame = true;    
            escKey = false;
        } else if (escKey && saveOverWrite) {
            sm.resetSaveMenu();
            resetIndex();
            saveOverWrite = false;
            saveConfirm = false;   
            escKey = false;
        }
        // Update handle
        if (!Game.GameOn)
            mm.update();
        else if (gs.getGamePaused())
            if (loadGame) 
                lm.update();
            else
                lm.resetLoadMenu();
            if (saveGame)
                sm.update();
            else
                sm.resetSaveMenu();
            if (!loadGame && !saveGame)
                pm.update();
        else {
            if (Game.GameOver)
                cm.update();        
            pm.resetPauseMenu();
        }
    }

    public void render(Graphics2D g2) {
        if (!Game.GameOn)
            mm.render(g2);
        else if (gs.getGamePaused()) {
            if (loadGame) 
                lm.render(g2);       
            else if (saveGame)            
                sm.render(g2);
            else
                pm.render(g2);
        } 
        else if (Game.GameOver)
            cm.render(g2);
        if (f1Key) {
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            g2.drawString("Fps: " + String.valueOf(fps), xStats, yStats);
            g2.drawString("Ticks: " + String.valueOf(tick), xStats, yStats + 10);
            g2.drawString("Nº obj: " + String.valueOf(objSize), xStats, yStats + 20);
            g2.drawString("X: " + String.valueOf(fPosX), xStats, yStats + 30);
            g2.drawString("Y: " + String.valueOf(fPosY), xStats, yStats + 40);
            g2.drawString("Velocity: " + String.valueOf(fVelY), xStats, yStats + 50);
            g2.drawString("Rot. Vel.: " + String.valueOf(fVelX), xStats, yStats + 60);
            g2.drawString("Health: " + String.valueOf(gs.getHealth()), xStats, yStats + 70);
        }
    }

    public int mIndex(int size, String dir) {
        if (mIndex > size)
            mIndex = 0;
        if (dir == "Up" || dir == "Left") {
            mIndex--;
            if (mIndex < 0) {
                mIndex = size - 1;
                return mIndex;
            }
            return mIndex;
        }
        if (dir == "Down" || dir == "Right") {
            mIndex++;
            if (mIndex == size){
                mIndex = 0;
                return mIndex;
            }
            return mIndex;
        }
        return 0;
    }

    public void resetIndex() {
        mIndex = 0;
    }
    
    public void statsDisplay(int x, int y, int fps, int tickrate, int objSize, 
                            float posX, float posY, float velY, float velX) {
        this.xStats = x;
        this.yStats = y;
        this.fps = fps;
        this.tick = tickrate;
        this.objSize = objSize;
        this.fPosX = posX;
        this.fPosY = posY;
        this.fVelX = velX;
        this.fVelY = velY;  
    }

    public void items_arrange_v(Graphics2D g2, String label, float height, 
                                Font font, boolean active) {
        int x = (Game.WIDTH - g2.getFontMetrics(font).stringWidth(label)) / 2;
        int y = (int) (Game.HEIGHT * height);

        if (active)
            g2.setColor(Color.GRAY);
        else
            g2.setColor(Color.BLACK);

        g2.setFont(font);
        g2.drawString(label, x, y);
        g2.setColor(Color.BLACK);
    }

    public void items_arrange_h(Graphics2D g2, String label, float height, 
                                int dist, Font font, boolean active) {
        int x = (Game.WIDTH - g2.getFontMetrics(font).stringWidth(label)) / 2 + dist;
        int y = (int) (Game.HEIGHT * height);

        if (active)
            g2.setColor(Color.GRAY);
        else
            g2.setColor(Color.BLACK);

        g2.setFont(font);
        g2.drawString(label, x, y);
        g2.setColor(Color.BLACK);
    }

    public boolean getRsetV() {
        return resetV;
    }

    public void setRsetV(boolean value) {
        this.resetV = value;
    }
    // Key handle
    public boolean getF1Key() {
        return f1Key;
    }

    public void setF1Key(boolean value) {
        f1Key = value;
    }

    public boolean getEnterKey() {
        return enterKey;
    }

    public void setEnterKey(boolean value) {
        enterKey = value;
    }

    public boolean getUpKey() {
        return upKey;
    }

    public void setUpKey(boolean value) {
        upKey = value;
    }

    public boolean getDownKey() {
        return downKey;
    }

    public void setDownKey(boolean value) {
        downKey = value;
    }

    public boolean getLeftKey() {
        return leftKey;
    }

    public void setLeftKey(boolean value) {
        leftKey = value;
    }

    public boolean getRightKey() {
        return rightKey;
    }

    public void setRightKey(boolean value) {
        rightKey = value;
    }

    public boolean getEscapeKey() {
        return escKey;
    }

    public void setEscapeKey(boolean value) {
        escKey = value;
    }
}