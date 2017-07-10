package hr.faleksic.sphiggosmystery;

public class Sheep extends GameObject {
    private boolean inBoat = false;
    private boolean moving = false;

    Sheep(int width, int height, int positionX, int positionY) {
        setBitmapName("sheep");
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
