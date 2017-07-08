package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class LevelManager {
    private Context context;
    private int level;
    private Bitmap backgroundImg;
    private Bitmap textbox;
    private int screenWidth;
    private int screenHeight;
    private ArrayList<String> rulesText;
    private LevelData levelData;
    private ArrayList<GameObject> gameObjects;
    private Bitmap[] bitmaps;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    public  LevelManager(Context context, int level, int width, int height) {
        this.context = context;
        this.screenWidth = width;
        this.screenHeight = height;
        this.level = level;

        rulesText = new ArrayList<>();

        switch (level) {
            case 1: {
                levelData = new LevelOne(context, screenWidth, screenHeight);
                for (int i = 1; i < 6; i++) {
                    rulesText.add(getStringResourceByName("al_one_rule_" + String.valueOf(i)));
                }
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            }
        }

        prepareLevel();
    }

    private void prepareLevel() {
        backgroundImg = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.game_background), screenWidth, screenHeight, false);
        textbox = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.textbox), screenWidth, (int)(screenHeight*0.3), false);
        int i = 0;
        for(GameObject go : gameObjects){
            bitmaps[i] = go.prepareBitmap(context, go.getBitmapName());
            i++;
        }
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

    public Bitmap getTextbox() {
        return textbox;
    }

    public int getLevel() {
        return level;
    }

    public ArrayList<String> getRulesText() {
        return rulesText;
    }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Bitmap[] getBitmaps() {
        return bitmaps;
    }
}
