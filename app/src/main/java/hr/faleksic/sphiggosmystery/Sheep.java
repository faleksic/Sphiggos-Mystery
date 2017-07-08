package hr.faleksic.sphiggosmystery;

public class Sheep extends GameObject {
    Sheep(int width, int height, int positionX, int positionY) {
        setBitmapName("sheep");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }
}
