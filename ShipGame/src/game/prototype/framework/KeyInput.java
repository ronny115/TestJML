package game.prototype.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import game.prototype.Game;
import game.prototype.HUD;
import game.prototype.Handler;
import game.prototype.Menu;
import game.prototype.objects.Projectile;

public class KeyInput extends KeyAdapter {

    private Handler handler;
    private Menu menu;
    private HUD hud;
    private int menuIndex = 0;
    private float topSpeed = 0, deltaSpeed = 0, vely = 0, velx = 0;
    //SysInfo
    private boolean f1Press, saveGameState;
    // Movement
    private boolean upKeyPressed, downKeyPressed, leftKeyPressed, rightKeyPressed;
    private boolean upKeyReleased, downKeyReleased, leftKeyReleased, rightKeyReleased;
    // Shooting
    private boolean spaceKeyPressed;
    private Point2D.Float[] firePos = new Point2D.Float[5];

    public KeyInput(Handler handler, Menu menu) {
        this.handler = handler;
        this.menu = menu;
    }

    public void updateInput(HUD hud) {
        this.hud = hud;
        // Movement
        if (upKeyPressed) {
            move("up");
            if (upKeyPressed && leftKeyPressed) {
                move("up");
                move("left");
            } else if (upKeyPressed && rightKeyPressed) {
                move("up");
                move("right");
            } else if (leftKeyReleased) {
                move("decelerateLeft");
            } else if (rightKeyReleased) {
                move("decelerateRight");
            }
        }
        if (downKeyPressed) {
            move("down");
        }
        if (leftKeyPressed) {
            move("left");
        }
        if (rightKeyPressed) {
            move("right");
        }
        // Released
        if (upKeyReleased) {
            move("decelerateUp");
        }
        if (downKeyReleased) {
            move("decelerateDown");
        }
        if (leftKeyReleased && !leftKeyPressed && !rightKeyPressed) {
            move("decelerateLeft");
        }
        if (rightKeyReleased && !rightKeyPressed && !leftKeyPressed) {
            move("decelerateRight");
        }
        // Shooting
        if (spaceKeyPressed && handler.player.size() > 0) {
            handler.addObject(new Projectile(firePos[1].x, firePos[1].y, 5, 20, 20, "player",
                                            Helper.getAngle(handler.player.get(0).points()), 
                                            ObjectId.Projectile));
            spaceKeyPressed = !spaceKeyPressed;
        }
    }
    
