package game.prototype.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import game.prototype.Game;
import game.prototype.HUD;
import game.prototype.Handler;
import game.prototype.Menu;
import game.prototype.objects.Projectile;
import game.prototype.objects.PropulsionFX;

public class KeyInput extends KeyAdapter {

    private Handler handler;
    private int menuIndex = 0;
    private float topSpeed = 0, deltaSpeed = 0, vely = 0, velx = 0;
    // Movement
    private boolean upKeyPressed, downKeyPressed, leftKeyPressed, rightKeyPressed;
    private boolean upKeyReleased, downKeyReleased, leftKeyReleased, rightKeyReleased;
    // Shooting
    private boolean spaceKeyPressed;
    private Point2D.Float[] firePos = new Point2D.Float[5];

    public KeyInput(Handler handler) {
        this.handler = handler;
    }

    public void updateInput() {   
        // Movement
        if (upKeyPressed == true) {
            move("up");
            if (upKeyPressed == true && leftKeyPressed == true) {
                move("up");
                move("left");
            } else if (upKeyPressed == true && rightKeyPressed == true) {
                move("up");
                move("right");
            } else if (leftKeyReleased == true) {
                move("decelerateLeft");
            } else if (rightKeyReleased == true) {
                move("decelerateRight");
            }
        }
        if (downKeyPressed == true) {
            move("down");
        }
        if (leftKeyPressed == true) {
            move("left");
        }
        if (rightKeyPressed == true) {
            move("right");
        }
        // Released
        if (upKeyReleased == true) {
            move("decelerateUp");
            if (HUD.HEALTH == 0) velx = vely = 0;
        }
        if (downKeyReleased == true) {
            move("decelerateDown");
        }
        if (leftKeyReleased == true && leftKeyPressed == false && rightKeyPressed == false) {
            move("decelerateLeft");
        }
        if (rightKeyReleased == true && rightKeyPressed == false && leftKeyPressed == false) {
            move("decelerateRight");
        }
        // Shooting
        if (spaceKeyPressed == true && handler.player.size() > 0 && !handler.player.get(0).state()) {
            handler.addObject(new Projectile(firePos[1].x, firePos[1].y, 5, 20, 20, "player",
                                            Helper.getAngle(handler.player.get(0).points()), 
                                            ObjectId.Projectile));
            spaceKeyPressed = false;
        }
    }
    
    public void keyPressed(KeyEvent e) {  
        int size = Menu.PauseItem.size();
        int size1 = Menu.GameOverItem.size();
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            upKeyPressed = true;
            upKeyReleased = false;
            if (!Game.Paused) PropulsionFX.isPropulsed = true;
            else {
                //Pause menu navigation
                Menu.PauseItem.set(menuIndex, 
                                  !Menu.PauseItem.get(menuIndex));
                menuIndex--;
                if (menuIndex < 0) menuIndex = size - 1;
                Menu.PauseItem.set(menuIndex, 
                                  !Menu.PauseItem.get(menuIndex));
            }
            if (Game.GameOver) {
                //GameOver menu navigation
                Menu.GameOverItem.set(menuIndex, 
                                     !Menu.GameOverItem.get(menuIndex));
                menuIndex--;
                if (menuIndex < 0) menuIndex = size1 - 1;
                Menu.GameOverItem.set(menuIndex, 
                                     !Menu.GameOverItem.get(menuIndex));
            }
            
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            downKeyPressed = true;
            downKeyReleased = false;          
            if (Game.Paused) {
                //Pause menu navigation
                Menu.PauseItem.set(menuIndex, 
                                  !Menu.PauseItem.get(menuIndex));
                menuIndex++;
                if (menuIndex == size) menuIndex = 0;            
                Menu.PauseItem.set(menuIndex, 
                                  !Menu.PauseItem.get(menuIndex));
            }
            if (Game.GameOver) {
                //GameOver menu navigation
                Menu.GameOverItem.set(menuIndex, 
                                     !Menu.GameOverItem.get(menuIndex));
                menuIndex++;
                if (menuIndex == size1) menuIndex = 0;
                Menu.GameOverItem.set(menuIndex, 
                                     !Menu.GameOverItem.get(menuIndex));
                }  
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            leftKeyPressed = true;
            leftKeyReleased = false;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightKeyPressed = true;
            rightKeyReleased = false;
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
            if (Menu.PauseItem.get(1) || Menu.GameOverItem.get(0)) {
                Game.Reseted = !Game.Reseted;
                velx = vely = 0;
            }
            if (Menu.PauseItem.get(2) || Menu.GameOverItem.get(1)) {
                System.exit(1);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            upKeyPressed = false;
            upKeyReleased = true;
            PropulsionFX.isPropulsed = false;
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
