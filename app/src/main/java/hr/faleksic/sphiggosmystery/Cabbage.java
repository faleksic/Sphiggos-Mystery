package hr.faleksic.sphiggosmystery;

public class Cabbage extends GameObject {
    private boolean inBoat = false;
    private boolean moving = false;

    Cabbage(int width, int height, int positionX, int positionY) {
        setBitmapName("cabbage");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }

    public boolean isInBoat() {
        return inBoat;
    }

    public void setInBoat(boolean inBoat) {
        this.inBoat = inBoat;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}
