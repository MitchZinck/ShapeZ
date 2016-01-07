package com.mzinck.shapez;

import java.util.LinkedHashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.mzinck.shapez.power.Power;

public class Player {

    private int                speed;
    private int                hp         = 10;
    private int                points     = 10000;
    private int 			   armorHP    = 0;
    private float              x          = 0;
    private float              y          = 0;
    private float              cameraSize = 300F;
    private float              size;
    private Sword              sword;
    private Armor			   armor;
    private Rectangle          rect;
    private Map<Power, Sprite> powers;

    public Player(int speed, float size, Sword sword, Armor armor) {
        rect = new Rectangle(x, y, size, size);
        this.speed = speed;
        this.size = size;
        this.sword = sword;
        this.armor = armor;
        powers = new LinkedHashMap<Power, Sprite>();
    }
    
    public void setHP(int hp) {
        this.hp = hp;
    }
    
    public int getArmorHP() {
    	return armorHP;
    }
    
    public void setArmorHP(int hp) {
    	armorHP = hp;
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
    
    public Armor getArmor() {
    	return armor;
    }
    
    public void setArmor(Armor armor) {
    	this.armor = armor;
    }

    public Rectangle getRectangle() {
        return rect;
    }

    public void gotHit() {
    	if(armorHP > 0) {
    		armorHP--;
    	} else {
    		hp--;
    		points = points > 100 ? points - 300 : 0;
    	}
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
