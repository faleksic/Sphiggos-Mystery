package hr.faleksic.sphiggosmystery;

public class Boat extends GameObject {

    private boolean moving = false;
    private boolean startingSide = true;

    Boat(int width, int height, int positionX, int positionY) {
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

    @Override
    public void update(int screenWidth, int screenHeight) {
        if(this.startingSide) {
            if(this.getPositionX() > screenWidth/5) {
                this.setPositionX(this.getPositionX() - (int)(screenWidth*0.005));
            } else {
                this.startingSide = false;
                this.moving = false;
            }
        } else {
            if(this.getPositionX() < screenWidth/2) {
                this.setPositionX(this.getPositionX() + (int)(screenWidth*0.005));
            } else {
                this.startingSide = true;
                this.moving = false;
            }
        }
    }
}
