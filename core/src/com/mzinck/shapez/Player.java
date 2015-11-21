package com.mzinck.shapez;

import com.badlogic.gdx.math.Rectangle;

public class Player {

    private int              speed;
    private int              hp           = 10;
    private int              points       = 100000;
    private float            x            = 0;
    private float            y            = 0;
    private float            cameraSize   = 300F;
    private float            size;
    private Sword            sword;
    private Rectangle        rect;

    private static final int MAX_VELOCITY = 1;

    public Player(int speed, float size, Sword sword) {
        rect = new Rectangle(x, y, size, size);
        this.speed = speed;
        this.size = size;
        this.sword = sword;
    }

    public void update() {
        rect.x = x;
        rect.y = y;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
    
    public Sword getSword() {
        return sword;
    }
    
    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public void gotHit() {
        hp--;
        points = points > 10 ? points - 10 : 0;
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
