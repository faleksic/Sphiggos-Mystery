package hr.faleksic.sphiggosmystery;

class GameOnTable extends GameObject {

    GameOnTable(String name, int width, int height, int positionX, int positionY) {
        setBitmapName(name);
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setRemoveBlur(true);
    }
}
