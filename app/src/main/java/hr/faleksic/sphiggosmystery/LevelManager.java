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
    private Bitmap opendDoor;
    private Bitmap enemy;
    private Bitmap gameOnTable;
    private Bitmap textbox;
    private int screenWidth;
    private int screenHeight;
    private boolean rules = false;
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
                options.inScaled = false;
                enemy = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                        (context.getResources(),R.drawable.al, options), (int)(screenWidth*0.1), (int)(screenHeight*0.35), false);
                gameOnTable = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                        (context.getResources(),R.drawable.game1, options), (int)(screenWidth*0.1), (int)(screenHeight*0.15), false);
                for(int i=1; i<6; i++) {
                    rulesText.add(getStringResourceByName("al_one_rule_" + String.valueOf(i)));
                }
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
        textbox = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.textbox), screenWidth, (int)(screenHeight*0.3), false);
    }

    private String getStringResourceByName(String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
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

    public boolean isRules() {
        return rules;
    }

    public void setRules(boolean rules) {
        this.rules = rules;
    }
}
