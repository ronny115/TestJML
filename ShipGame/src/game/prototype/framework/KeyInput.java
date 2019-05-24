package game.prototype.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import game.prototype.Game;
import game.prototype.Menu;
import game.prototype.Movement;
import game.prototype.Shoot;

public class KeyInput extends KeyAdapter {

    private Menu menu;
    private GameStates gs;
    private Movement move;
    private Shoot s;

    public KeyInput(Menu menu, Movement m, GameStates gs, Shoot s) {
        this.menu = menu;
        this.move = m;
        this.gs = gs;
        this.s = s;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            menu.setF1Key(!menu.getF1Key());
        }
        
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            move.setUpKey(true);
            menu.setUpKey(true);
            if (Game.GameOn && !gs.getGamePaused()) {
                gs.setPropulsionState(true);
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
            menu.setEscapeKey(true);
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
                gs.setPropulsionState(false);
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

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            menu.setEscapeKey(false);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            s.setSpaceKey(false);
        }
        
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            menu.setEnterKey(false);
        }
    }
}
