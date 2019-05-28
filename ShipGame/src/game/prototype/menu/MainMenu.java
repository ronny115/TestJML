package game.prototype.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.prototype.Game;
import game.prototype.Menu;
import game.prototype.framework.GameStates;

public class MainMenu {

    private Menu menu;
    private Font font;
    private GameStates gs;
    private float menu_position, increment, font_size;
    private int index;
    private List<String> menu_item;
    private List<Boolean> menu_switch = new ArrayList<Boolean>();

    public MainMenu(Menu menu, GameStates gs, List<String> menu_item, 
                    float position, float increment, float size) {
        this.menu = menu;
        this.gs = gs;
        this.menu_item = menu_item;
        this.menu_position = position;
        this.increment = increment;
        this.font_size = size;

        for (int i = 0; i < this.menu_item.size(); i++)
            menu_switch.add(false);
        menu_switch.set(0, true);
    }

    public void update() {
        if (menu.getUpKey()) {      
            menu_switch.set(index, !menu_switch.get(index));
            index = menu.mIndex(menu_item.size(), "Up");
            menu_switch.set(index, !menu_switch.get(index));
            menu.setUpKey(false);
        }

        if (menu.getDownKey()) {
            menu_switch.set(index, !menu_switch.get(index));
            index = menu.mIndex(menu_item.size(), "Down");
            menu_switch.set(index, !menu_switch.get(index));
            menu.setDownKey(false);
        }
        // New Game
        if (menu.getEnterKey() && menu_switch.get(0)) {
            Game.GameOn = !Game.GameOn;
            resetMainMenu();
            menu.resetIndex();
            menu.setEnterKey(false);
        }
        // Load
        if (menu.getEnterKey() && menu_switch.get(1)) {
            menu.loadGame = true;
            resetMainMenu();
            menu.resetIndex();
            menu.setEnterKey(false);
        }
        // TODO add options 
        // Exit 
        if (menu.getEnterKey() && menu_switch.get(3)) 
            System.exit(1);
    }

    public void render(Graphics2D g2) {
        g2.drawImage(Game.getTexInstance().title, 
                    Game.getTexInstance().title.getWidth() + 100,
                    Game.getTexInstance().title.getHeight() + 50, 400, 175, null);
        g2.setColor(Color.BLACK);

        for (int i = 0; i < menu_item.size(); i++) 
            menu.items_arrange_v(g2, menu_item.get(i), menu_position + (i * increment), 
                                font.deriveFont(font_size), 
                                menu_switch.get(i));       
    }

    public void resetMainMenu() {
        menu_switch.set(index, !menu_switch.get(index));
        menu_switch.set(0, !menu_switch.get(0));
        index = 0;
    }

    public void setFont(Font font) {
        this.font = font;
    }
}