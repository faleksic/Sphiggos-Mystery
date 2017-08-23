package hr.faleksic.sphiggosmystery;

class GameOver extends GameObject {
    GameOver(int width, int height, int positionX, int positionY) {
        setBitmapName("game_over");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }
}
