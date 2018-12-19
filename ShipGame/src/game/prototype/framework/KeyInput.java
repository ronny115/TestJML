package game.prototype.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Point2D;

import game.prototype.Handler;
import game.prototype.objects.Projectile;

public class KeyInput extends KeyAdapter {

    private PlayerObject tempPlayer;
    private Handler handler;
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
        // Pressed
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
        if (spaceKeyPressed == true) {
            handler.addObject(new Projectile(firePos[1].x, firePos[1].y, 5, 20, 20, "player",
                                            Helper.bulletAngle(handler.player.get(0).points()), 
                                            ObjectId.Projectile));

            spaceKeyPressed = false;
        }
    }

    public void keyPressed(KeyEvent e) {
        for (int i = 0; i < handler.player.size(); i++) {
            tempPlayer = handler.player.get(i);
            if (tempPlayer.getId() == PlayerId.PlayerShip) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    upKeyPressed = true;
                    upKeyReleased = false;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    downKeyPressed = true;
                    downKeyReleased = false;
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
                    System.exit(1);
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    firePos = tempPlayer.points();
                    spaceKeyPressed = true;
                }
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        for (int i = 0; i < handler.player.size(); i++) {
            tempPlayer = handler.player.get(i);
            if (tempPlayer.getId() == PlayerId.PlayerShip) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    upKeyPressed = false;
                    upKeyReleased = true;
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
        }
    }

    public void setSpeed(float topSpeed, float deltaSpeed) {
        this.topSpeed = topSpeed;
        this.deltaSpeed = deltaSpeed;
    }

    private void move(String move) {
        switch (move) {
            case "up":
                vely += deltaSpeed;
                tempPlayer.setVelY(vely);
                if (vely >= topSpeed) {
                    vely = (float) topSpeed;
                    tempPlayer.setVelY(vely);
                }
                break;
            case "down":
                vely += -deltaSpeed * 2;
                tempPlayer.setVelY(vely);
                if (vely <= -topSpeed / 10) {
                    vely = (float) -topSpeed / 10;
                    tempPlayer.setVelY(vely);
                }
                break;
            case "left":
                velx -= deltaSpeed / 50;
                tempPlayer.setVelX(velx);
                if (velx <= -topSpeed / 100) {
                    velx = (float) (-topSpeed / 100);
                    tempPlayer.setVelX(velx);
                }
                break;
            case "right":
                velx += deltaSpeed / 50;
                tempPlayer.setVelX(velx);
                if (velx >= topSpeed / 100) {
                    velx = (float) (topSpeed / 100);
                    tempPlayer.setVelX(velx);
                }
                break;
            case "decelerateUp":
                vely -= deltaSpeed / 5;
                tempPlayer.setVelY(vely);
                if (vely < 0) {
                    vely = 0;
                    tempPlayer.setVelY(vely);
                    upKeyReleased = false;
                }
                break;
            case "decelerateDown":
                vely += deltaSpeed / 5;
                tempPlayer.setVelY(vely);
                if (vely > 0) {
                    vely = 0;
                    tempPlayer.setVelY(vely);
                    downKeyReleased = false;
                }
                break;
            case "decelerateLeft":
                velx += deltaSpeed / 50;
                tempPlayer.setVelX(velx);
                if (velx > 0) {
                    velx = 0;
                    tempPlayer.setVelX(velx);
                    leftKeyReleased = false;
                }
                break;
            case "decelerateRight":
                velx -= deltaSpeed / 50;
                tempPlayer.setVelX(velx);
                if (velx < 0) {
                    velx = 0;
                    tempPlayer.setVelX(velx);
                    rightKeyReleased = false;
                }
                break;
        }
    }
}
