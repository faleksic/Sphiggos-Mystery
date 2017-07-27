package hr.faleksic.sphiggosmystery;

public class Toxic extends GameObject {

    Toxic(int width, int height, int positionX, int positionY) {
        setBitmapName("toxic1");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }
}
