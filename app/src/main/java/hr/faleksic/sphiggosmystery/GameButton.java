package hr.faleksic.sphiggosmystery;

public class GameButton extends GameObject {
    GameButton(String bitmapName, int width, int height, int positionX, int positionY) {
        setBitmapName(bitmapName);
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }
}
