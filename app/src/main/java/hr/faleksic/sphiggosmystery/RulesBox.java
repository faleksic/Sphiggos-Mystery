package hr.faleksic.sphiggosmystery;

public class RulesBox extends GameObject {

    RulesBox(int width, int height, int positionX, int positionY) {
        setBitmapName("textbox");
        setPositionX(positionX);
        setPositionY(positionY);
        setHeight(height);
        setWidth(width);
        setVisible(false);
    }
}
