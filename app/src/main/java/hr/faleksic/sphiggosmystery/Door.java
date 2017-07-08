package hr.faleksic.sphiggosmystery;

public class Door extends GameObject {
    Door(int width, int height, int positionX, int positionY) {
        setBitmapName("door_closed");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
    }

    @Override
    public void setBitmapName(String bitmapName) {
        super.setBitmapName(bitmapName);
    }
}
