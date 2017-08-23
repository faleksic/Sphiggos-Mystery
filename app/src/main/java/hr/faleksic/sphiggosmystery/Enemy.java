package hr.faleksic.sphiggosmystery;

class Enemy extends GameObject {

    Enemy(String name, int width, int height, int positionX, int positionY) {
        setBitmapName(name);
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setRemoveBlur(true);
    }
}
