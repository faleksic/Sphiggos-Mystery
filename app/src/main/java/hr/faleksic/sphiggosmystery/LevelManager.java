package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class LevelManager {
    private Context context;
    private int level;
    private Bitmap backgroundImg;
    private Bitmap closedDoor;
    private Enemy enemy;
    private Bitmap gameOnTable;
    private Bitmap textbox;
    private int screenWidth;
    private int screenHeight;
    private ArrayList<String> rulesText;
    private Player player;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    public  LevelManager(Context context, int level, int width, int height) {
        this.context = context;
        this.screenWidth = width;
        this.screenHeight = height;
        this.level = level;

        rulesText = new ArrayList<>();

        prepareLevel();

        switch (level) {
            case 1:
                new LevelOne(context, this, screenWidth, screenHeight);
        }
    }

    private void prepareLevel() {
        backgroundImg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.game_background), screenWidth, screenHeight, false);
        closedDoor = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.door_closed), (int)(screenWidth*0.2), (int)(screenHeight*0.405), false);
        player = new Player(context, (int)(screenWidth/5), (int)(screenHeight/2.5));
        textbox = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.textbox), screenWidth, (int)(screenHeight*0.3), false);
    }

    public String getStringResourceByName(String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }


    public Bitmap getBackgroundImg() {
        return backgroundImg;
    }

    public void setBackgroundImg(String backgroundImg) {
        int resID = context.getResources().getIdentifier(backgroundImg,"drawable", context.getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),resID);
        bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, false);
        this.backgroundImg = bitmap;
    }

    public Bitmap getClosedDoor() {
        return closedDoor;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Bitmap getGameOnTable() {
        return gameOnTable;
    }

    public Bitmap getTextbox() {
        return textbox;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<String> getRulesText() {
        return rulesText;
    }

    public void setRulesText(ArrayList<String> rulesText) {
        this.rulesText = rulesText;
    }

    public void setGameOnTable(Bitmap gameOnTable) {
        this.gameOnTable = gameOnTable;
    }
}
