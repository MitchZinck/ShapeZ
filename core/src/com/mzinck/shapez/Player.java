package com.mzinck.shapez;

import com.badlogic.gdx.math.Rectangle;

public class Player {

    private int    speed;
    private int    hp = 10;
    private float x = 0;
    private float y = 0;
    private float  cameraSize = 300F;
    private float  size;
    private Rectangle rect;

    private static final int MAX_VELOCITY = 1;

    public Player(int speed, float size) {
        rect = new Rectangle(x, y, size, size);
        this.speed = speed;
        this.size = size;
    }

    public void update() {
        rect.x = x;
        rect.y = y;
    }
    
    public Rectangle getRectangle() {
        return rect;
    }
    
    public void gotHit() {
        hp--;
    }

    public float getSize() {
        return size;
    }
    
    public int getHP() {
        return hp;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getCameraSize() {
        return cameraSize;
    }

    public void setCameraSize(float cameraSize) {
        this.cameraSize = cameraSize;
    }

    public double getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
