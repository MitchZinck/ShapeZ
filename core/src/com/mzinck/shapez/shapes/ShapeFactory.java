package com.mzinck.shapez.shapes;

import com.mzinck.shapez.Player;

public interface ShapeFactory <S extends Shape>{
    S create(ShapeDefs shape, Player player, float xPos, float yPos, float speed, float size);
}
