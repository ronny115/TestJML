package game.prototype.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.prototype.FileManagement;
import game.prototype.Game;
import game.prototype.Menu;
import game.prototype.framework.GameStates;

public class SaveMenu {

    private Menu menu;
    private Font font;
    private GameStates gs;
    private FileManagement fileMgmt;
    private float menu_position, increment, font_size;
    private int index, index_2, index_3, h_dist[] = {-100, 0, 110, -50, 60};
    private int slotIndex;
    private List<String> menu_item;
    private List<Boolean> menu_switch = new ArrayList<Boolean>();
    private List<Boolean> confirm_switch = new ArrayList<Boolean>();
    private List<Boolean> overwrite_switch = new ArrayList<Boolean>();

    public SaveMenu(Menu menu, GameStates gs, List<String> menu_item, 
                    float position, float increment, float size, FileManagement fm) {
        this.menu = menu;
        this.gs = gs;
        this.menu_item = menu_item;
        this.font_size = size;
        this.menu_position = position;
        this.increment = increment;
        this.fileMgmt = fm;
                
        for (int i = 0; i < gs.saveSlots().size(); i++)
            menu_switch.add(false);
        menu_switch.set(0, true);

        for (int i = 5; i < menu_item.size(); i++)
            confirm_switch.add(false);
        confirm_switch.set(0, true);

        for (int i = 4; i < menu_item.size(); i++)
            overwrite_switch.add(false);
        overwrite_switch.set(0, true);
    }

    public void update() {
        if (!menu.saveConfirm && !menu.saveOverWrite) {
            if (menu.getUpKey()) {
                menu_switch.set(index, !menu_switch.get(index));
                index = menu.mIndex(gs.saveSlots().size(), "Up");
                menu_switch.set(index, !menu_switch.get(index));
                menu.setUpKey(false);
            }
            if (menu.getDownKey()) {
                menu_switch.set(index, !menu_switch.get(index));
                index = menu.mIndex(gs.saveSlots().size(), "Down");
                menu_switch.set(index, !menu_switch.get(index));
                menu.setDownKey(false);
            }
            // Slots
            if (menu.getEnterKey() && gs.saveSlots().get(index) != "Empty") {
                slotIndex = index;
                menu.saveOverWrite = true;
                resetSaveMenu();
                menu.resetIndex();
                menu.setEnterKey(false);
            } else if (menu.getEnterKey() && gs.saveSlots().get(index) == "Empty") {
                menu.saveConfirm = true;
                resetSaveMenu();
                menu.resetIndex();
                menu.setEnterKey(false);
            }
        } else if (menu.saveConfirm) {
            if (menu.getLeftKey()) {
                confirm_switch.set(index_2, !confirm_switch.get(index_2));
                index_2 = menu.mIndex(confirm_switch.size(), "Left");
                confirm_switch.set(index_2, !confirm_switch.get(index_2));
                menu.setLeftKey(false);
            }
            if (menu.getRightKey()) {
                confirm_switch.set(index_2, !confirm_switch.get(index_2));
                index_2 = menu.mIndex(confirm_switch.size(), "Right");
                confirm_switch.set(index_2, !confirm_switch.get(index_2));
                menu.setRightKey(false);
            }
            // Save func.
            if (menu.getEnterKey() && confirm_switch.get(0)) {
                fileMgmt.save();
                menu.resetIndex();
                menu.saveConfirm = false;
                menu.saveOverWrite = false;
                menu.setEnterKey(false);
            }
            // No
            if (menu.getEnterKey() && confirm_switch.get(1)) {
                resetSaveMenu();
                menu.saveConfirm = false;
                menu.saveOverWrite = false;
                menu.resetIndex();
                menu.setEnterKey(false);
            }

        } else if (menu.saveOverWrite) {
            if (menu.getLeftKey()) {
                overwrite_switch.set(index_3, !overwrite_switch.get(index_3));
                index_3 = menu.mIndex(overwrite_switch.size(), "Left");
                overwrite_switch.set(index_3, !overwrite_switch.get(index_3));
                menu.setLeftKey(false);
            }
            if (menu.getRightKey()) {
                overwrite_switch.set(index_3, !overwrite_switch.get(index_3));
                index_3 = menu.mIndex(overwrite_switch.size(), "Right");
                overwrite_switch.set(index_3, !overwrite_switch.get(index_3));
                menu.setRightKey(false);
            }
            // Overwrite yes.
            if (menu.getEnterKey() && overwrite_switch.get(0)) {
                fileMgmt.deleteFile(slotIndex);
                fileMgmt.save();
                menu.resetIndex();
                menu.saveOverWrite = false;
                menu.setEnterKey(false);
                slotIndex = 0;
            }
            // No.
            if (menu.getEnterKey() && overwrite_switch.get(1)) {
                resetSaveMenu();
                menu.saveOverWrite = false;
                menu.resetIndex();
                menu.setEnterKey(false);
            }
            // Delete file
            if (menu.getEnterKey() && overwrite_switch.get(2)) {
                fileMgmt.deleteFile(slotIndex);
                resetSaveMenu();
                menu.saveOverWrite = false;
                menu.resetIndex();
                menu.setEnterKey(false);
                slotIndex = 0;
            }
        }
    }

