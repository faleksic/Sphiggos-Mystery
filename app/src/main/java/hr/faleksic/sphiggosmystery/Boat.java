package hr.faleksic.sphiggosmystery;

public class Boat extends GameObject {

    private boolean moving = false;
    private boolean startingSide = true;
    private GameObject passenger;

    private int screenWidth;

    private double speed = 0.005;
    private float startX;
    private float finishX;

    Boat(int width, int height, int positionX, int positionY, int screenWidth) {
        this.screenWidth = screenWidth;

        startX = screenWidth / 2;
        finishX = screenWidth / 5;

        setBitmapName("boat");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setPassenger(GameObject passenger) {
        this.passenger = passenger;
    }

    @Override
    public void update() {
        if(this.startingSide) {
            moveToOtherSide();
        } else {
            moveToStartingSide();
        }
    }

    private void moveToOtherSide() {
        if(this.getPositionX() > finishX) {
            this.setPositionX(this.getPositionX() - (int)(screenWidth * speed));
            if(passenger != null) {
                passenger.setPositionX(passenger.getPositionX() - (int) (screenWidth * speed));
            }
        } else {
            this.startingSide = false;
            this.moving = false;
            passenger = null;
        }
    }

    private void moveToStartingSide() {
        if(this.getPositionX() < startX) {
            this.setPositionX(this.getPositionX() + (int)(screenWidth * speed));
            if(passenger != null) {
                passenger.setPositionX(passenger.getPositionX() + (int) (screenWidth * speed));
            }
        } else {
            this.startingSide = true;
            this.moving = false;
            passenger = null;
        }
    }

    public boolean isStartingSide() {
        return startingSide;
    }
}
