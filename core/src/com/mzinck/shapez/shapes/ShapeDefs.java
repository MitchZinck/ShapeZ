package com.mzinck.shapez.shapes;

public enum ShapeDefs {

    ZOMBIE(10, 1);

    private int hp = 0;
    private int speed = 0;

    private ShapeDefs(int hp, int speed) {
        this.hp = hp;
        this.speed = speed;
    }

    public int getHP() {
        return hp;
    }

    public int getSpeed() {
        return speed;
    }

}
