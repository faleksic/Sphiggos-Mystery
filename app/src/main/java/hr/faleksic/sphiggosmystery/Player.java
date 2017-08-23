package hr.faleksic.sphiggosmystery;

class Player extends  GameObject {

    Player(int positionX, int positionY) {
        setBitmapName("player");
        setHeight(180);
        setWidth(350);
        setPositionX(positionX);
        setPositionY(positionY);
        setRemoveBlur(true);
    }
}
