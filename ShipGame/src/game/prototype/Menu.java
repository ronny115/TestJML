package game.prototype;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.prototype.framework.GameStates;

public class Menu {

    private Font font;
    private GameStates gs;
    private FileManagement fileM;
    private int mIndex, fIndex;
    private boolean upKey, downKey, leftKey, rightKey, enterKey, f1Key;
    private boolean saveGame, saveConfirm, saveOverWrite, loadGame, loadConfirm, resetV;
    private List<String> menuItem;
    private List<Boolean> mainMItem = new ArrayList<Boolean>();
    private List<Boolean> pauseItem = new ArrayList<Boolean>();
    private List<Boolean> gameOItem = new ArrayList<Boolean>();
    private List<Boolean> overWriteItem = new ArrayList<Boolean>();
    private List<Boolean> saveItem = new ArrayList<Boolean>();
    private List<Boolean> loadItem = new ArrayList<Boolean>();
    private List<Boolean> slotsItem = new ArrayList<Boolean>();

    public Menu(GameStates s, FileManagement fm) {
        this.gs = s;
        this.fileM = fm;
        menuItem = List.of("PAUSE", "CONTINUE?", "GAME OVER", "New Game", "Load Game", "Save Game", "Restart",
                "Continue", "Exit", "Quit Game", "Save", "Load", "Delete", "Cancel", "Save File",
                "Select File", "Overwrite File?", "Yes", "No", "Load this File?", "Do you want to save?");

        for (int i = 0; i < 3; i++) {
            mainMItem.add(false);            
            loadItem.add(false);
            overWriteItem.add(false);
        }
        mainMItem.set(0, true);        
        loadItem.set(0, true);
        overWriteItem.set(0, true);
        
        for (int i = 0; i < 4; i++)
            pauseItem.add(false);
        pauseItem.set(0, true);

        for (int i = 0; i < 2; i++) {
            gameOItem.add(false);
            saveItem.add(false);            
        }        
        saveItem.set(0, true);
        gameOItem.set(0, true);

        for (int i = 0; i < gs.saveSlots().size(); i++) 
            slotsItem.add(false);
        slotsItem.set(0, true);
    }

    public void update() {

        if (upKey && !loadConfirm && !saveConfirm && !saveOverWrite) {
            menuMove("Up");
            upKey = false;
        }

        if (downKey && !loadConfirm && !saveConfirm && !saveOverWrite) {
            menuMove("Down");
            downKey = false;
        }

        if (leftKey && (loadConfirm || saveConfirm || saveOverWrite)) {
            menuMove("Left");
            leftKey = false;
        }

        if (rightKey && (loadConfirm || saveConfirm || saveOverWrite)) {
            menuMove("Right");
            rightKey = false;
        }

        if (enterKey) {
            menuOption();
            enterKey = false;
        }
    }

