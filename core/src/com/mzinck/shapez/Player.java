package com.mzinck.shapez;

public class Player {

    private int    speed;
    private double x;
    private double y;
    private float  cameraSize = 300F;
    private float  size;

    private static final int MAX_VELOCITY = 1;

    public Player(int speed, float size) {
        this.speed = speed;
        this.size = size;
    }

    private void update() {

    }

    public float getSize() {
        return size;
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

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

}
