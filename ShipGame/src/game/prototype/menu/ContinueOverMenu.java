package game.prototype.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import game.prototype.Game;
import game.prototype.Menu;
import game.prototype.framework.GameStates;

public class ContinueOverMenu {

    private Menu menu;
    private Font font;
    private GameStates gs;
    private float menu_position, increment, font_size;
    private int index;
    private List<String> menu_item;
    private List<Boolean> continue_switch = new ArrayList<Boolean>();

    public ContinueOverMenu(Menu menu, GameStates gs, List<String> menu_item, 
                            float position, float increment, float size) {
        this.menu = menu;
        this.gs = gs;
        this.menu_item = menu_item;
        this.font_size = size;
        this.menu_position = position;
        this.increment = increment;

        for (int i = 0; i < 2; i++)
            continue_switch.add(false);
        continue_switch.set(0, true);
    }

    public void update() {
        if (menu.getUpKey()) {
            continue_switch.set(index, !continue_switch.get(index));
            index = menu.mIndex(continue_switch.size(), "Up");
            continue_switch.set(index, !continue_switch.get(index));
            menu.setUpKey(false);
        }
        if (menu.getDownKey()) {
            continue_switch.set(index, !continue_switch.get(index));
            index = menu.mIndex(continue_switch.size(), "Down");
            continue_switch.set(index, !continue_switch.get(index));
            menu.setDownKey(false);
        }
        // Continue
        if (gs.getLife() > 0) {
            if (menu.getEnterKey() && continue_switch.get(0)) {
                Game.Continued = !Game.Continued;
                resetContinueOverMenu();
                menu.setResetVelocity(true);
                menu.setEnterKey(false);
            }
        }
        // Game Over - Restart
        else {
            if (menu.getEnterKey() && continue_switch.get(0)) {
                Game.Reseted = !Game.Reseted;
                resetContinueOverMenu();
                menu.setResetVelocity(true);
                menu.setEnterKey(false);
            }
        }
        // Exit
        if (menu.getEnterKey() && continue_switch.get(1)) {
            Game.GameOn = !Game.GameOn;
            Game.Exited = !Game.Exited;
            resetContinueOverMenu();
            menu.setEnterKey(false);
        }
    }

    public void render(Graphics2D g2) {
        g2.drawImage(Game.getTexInstance().cristal, 0, 0, Game.WIDTH, Game.HEIGHT, null);
        g2.setColor(Color.BLACK);

        if (gs.getLife() > 0) {
            menu.items_arrange_v(g2, menu_item.get(0), 0.25f, font.deriveFont(100f), false);
            menu.items_arrange_v(g2, menu_item.get(2), menu_position, 
                                font.deriveFont(font_size),
                                continue_switch.get(0));
            menu.items_arrange_v(g2, menu_item.get(4), menu_position + increment, 
                                font.deriveFont(font_size),
                                continue_switch.get(1));
        } else {
            menu.items_arrange_v(g2, menu_item.get(1), 0.25f, font.deriveFont(100f), false);
            menu.items_arrange_v(g2, menu_item.get(3), menu_position, 
                                font.deriveFont(font_size),
                                continue_switch.get(0));
            menu.items_arrange_v(g2, menu_item.get(4), menu_position + increment, 
                                font.deriveFont(font_size),
                                continue_switch.get(1));
        }
    }

    public void resetContinueOverMenu() {
        continue_switch.set(index, !continue_switch.get(index));
        continue_switch.set(0, !continue_switch.get(0));
        index = 0;
        menu.resetIndex();
    }

    public void setFont(Font font) {
        this.font = font;
    }
}