    public void render(Graphics2D g2) {
        g2.drawImage(Game.getTexInstance().cristal, 0, 0, Game.WIDTH, Game.HEIGHT, null);
        g2.setColor(Color.BLACK);

        menu.items_arrange_v(g2, menu_item.get(0), 0.25f, font.deriveFont(100f), false);
        menu.items_arrange_v(g2, menu_item.get(1), 0.45f, font.deriveFont(25f), false);

        if (menu.saveGame) {
            for (int i = 0; i < menu_switch.size(); i++) 
                menu.items_arrange_v(g2, gs.saveSlots().get(i), menu_position + (i * increment), 
                                    font.deriveFont(font_size), 
                                    menu_switch.get(i));
        } 
        if (menu.saveConfirm) {
            g2.drawImage(Game.getTexInstance().window,
                        (Game.WIDTH - Game.getTexInstance().cristal.getWidth() - 200) / 2,
                        (Game.HEIGHT - Game.getTexInstance().cristal.getHeight() + 200) / 2, null);
            
            menu.items_arrange_v(g2, menu_item.get(2), menu_position - 0.05f, 
                                font.deriveFont(font_size + 15f), false);

            menu.items_arrange_h(g2, menu_item.get(4), menu_position + 0.05f, 
                                h_dist[3], font.deriveFont(font_size + 5f), 
                                confirm_switch.get(0));
            menu.items_arrange_h(g2, menu_item.get(5), menu_position + 0.05f, 
                                h_dist[4], font.deriveFont(font_size + 5f), 
                                confirm_switch.get(1));
        } 
        if (menu.saveOverWrite) {
            g2.drawImage(Game.getTexInstance().window,
                        (Game.WIDTH - Game.getTexInstance().cristal.getWidth() - 200) / 2,
                        (Game.HEIGHT - Game.getTexInstance().cristal.getHeight() + 200) / 2, null);
                        
            menu.items_arrange_v(g2, menu_item.get(3), menu_position - 0.05f, 
                                font.deriveFont(font_size + 15f), false);

            for (int i = 0; i < 3; i++)
                menu.items_arrange_h(g2, menu_item.get(i + 4), menu_position + 0.05f, 
                                    h_dist[i], font.deriveFont(font_size + 5f), 
                                    overwrite_switch.get(i));
        }
    }

    public void resetSaveMenu() {
        menu_switch.set(index, !menu_switch.get(index));
        menu_switch.set(0, !menu_switch.get(0));
        index = 0;
        confirm_switch.set(index_2, !confirm_switch.get(index_2));
        confirm_switch.set(0, !confirm_switch.get(0));
        index_2 = 0;
        overwrite_switch.set(index_3, !overwrite_switch.get(index_3));
        overwrite_switch.set(0, !overwrite_switch.get(0));
        index_3 = 0;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}