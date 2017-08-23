package hr.faleksic.sphiggosmystery;

class Toxic extends GameObject {

    Toxic(int width, int height, int positionX, int positionY) {
        setBitmapName("toxic1");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }
}
