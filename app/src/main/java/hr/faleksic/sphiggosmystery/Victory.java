package hr.faleksic.sphiggosmystery;

public class Victory extends GameObject {
    Victory(int width, int height, int positionX, int positionY) {
        setBitmapName("victory");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
    }
}
