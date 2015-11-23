package com.mzinck.shapez;

public enum Sword {

    TEN(0, 1, 1, 1, 9, 0.09F),
    NINE(1, 1, 1, 2, 8, 0.08F),
    EIGHT(2, 1, 1, 5, 7, 0.075F),
    SEVEN(3, 1, 1, 10, 6, 0.07F),
    SIX(4, 1, 1, 20, 5, 0.065F),
    FIVE(5, 1, 1, 30, 4, 0.06F),
    FOUR(6, 1, 1, 50, 3, 0.055F),
    THREE(7, 1, 1, 65, 2, 0.05F),
    TWO(8, 1, 1, 60, 1, 0.045F),
    ONE(9, 1, 1, 100, 0, 0.04F);
    
    private int speed,
                height,
                width,
                rarity,
                index;
    
    private float scaleSize;

    private Sword(int speed, int height, int width, int rarity, int index, float scaleSize) {
        this.speed = speed;
        this.height = height;
        this.width = width;
        this.rarity = rarity;
        this.index = index;
        this.scaleSize = scaleSize;
    }
    
    public int getRarity() {
        return rarity;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public int getIndex() {
        return index;
    }
    
    public float getScaleSize() {
        return scaleSize;
    }
    
}