    private void menuMove(String dir) {
        // Main menu navigation
        if (!Game.GameOn) {
            menuIndex(mainMItem.size(), dir, "Main");
        }
        // Pause menu navigation
        if (gs.getGamePaused() && Game.GameOn && !saveGame && !loadGame) {
            menuIndex(pauseItem.size(), dir, "Pause");
        }
        // Save slots
        if (gs.getGamePaused() && Game.GameOn && saveGame && !saveConfirm && !saveOverWrite) {
            menuIndex(slotsItem.size(), dir, "FileList");
        }
        // Save options
        else if (saveConfirm) {
            menuIndex(saveItem.size(), dir, "SaveConfirm");
        }
        else if (saveOverWrite) {
            menuIndex(overWriteItem.size(), dir, "OverWrite");
        }
        // Load slots
        if (gs.getGamePaused() && Game.GameOn && loadGame && !loadConfirm) {
            menuIndex(slotsItem.size(), dir, "FileList");
        }
        // Load options
        else if (loadConfirm) {
            menuIndex(loadItem.size(), dir, "LoadConfirm");
        }
        // GameOver menu navigation
        if (Game.GameOver && Game.GameOn) {
            menuIndex(gameOItem.size(), dir, "GameOver");
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
            case "FileList":
                slotsItem.set(mIndex, !slotsItem.get(mIndex));
                break;
            case "SaveConfirm":
                saveItem.set(mIndex, !saveItem.get(mIndex));
                break;
            case "LoadConfirm":
                loadItem.set(mIndex, !loadItem.get(mIndex));
                break;
            case "GameOver":
                gameOItem.set(mIndex, !gameOItem.get(mIndex));
                break;
            case "OverWrite":
                overWriteItem.set(mIndex, !overWriteItem.get(mIndex));
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
        if (!Game.GameOn && mainMItem.get(1)) {
            
        }
        // Quit game
        if (!Game.GameOn && mainMItem.get(2)) {
            System.exit(1);
        }
        ///////////////////////////
        // Continue and game over//
        ///////////////////////////
        if (Game.GameOver && gameOItem.get(0)) {
            if (gs.getLife() > 0) {
                Game.Continued = !Game.Continued;
                resetV = true;
            }
            if (gs.getLife() == 0) {
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
        if (!gs.getGamePaused()) {
            loadConfirm = false;
            loadGame = false;
            saveGame = false;
            saveConfirm = false;
        }
        // Reset
        if (gs.getGamePaused() && pauseItem.get(2)) {
            Game.Reseted = !Game.Reseted;
            resetPauseMenuItem(2);
            resetV = true;
        }
        // Exit
        else if (gs.getGamePaused() && pauseItem.get(3)) {
            Game.GameOn = !Game.GameOn;
            Game.Exited = !Game.Exited;
            resetPauseMenuItem(3);
        }
        /////////////
        // Save Game/
        /////////////
        else if (gs.getGamePaused() && pauseItem.get(1)) {
            saveGame = true;
            fileM.loadFiles();
            resetPauseMenuItem(1);
        }
        ////Save Game sub-menu
        else if (saveGame && !saveConfirm && !saveOverWrite && slotsItem.get(mIndex)) {
            fIndex = mIndex;
            if (gs.saveSlots().get(mIndex) == "Empty") {
                saveConfirm = true;
                if (mIndex == 0) {
                    slotsItem.set(0, true);
                } else {
                    slotsItem.set(0, true);
                    slotsItem.set(mIndex, false);
                }
            } else {
                saveOverWrite = true;
            }
            mIndex = 0;
            resetV = true;
        }
        /////// Save over write
        // Yes
        else if (saveOverWrite && overWriteItem.get(0)) {
            fileM.deleteFile(fIndex);
            fileM.save();
            System.out.println("Saved");
            if (fIndex == 0) {
                slotsItem.set(0, true);
            } else {
                slotsItem.set(0, true);
                slotsItem.set(fIndex, false);
            }
            mIndex = 0;
            saveOverWrite = false;
            saveGame = false;
        }
        // No
        else if (saveOverWrite && overWriteItem.get(1)) {
            overWriteItem.set(0, !overWriteItem.get(0));
            overWriteItem.set(1, !overWriteItem.get(1));
            if (fIndex == 0) {
                slotsItem.set(0, true);
            } else {
                slotsItem.set(0, true);
                slotsItem.set(fIndex, false);
            }
            mIndex = 0;
            saveOverWrite = false;
        }
        // Delete
        else if (saveOverWrite && overWriteItem.get(2)) {
            System.out.println("deleted");
            fileM.deleteFile(fIndex);
            overWriteItem.set(0, !overWriteItem.get(0));
            overWriteItem.set(2, !overWriteItem.get(2));
            slotsItem.set(0, true);
            slotsItem.set(fIndex, false);
            mIndex = 0;
            resetV = true;
            saveOverWrite = false;
        }        
        ////// Save Confirmation
        // Save
        else if (saveConfirm && saveItem.get(0)) {
            System.out.println("Saved");
            fileM.save();
            mIndex = 0;
            resetV = true;
            saveConfirm = false;
            saveGame = false;
        }
        // Close
        else if (saveConfirm && saveItem.get(1)) {            
            saveItem.set(0, !saveItem.get(0));
            saveItem.set(1, !saveItem.get(1));           
            mIndex = 0;
            resetV = true;
            saveConfirm = false;
            saveGame = false;
        }
        /////////////
        // Load Game/
        /////////////
        else if (gs.getGamePaused() && pauseItem.get(0)) {
            pauseItem.set(0, !pauseItem.get(0));
            loadGame = true;
            fileM.loadFiles();
        }
        //// Load Game sub-menu
        else if (loadGame && !loadConfirm && slotsItem.get(mIndex)) {
            fIndex = mIndex;
            if (gs.saveSlots().get(mIndex) == "Empty") {
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
        ////// Load Confirmation
        // Load
        else if (loadConfirm && loadItem.get(0)) {
            System.out.println("Loaded");
            System.out.println(gs.saveSlots().get(fIndex));
            fileM.load(gs.saveSlots().get(fIndex));
            pauseItem.set(0, !pauseItem.get(0));
            mIndex = 0;
            resetV = true;
            loadConfirm = false;
            loadGame = false;
            gs.setGamePaused(!gs.getGamePaused());
        }
        // Delete
        else if (loadConfirm && loadItem.get(1)) {
            System.out.println("deleted");
            fileM.deleteFile(fIndex);
            loadItem.set(0, !loadItem.get(0));
            loadItem.set(1, !loadItem.get(1));
            mIndex = 0;
            resetV = true;
            loadConfirm = false;
        }
        // Close
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
        // TODO Add Options
        g2.drawImage(Game.getTexInstance().title, Game.getTexInstance().title.getWidth() + 100,
                Game.getTexInstance().title.getHeight() + 50, 400, 175, null);
        g2.setColor(Color.BLACK);
        vDisplay(g2, menuItem.get(3), 0.55f, font.deriveFont(15f), mainMItem.get(0));
        vDisplay(g2, menuItem.get(4), 0.65f, font.deriveFont(15f), mainMItem.get(1));
        vDisplay(g2, menuItem.get(9), 0.75f, font.deriveFont(15f), mainMItem.get(2));
    }

    public void pauseRender(Graphics2D g2) {
        // Pause menu title
        g2.drawImage(Game.getTexInstance().cristal, 0, 0, Game.WIDTH, Game.HEIGHT, null);
        g2.setColor(Color.BLACK);
        vDisplay(g2, menuItem.get(0), 0.25f, font.deriveFont(100f), false);

        if (saveGame) {
            vDisplay(g2, menuItem.get(14), 0.45f, font.deriveFont(25f), false);

            for (int i = 0; i < gs.saveSlots().size(); i++) {
                vDisplay(g2, gs.saveSlots().get(i), 0.55f + (i * 0.05f), font.deriveFont(10f), slotsItem.get(i));
            }

            if (saveConfirm) {
                g2.drawImage(Game.getTexInstance().window,
                        (Game.WIDTH - Game.getTexInstance().cristal.getWidth() - 200) / 2,
                        (Game.HEIGHT - Game.getTexInstance().cristal.getHeight() + 200) / 2, null);
                vDisplay(g2, menuItem.get(20), 0.50f, font.deriveFont(25f), false);
                hDisplay(g2, menuItem.get(10), 0.60f, -50, font.deriveFont(15f), saveItem.get(0));
                hDisplay(g2, menuItem.get(13), 0.60f, 60, font.deriveFont(15f), saveItem.get(1));
            } else if (saveOverWrite) {
                g2.drawImage(Game.getTexInstance().window,
                        (Game.WIDTH - Game.getTexInstance().cristal.getWidth() - 200) / 2,
                        (Game.HEIGHT - Game.getTexInstance().cristal.getHeight() + 200) / 2, null);
                vDisplay(g2, menuItem.get(16), 0.50f, font.deriveFont(25f), false);
                hDisplay(g2, menuItem.get(17), 0.60f, -100, font.deriveFont(15f), overWriteItem.get(0));
                hDisplay(g2, menuItem.get(18), 0.60f, 0, font.deriveFont(15f), overWriteItem.get(1));
                hDisplay(g2, menuItem.get(12), 0.60f, 100, font.deriveFont(15f), overWriteItem.get(2));
            }
        } else if (loadGame) {
            vDisplay(g2, menuItem.get(15), 0.45f, font.deriveFont(25f), false);

            for (int i = 0; i < gs.saveSlots().size(); i++) {
                vDisplay(g2, gs.saveSlots().get(i), 0.55f + (i * 0.05f), font.deriveFont(10f), slotsItem.get(i));
            }

            if (loadConfirm) {
                g2.drawImage(Game.getTexInstance().window,
                        (Game.WIDTH - Game.getTexInstance().cristal.getWidth() - 200) / 2,
                        (Game.HEIGHT - Game.getTexInstance().cristal.getHeight() + 200) / 2, null);
                vDisplay(g2, menuItem.get(19), 0.50f, font.deriveFont(25f), false);
                hDisplay(g2, menuItem.get(11), 0.60f, -100, font.deriveFont(15f), loadItem.get(0));
                hDisplay(g2, menuItem.get(12), 0.60f, 0, font.deriveFont(15f), loadItem.get(1));
                hDisplay(g2, menuItem.get(13), 0.60f, 110, font.deriveFont(15f), loadItem.get(2));
            }
        } else {
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
                             int objSize, float posX, float posY, float velY, float velX) 
    {
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        g2.drawString("Fps: " + String.valueOf(fps), x, y);
        g2.drawString("Ticks: " + String.valueOf(tickrate), x, y + 10);
        g2.drawString("Nº obj: " + String.valueOf(objSize), x, y + 20);
        g2.drawString("X: " + String.valueOf(posX), x, y + 30);
        g2.drawString("Y: " + String.valueOf(posY), x, y + 40);
        g2.drawString("Velocity: " + String.valueOf(velY), x, y + 50);
        g2.drawString("Rot. Vel.: " + String.valueOf(velX), x, y + 60);
        g2.drawString("Health: " + String.valueOf(gs.getHealth()), x, y + 70);
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
