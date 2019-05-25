package game.prototype.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.prototype.Game;
import game.prototype.Menu;
import game.prototype.framework.GameStates;

public class PauseMenu {

    private Menu menu;
    private Font font;
    private GameStates gs;
    private float menu_position, increment, font_size;
    private int index;
    private List<String> menu_item;
    private List<Boolean> menu_switch = new ArrayList<Boolean>();
    
    public PauseMenu(Menu menu, GameStates gs, List<String> menu_item, 
                    float position, float increment, float size) {
        this.menu = menu;
        this.gs = gs;
        this.menu_item = menu_item;
        this.font_size = size;
  
        this.menu_position = position;
        this.increment = increment;

        for (int i = 0; i < this.menu_item.size() - 1; i++)
            menu_switch.add(false);
        menu_switch.set(0, true);
    }

    public void update() {
        if (menu.getUpKey()) {
            menu_switch.set(index, !menu_switch.get(index));
            index = menu.mIndex(menu_item.size() - 1, "Up");
            menu_switch.set(index, !menu_switch.get(index));
            menu.setUpKey(false);
        }
        if (menu.getDownKey()) {
            menu_switch.set(index, !menu_switch.get(index));
            index = menu.mIndex(menu_item.size() - 1, "Down");
            menu_switch.set(index, !menu_switch.get(index));
            menu.setDownKey(false);
        }
        // Load Game
        if (menu.getEnterKey() && menu_switch.get(0)) {
            menu.loadGame = true;
            resetPauseMenu();
            menu.resetIndex();
            menu.setEnterKey(false);
        }
        // Save Game
        if (menu.getEnterKey() && menu_switch.get(1)) {
            menu.saveGame = true;
            resetPauseMenu();
            menu.resetIndex();
            menu.setEnterKey(false);
        }
        // Restart
        if (menu.getEnterKey() && menu_switch.get(2)) {
            Game.Reseted = !Game.Reseted;
            resetPauseMenu();
            menu.setResetVelocity(true);
            menu.setEnterKey(false);
        }
        // Exit
        if (menu.getEnterKey() && menu_switch.get(3)) {
            Game.GameOn = !Game.GameOn;
            Game.Exited = !Game.Exited;
            resetPauseMenu();
            menu.setResetVelocity(true);
            menu.setEnterKey(false);
        }
    }

    public void render(Graphics2D g2) {
        g2.drawImage(Game.getTexInstance().cristal, 0, 0, Game.WIDTH, Game.HEIGHT, null);
        g2.setColor(Color.BLACK);

        for (int i = 0; i < menu_item.size(); i++) {
            if (i == 0)
                menu.items_arrange_v(g2, menu_item.get(i), 0.25f, 
                                    font.deriveFont(100f), false);
            else
                menu.items_arrange_v(g2, menu_item.get(i), menu_position + ((i - 1) * increment), 
                                    font.deriveFont(font_size), 
                                    menu_switch.get(i - 1));    
        }    
    }

    public void resetPauseMenu() {
        menu_switch.set(index, !menu_switch.get(index));
        menu_switch.set(0, !menu_switch.get(0));
        index = 0;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}