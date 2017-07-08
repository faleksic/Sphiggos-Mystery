package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public abstract class GameObject {
    private float width;
    private float height;
    private int positionX;
    private int positionY;
    private String bitmapName;
    private boolean visible = true;
    private boolean removeBlur = false;

    public String getBitmapName() {
        return bitmapName;
    }


    public Bitmap prepareBitmap(Context context, String bitmapName) {
        int resID = context.getResources().getIdentifier(bitmapName,"drawable", context.getPackageName());
        Bitmap bitmap;
        if(removeBlur){
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            bitmap = BitmapFactory.decodeResource(context.getResources(),resID, options);
        } else {
            bitmap = BitmapFactory.decodeResource(context.getResources(),resID);
        }
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (width), (int) (height), false);
        return bitmap;
    }

    public void setBitmapName(String bitmapName){
        this.bitmapName = bitmapName;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getPositionY() {
        return positionY;
    }

    public boolean isVisiable() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setRemoveBlur(boolean removeBlur) {
        this.removeBlur = removeBlur;
    }
}
