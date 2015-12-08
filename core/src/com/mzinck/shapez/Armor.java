package com.mzinck.shapez;

public enum Armor {
    TEN(9, 1, 9),
    NINE(8, 2, 8),
    EIGHT(7, 5, 7),
    SEVEN(6, 10, 6),
    SIX(5, 20, 5),
    FIVE(4, 30, 4),
    FOUR(3, 90, 3),
    THREE(2, 100, 2),
    TWO(1, 200, 1),
    ONE(0, 1000, 0);
    
    private int hp,
                rarity,
                index;
    
    private Armor(int hp, int rarity, int index) {
        this.hp = hp;
        this.rarity = rarity;
        this.index = index;
    }
    
    public int getHP() {
        return hp;
    }
    
    public int getRarity() {
        return rarity;
    }
    
    public int getIndex() {
        return index;
    }
}
