package hr.faleksic.sphiggosmystery;

class Background extends GameObject {

    Background(String name, int width, int height, int positionX, int positionY) {
        setBitmapName(name);
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
    }
}
