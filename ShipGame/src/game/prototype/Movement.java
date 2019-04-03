package game.prototype;

public class Movement {
    
    private Handler handler;
    private Menu menu;
    private float topSpeed = 0, deltaSpeed = 0, vely = 0, velx = 0;
    private boolean upKey, downKey, leftKey, rightKey;
  
    public Movement(Handler h, Menu m) {
        this.handler = h;
        this.menu = m;
    }
    
    public void update() {
        // Movement
        if (upKey) {
            move("up");
            if (upKey && leftKey) {
                move("up");
                move("left");
            } else if (upKey && rightKey) {
                move("up");
                move("right");
            } else if (!leftKey && velx < 0) {
                move("decelerateLeft");
            } else if (!rightKey && velx > 0) {
                move("decelerateRight");
            }
        } else if (!upKey && vely > 0) 
            move("decelerateUp");
        
        if (downKey) 
            move("down");
        else if (!downKey && vely < 0) 
            move("decelerateDown");
        
        if (leftKey) 
            move("left");
        else if (!leftKey && velx < 0)
            move("decelerateLeft");
        
        if (rightKey) 
            move("right");
        else if (!rightKey && velx > 0)
            move("decelerateRight");
        
        if(menu.getRsetV() && handler.player.size() > 0) {
            handler.player.get(0).setVelY(0);
            handler.player.get(0).setVelX(0);
            vely = velx = 0;
            menu.setRsetV(false);          
        }

    }
    
    public void setSpeed(float topSpeed, float deltaSpeed) {
        this.topSpeed = topSpeed;
        this.deltaSpeed = deltaSpeed;
    }
    
    private void move(String direction) {
        if (handler.player.size() > 0) {
            switch (direction) {
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
                    if(vely >= 0) {
                    vely -= deltaSpeed / 5;
                    handler.player.get(0).setVelY(vely);}
                    if (vely < 0) {
                        vely = 0;
                        handler.player.get(0).setVelY(vely);
                    }
                    break;
                case "decelerateDown":
                    if(vely <= 0) {
                    vely += deltaSpeed / 5;
                    handler.player.get(0).setVelY(vely);}
                    if (vely > 0) {
                        vely = 0;
                        handler.player.get(0).setVelY(vely);
                    }
                    break;
                case "decelerateLeft":
                    velx += deltaSpeed / 50;
                    handler.player.get(0).setVelX(velx);
                    if (velx > 0) {
                        velx = 0;
                        handler.player.get(0).setVelX(velx);
                    }
                    break;
                case "decelerateRight":
                    velx -= deltaSpeed / 50;
                    handler.player.get(0).setVelX(velx);
                    if (velx < 0) {
                        velx = 0;
                        handler.player.get(0).setVelX(velx);
                    }
                    break;
            }
        }
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
}
