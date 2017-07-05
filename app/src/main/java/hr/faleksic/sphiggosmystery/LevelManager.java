package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class LevelManager {
    private Context context;
    private Bitmap backgroundImg;
    private Bitmap closedDoor;
    private Bitmap opendDoor;
    private Bitmap enemy;
    private int screenWidth;
    private int screenHeight;
    private Player player;
    BitmapFactory.Options options = new BitmapFactory.Options();

    public  LevelManager(Context context, int level, int width, int height) {
        this.context = context;
        this.screenWidth = width;
        this.screenHeight = height;

        prepareLevel();

        switch (level) {
            case 1:
                options.inScaled = false;
                enemy = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                        (context.getResources(),R.drawable.al, options), (int)(screenWidth*0.1), (int)(screenHeight*0.35), false);
        }
    }

    private void prepareLevel() {
        backgroundImg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.game_background), screenWidth, screenHeight, false);
        opendDoor = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.door_opened), (int)(screenWidth*0.2), (int)(screenHeight*0.405), false);
        closedDoor = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.door_closed), (int)(screenWidth*0.2), (int)(screenHeight*0.405), false);
        player = new Player(context, (int)(screenWidth/5), (int)(screenHeight/2.5));
    }


    public Bitmap getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(Bitmap backgroundImg) {
        this.backgroundImg = backgroundImg;
    }

    public Bitmap getClosedDoor() {
        return closedDoor;
    }

    public Player getPlayer() {
        return player;
    }

    public Bitmap getEnemy() {
        return enemy;
    }
}
