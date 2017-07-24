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

    @Override
    public void update(int screenWidth, int screenHeight) {
        if(this.isInBoat()) {
            if(this.getPositionX() < screenWidth/1.3) {
                this.setPositionX(this.getPositionX() + (int)(screenWidth*0.005));
            }
            if(this.getPositionY() > screenHeight*0.1) {
                this.setPositionY(this.getPositionY() - (int)(screenHeight*0.005));
            }
            if(this.getPositionX() >= screenWidth/1.3 && this.getPositionY() <= screenHeight*0.1) {
                this.setInBoat(false);
                this.setMoving(false);
            }

        } else {
            if(this.getPositionX() > screenWidth/1.7) {
                this.setPositionX(this.getPositionX() - (int)(screenWidth*0.005));
            }
            if(this.getPositionY() < screenHeight*0.26) {
                this.setPositionY(this.getPositionY() + (int)(screenHeight*0.005));
            }
            if(this.getPositionX() <= screenWidth/1.7 && this.getPositionY() >= screenHeight*0.26) {
                this.setInBoat(true);
                this.setMoving(false);
            }
        }
    }
}
