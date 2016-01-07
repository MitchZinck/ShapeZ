package com.mzinck.shapez;

public enum Sword {

    TEN(3, 1, 9, 0.13F),
    NINE(3, 2, 8, 0.12F),
    EIGHT(4, 5, 7, 0.115F),
    SEVEN(4, 10, 6, 0.11F),
    SIX(4, 15, 5, 0.105F),
    FIVE(5, 20, 4, 0.1F),
    FOUR(6, 25, 3, 0.095F),
    THREE(7, 30, 2, 0.09F),
    TWO(8, 50, 1, 0.085F),
    ONE(9, 1000, 0, 0.08F);
    
    private int speed,
                rarity,
                index;
    
    private float scaleSize;

    private Sword(int speed, int rarity, int index, float scaleSize) {
        this.speed = speed;
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
