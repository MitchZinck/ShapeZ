package com.mzinck.shapez.shapes;

import com.mzinck.shapez.MainScreen;
import com.mzinck.shapez.Player;

public class Shape {

	private ShapeDefs	shape;
	private Player		player;

	private float	xPos;
	private float	yPos;
	private float	speed;
	private float	size;

	private int hp;

	private boolean isDead;

	public Shape(ShapeDefs shape, Player player, float xPos, float yPos, float speed, float size) {
		this.shape = shape;
		this.hp = shape.getHP();
		this.player = player;
		this.xPos = xPos;
		this.yPos = yPos;
		this.speed = speed;
		this.size = size;
	}

	public void update() {
		if(isDead == false) {
			double x = (player.getX() - xPos);
			double y = (player.getY() - yPos);
			double hypotenuse = Math.pow(Math.pow(x, 2) + Math.pow(y, 2), 0.5);
	
			float scaleValue = (float) (speed / hypotenuse);
	
			x = x * scaleValue;
			y = y * scaleValue * 1;
	
			xPos += x;
			yPos += y;
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

}
