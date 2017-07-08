package hr.faleksic.sphiggosmystery;

public class Wolf extends GameObject {
    Wolf(int width, int height, int positionX, int positionY) {
        setBitmapName("wolf");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }
}
