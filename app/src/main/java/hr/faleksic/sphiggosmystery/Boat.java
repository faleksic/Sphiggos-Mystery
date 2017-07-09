package hr.faleksic.sphiggosmystery;

public class Boat extends GameObject {
    Boat(int width, int height, int positionX, int positionY) {
        setBitmapName("boat");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }
}
