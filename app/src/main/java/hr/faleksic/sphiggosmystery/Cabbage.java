package hr.faleksic.sphiggosmystery;

public class Cabbage extends GameObject {
    Cabbage(int width, int height, int positionX, int positionY) {
        setBitmapName("cabbage");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
    }
}
