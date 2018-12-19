package game.prototype;

import game.prototype.framework.PlayerObject;

public class Camera {
    private float x, y;

    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(PlayerObject player) {
        // Movement easing (interpolation)
        x += ((player.getX() - x) - Game.WIDTH / 2) * 0.05f;
        y += ((player.getY() - y) - Game.HEIGHT / 2) * 0.05f;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
