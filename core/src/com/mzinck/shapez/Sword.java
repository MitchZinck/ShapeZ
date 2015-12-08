package com.mzinck.shapez;

public enum Sword {

    TEN(3, 1, 9, 0.21F),
    NINE(3, 2, 8, 0.2F),
    EIGHT(4, 5, 7, 0.195F),
    SEVEN(4, 10, 6, 0.19F),
    SIX(4, 20, 5, 0.185F),
    FIVE(5, 50, 4, 0.18F),
    FOUR(6, 100, 3, 0.175F),
    THREE(7, 150, 2, 0.17F),
    TWO(8, 200, 1, 0.15F),
    ONE(9, 1000, 0, 0.1F);
    
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
