package com.mzinck.shapez.shapes;

import com.badlogic.gdx.math.Rectangle;
import com.mzinck.shapez.Player;

public class Shape {

    private ShapeDefs shape;
    private Player    player;
    private float xPos;
    private float yPos;
    private float speed;
    private float size;
    private int hp;
    private int lastAttack = 0;
    private boolean isDead;
    private Rectangle rect;

    public Shape(ShapeDefs shape, Player player, float xPos, float yPos, float speed, float size) {
        this.shape = shape;
        this.hp = shape.getHP();
        this.player = player;
        this.xPos = xPos;
        this.yPos = yPos;
        this.speed = speed;
        this.size = size;
        rect = new Rectangle(0, 0, size, size);
    }

    public void update() {
        if (isDead == false) {
            rect.x = xPos;
            rect.y = yPos;
            if(rect.overlaps(player.getRectangle()) && lastAttack == 0) {
                player.gotHit();
                lastAttack = 60;
            }
            double x = (player.getX() - xPos);
            double y = (player.getY() - yPos);
            double hypotenuse = Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);

            float scaleValue = (float) (speed / hypotenuse);

            x = x * scaleValue;
            y = y * scaleValue * 1;

            xPos += x;
            yPos += y;
            lastAttack = lastAttack > 0 ? lastAttack - 1 : 0;
        }
    }
   
    public void setDead() {
        isDead = true;
    }

    public float getSize() {
        return size;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public float getxPos() {
        return xPos;
    }

    public float getyPos() {
        return yPos;
    }
    
    public boolean isDead() {
        return isDead;
    }

}
