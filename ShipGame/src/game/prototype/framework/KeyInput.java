package game.prototype.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.Menu;
import game.prototype.Movement;
import game.prototype.Shoot;

public class KeyInput extends KeyAdapter {

    private Handler handler;
    private Menu menu;
    private States st;
    private Movement move;
    private Shoot s;

    public KeyInput(Handler h, Menu menu, Movement m, States s, Shoot sh) {
        this.handler = h;
        this.menu = menu;
        this.move = m;
        this.st = s;
        this.s = sh;
    }

    public void keyPressed(KeyEvent e) {

        if (e.getKeyCode() == KeyEvent.VK_F1) {
            menu.setF1Key(!menu.getF1Key());
        }
        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            move.setUpKey(true);
            menu.setUpKey(true);
            if (!Game.Paused) {
                st.setPropulsionState(true);
            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            move.setDownKey(true);
            menu.setDownKey(true);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            move.setLeftKey(true);
            menu.setLeftKey(true);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            move.setRightKey(true);
            menu.setRightKey(true);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (handler.player.size() > 0) {
                Game.Paused = !Game.Paused;
            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            s.setSpaceKey(true);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            menu.setEnterKey(true);
        }
    }

    public void keyReleased(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            move.setUpKey(false);
            menu.setUpKey(false);
            if (Game.GameOn) {
                st.setPropulsionState(false);
            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            move.setDownKey(false);
            menu.setDownKey(false);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            move.setLeftKey(false);
            menu.setLeftKey(false);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            move.setRightKey(false);
            menu.setRightKey(false);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            s.setSpaceKey(false);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            menu.setEnterKey(false);
        }

    }
}
