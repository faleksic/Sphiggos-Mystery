package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;

public class Player extends  GameObject {
    private Bitmap bitmap;

    Player(Context context,int positionX, int positionY) {
        setBitmapName("player");
        setHeight(180);
        setWidth(350);
        bitmap = prepareBitmap(context, "player");
        setPositionX(positionX);
        setPositionY(positionY);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    @Override
    public void update(long fps, float gravity) {

    }
}
