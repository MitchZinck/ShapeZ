package com.mzinck.shapez;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.mzinck.shapez.power.Power;

public class Player {

    private int                speed;
    private int                hp         = 10;
    private int                points     = 0;
    private float              x          = 0;
    private float              y          = 0;
    private float              cameraSize = 300F;
    private float              size;
    private Sword              sword;
    private Rectangle          rect;
    private Map<Power, Sprite> powers;
    private Sprite             sprite;

    public Player(int speed, float size, Sword sword, Sprite sprite) {
        rect = new Rectangle(x, y, size, size);
        this.speed = speed;
        this.size = size;
        this.sword = sword;
        this.sprite = sprite;
        powers = new LinkedHashMap<Power, Sprite>();
    }
    
    public void setHP(int hp) {
        this.hp = hp;
    }
    
    public Sprite getSprite() {
        return sprite;
    }
    
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }
        
    public void addPower(Power power, Sprite sprite) {
        powers.put(power, sprite);
    }
    
    public Map<Power, Sprite> getPowers() {
        return powers;
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