    public void keyPressed(KeyEvent e) { 
        int mainMenuSize = menu.mainMenuItem().size();
        int pauseSize = menu.pauseItem().size();
        int gameOverSize = menu.gameOverItem().size();
        
        if (e.getKeyCode() == KeyEvent.VK_F1) {
            f1Press = !f1Press;
            menu.setSysInfo(f1Press);
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            upKeyPressed = true;
            upKeyReleased = false;
            if(!menu.getSaveGameState()) {
                menuMove(mainMenuSize, pauseSize, gameOverSize, "Up");
            } 
        }
        
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downKeyPressed = true;
            downKeyReleased = false;
            if(!menu.getSaveGameState()) {
                menuMove(mainMenuSize, pauseSize, gameOverSize, "Down");
            } 
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftKeyPressed = true;
            leftKeyReleased = false;
            if(menu.getSaveGameState()) {
                menuMove(mainMenuSize, pauseSize, gameOverSize, "Right");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightKeyPressed = true;
            rightKeyReleased = false;
            if(menu.getSaveGameState()) {
                menuMove(mainMenuSize, pauseSize, gameOverSize, "Left");
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            if (handler.player.size() > 0) {
                Game.Paused = !Game.Paused;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (handler.player.size() > 0) {
                firePos = handler.player.get(0).points();
                spaceKeyPressed = true;
            }            
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            activeMenuOption();
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            upKeyPressed = false;
            upKeyReleased = true;
            propulsionState(false);
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downKeyPressed = false;
            downKeyReleased = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftKeyPressed = false;
            leftKeyReleased = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightKeyPressed = false;
            rightKeyReleased = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            spaceKeyPressed = false;
        }
    }
    
    

    public void setSpeed(float topSpeed, float deltaSpeed) {
        this.topSpeed = topSpeed;
        this.deltaSpeed = deltaSpeed;
    }
    
    private void activeMenuOption() {
      //Main menu//
        //New Game
        if(!Game.GameOn && menu.mainMenuItem().get(0)) {
            Game.GameOn = !Game.GameOn;
        }
        //Quit game
        if (!Game.GameOn && menu.mainMenuItem().get(2)) {
            System.exit(1);
        }
        //Continue and game over//
        //Continue
        if (Game.GameOver && menu.gameOverItem().get(0)) {
            if (hud.getLife() > 0) {
                Game.Continued = !Game.Continued;
                velx = vely = 0;
            }
            if (hud.getLife() == 0) {
                Game.Reseted = !Game.Reseted;
                Game.GameOver = !Game.GameOver;
                velx = vely = 0;
            }
        }
        //Exit
        if (Game.GameOver && menu.gameOverItem().get(1)) {
            Game.GameOn = !Game.GameOn;
            Game.Exited = !Game.Exited;
            menu.gameOverItem().set(0, !menu.gameOverItem().get(0));
            menu.gameOverItem().set(1, !menu.gameOverItem().get(1));
            menuIndex = 0;
            velx = vely = 0;
        }
        //Pause//
        //Reset
        if (Game.Paused && menu.pauseItem().get(2)) {
            Game.Reseted = !Game.Reseted;
            menu.pauseItem().set(0, !menu.pauseItem().get(0));
            menu.pauseItem().set(2, !menu.pauseItem().get(2));
            menuIndex = 0;
            velx = vely = 0;
        }
        //Exit
        if (Game.Paused && menu.pauseItem().get(3)) {
            Game.GameOn = !Game.GameOn;
            Game.Exited = !Game.Exited;
            menu.pauseItem().set(0, !menu.pauseItem().get(0));
            menu.pauseItem().set(3, !menu.pauseItem().get(3));
            menuIndex = 0;
            velx = vely = 0;
        }
        //Save Game
        if(Game.Paused && menu.pauseItem().get(1)) {
            saveGameState = !saveGameState;
            menu.setSaveGameState(saveGameState);
            menu.pauseItem().set(0, !menu.pauseItem().get(0));
            menu.pauseItem().set(1, !menu.pauseItem().get(1));
            menuIndex = 0;
            velx = vely = 0;
        }
        //Save Game sub-menu
        if(menu.getSaveGameState() && menu.saveWindowItem().get(0)) {
            System.out.println("Saved");
            menuIndex = 0;
            velx = vely = 0;
        }
        if(menu.getSaveGameState() && menu.saveWindowItem().get(1)) {
            saveGameState = !saveGameState;
            menu.setSaveGameState(saveGameState);
            menu.saveWindowItem().set(0, !menu.saveWindowItem().get(0));
            menu.saveWindowItem().set(1, !menu.saveWindowItem().get(1));
            menuIndex = 0;
            velx = vely = 0;
        }
    }
    
    private void menuMove(int mMSize, int pSize, int gOSize, String dir) {
        // Main menu navigation
        if (!Game.GameOn) {
            menu.mainMenuItem().set(menuIndex, !menu.mainMenuItem().get(menuIndex));
            if (dir == "Up") {
                menuIndex--;
                if (menuIndex < 0)
                    menuIndex = mMSize - 1;
                menu.mainMenuItem().set(menuIndex, !menu.mainMenuItem().get(menuIndex));
            } else if (dir == "Down") {
                menuIndex++;
                if (menuIndex == mMSize)
                    menuIndex = 0;
                menu.mainMenuItem().set(menuIndex, !menu.mainMenuItem().get(menuIndex));
            }

        }
        // Pause menu navigation
        if (Game.Paused && Game.GameOn && !menu.getSaveGameState()) {
            menu.pauseItem().set(menuIndex, !menu.pauseItem().get(menuIndex));
            if (dir == "Up") {
                menuIndex--;
                if (menuIndex < 0)
                    menuIndex = pSize - 1;
                menu.pauseItem().set(menuIndex, !menu.pauseItem().get(menuIndex));
            } else if (dir == "Down") {
                menuIndex++;
                if (menuIndex == pSize)
                    menuIndex = 0;
                menu.pauseItem().set(menuIndex, !menu.pauseItem().get(menuIndex));

            }
        } 
        // Save options
        if(Game.Paused && Game.GameOn && menu.getSaveGameState()) {
            menu.saveWindowItem().set(menuIndex, !menu.saveWindowItem().get(menuIndex));
            if (dir == "Left") {
                menuIndex--;
                if (menuIndex < 0)
                    menuIndex = gOSize - 1;              
                menu.saveWindowItem().set(menuIndex, !menu.saveWindowItem().get(menuIndex));
  
            } else if (dir == "Right") {
                menuIndex++;
                if (menuIndex == gOSize)
                    menuIndex = 0;
                menu.saveWindowItem().set(menuIndex, !menu.saveWindowItem().get(menuIndex));

            }
            
        }  
        // GameOver menu navigation
        if (Game.GameOver && Game.GameOn) {
            menu.gameOverItem().set(menuIndex, !menu.gameOverItem().get(menuIndex));
            if (dir == "Up") {
                menuIndex--;
                if (menuIndex < 0)
                    menuIndex = gOSize - 1;
                menu.gameOverItem().set(menuIndex, !menu.gameOverItem().get(menuIndex));

            } else if (dir == "Down") {
                menuIndex++;
                if (menuIndex == gOSize)
                    menuIndex = 0;
                menu.gameOverItem().set(menuIndex, !menu.gameOverItem().get(menuIndex));
            }
        }
        // Propulsion animation
        if (!Game.Paused && dir == "Up") {
            propulsionState(true);
        }
    }
       
    private void propulsionState(boolean state) {
        for(int i = 0; i<handler.object.size();i++) {
            if(handler.object.get(i).getId() == ObjectId.Propulsion) {
                handler.object.get(i).setState(state);
            }
        }
    }

    private void move(String move) {
        if (handler.player.size() > 0) {
            switch (move) {
                case "up":
                    vely += deltaSpeed;
                    handler.player.get(0).setVelY(vely);
                    if (vely >= topSpeed) {
                        vely = (float) topSpeed;
                        handler.player.get(0).setVelY(vely);
                    }
                    break;
                case "down":
                    vely += -deltaSpeed * 2;
                    handler.player.get(0).setVelY(vely);
                    if (vely <= -topSpeed / 10) {
                        vely = (float) -topSpeed / 10;
                        handler.player.get(0).setVelY(vely);
                    }
                    break;
                case "left":
                    velx -= deltaSpeed / 50;
                    handler.player.get(0).setVelX(velx);
                    if (velx <= -topSpeed / 100) {
                        velx = (float) (-topSpeed / 100);
                        handler.player.get(0).setVelX(velx);
                    }
                    break;
                case "right":
                    velx += deltaSpeed / 50;
                    handler.player.get(0).setVelX(velx);
                    if (velx >= topSpeed / 100) {
                        velx = (float) (topSpeed / 100);
                        handler.player.get(0).setVelX(velx);
                    }
                    break;
                case "decelerateUp":
                    vely -= deltaSpeed / 5;
                    handler.player.get(0).setVelY(vely);
                    if (vely < 0) {
                        vely = 0;
                        handler.player.get(0).setVelY(vely);
                        upKeyReleased = false;
                    }
                    break;
                case "decelerateDown":
                    vely += deltaSpeed / 5;
                    handler.player.get(0).setVelY(vely);
                    if (vely > 0) {
                        vely = 0;
                        handler.player.get(0).setVelY(vely);
                        downKeyReleased = false;
                    }
                    break;
                case "decelerateLeft":
                    velx += deltaSpeed / 50;
                    handler.player.get(0).setVelX(velx);
                    if (velx > 0) {
                        velx = 0;
                        handler.player.get(0).setVelX(velx);
                        leftKeyReleased = false;
                    }
                    break;
                case "decelerateRight":
                    velx -= deltaSpeed / 50;
                    handler.player.get(0).setVelX(velx);
                    if (velx < 0) {
                        velx = 0;
                        handler.player.get(0).setVelX(velx);
                        rightKeyReleased = false;
                    }
                    break;
            }
        }
    }
}
