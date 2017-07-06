package hr.faleksic.sphiggosmystery;

import android.content.Context;

public class Enemy extends GameObject {

    Enemy(String name, int width, int height, int positionX, int positionY) {
        setBitmapName(name);
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
    }
}
