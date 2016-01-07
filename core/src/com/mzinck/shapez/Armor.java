package com.mzinck.shapez;

public enum Armor {
    FIVE(10, 1, 4),
    FOUR(9, 10, 3),
    THREE(7, 20, 2),
    TWO(5, 30, 1),
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
