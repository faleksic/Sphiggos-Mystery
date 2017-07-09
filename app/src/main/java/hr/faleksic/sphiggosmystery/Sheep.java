package hr.faleksic.sphiggosmystery;

public class Sheep extends GameObject {
    private boolean inBoat = false;

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
}
