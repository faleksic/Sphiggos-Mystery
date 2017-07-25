package hr.faleksic.sphiggosmystery;

public class Wolf extends GameObject {

    private boolean inBoat = false;
    private boolean moving = false;
    private boolean otherSide = false;
    private boolean boatOnStartSide;

    private int screenWidth;
    private int screenHeight;

    private double speed = 0.005;
    private double startingPositionX;
    private double startingPositionY;
    private double boatPositionX;
    private double boatPositionY;
    private double boatOtherSideX;
    private double otherSideX;
    private double otherBoatPositionX;


    Wolf(int width, int height, int positionX, int positionY, int screenWidth, int screenHeight) {
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        startingPositionX = screenWidth / 1.25;
        startingPositionY =  screenHeight * 0.3;
        boatPositionX = screenWidth / 1.7;
        boatPositionY = screenHeight * 0.26;
        boatOtherSideX = screenWidth / 2;
        otherSideX = screenWidth * 0.02;
        otherBoatPositionX = screenWidth / 3.5;

        setBitmapName("wolf");
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

    public void setBoatOnStartSide(boolean boatOnStartSide) {
        this.boatOnStartSide = boatOnStartSide;
    }

    public boolean isOtherSide() {
        return otherSide;
    }

    @Override
    public void update() {

        if(this.isInBoat()) {
            //if wolf is not on the other side
            if(this.getPositionX() > boatOtherSideX) {
                moveToStart();
            } else {
                moveToOtherSide();
            }
        } else {
            //if wolf is not on the other side
            if(!this.otherSide) {
                if(boatOnStartSide) {
                    moveToBoat();
                }
            } else {
                if(!boatOnStartSide) {
                    moveToBoatOtherSide();
                }
            }
        }
    }

    private void moveToStart() {
        if (this.getPositionX() < startingPositionX) {
            this.setPositionX(this.getPositionX() + (int) (screenWidth * speed));
        }
        if (this.getPositionY() > startingPositionY) {
            this.setPositionY(this.getPositionY() - (int) (screenHeight * speed));
        }
        if (this.getPositionX() >= startingPositionX && this.getPositionY() <= startingPositionY) {
            this.setInBoat(false);
            this.setMoving(false);
        }
    }

    private void moveToOtherSide() {
        if (this.getPositionX() > otherSideX) {
            this.setPositionX(this.getPositionX() - (int) (screenWidth * speed));
        }
        if (this.getPositionY() > startingPositionY) {
            this.setPositionY(this.getPositionY() - (int) (screenHeight * speed));
        }
        if (this.getPositionX() <= otherSideX && this.getPositionY() <= startingPositionY) {
            this.setInBoat(false);
            this.setMoving(false);
            this.otherSide = true;
        }
    }

    private void moveToBoat() {
        if (this.getPositionX() > boatPositionX) {
            this.setPositionX(this.getPositionX() - (int) (screenWidth * speed));
        }
        if (this.getPositionY() < boatPositionY) {
            this.setPositionY(this.getPositionY() + (int) (screenHeight * speed));
        }
        if (this.getPositionX() <= boatPositionX && this.getPositionY() >= boatPositionY) {
            this.setInBoat(true);
            this.setMoving(false);
        }
    }

    private void moveToBoatOtherSide() {
        if (this.getPositionX() < otherBoatPositionX) {
            this.setPositionX(this.getPositionX() + (int) (screenWidth * speed));
        }
        if (this.getPositionY() < boatPositionY) {
            this.setPositionY(this.getPositionY() + (int) (screenHeight * speed));
        }
        if (this.getPositionX() >= otherBoatPositionX && this.getPositionY() >= boatPositionY) {
            this.setInBoat(true);
            this.setMoving(false);
            this.otherSide = false;
        }
    }
}
