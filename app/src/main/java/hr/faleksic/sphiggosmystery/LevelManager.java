package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;

public class LevelManager {
    private Context context;
    private int level;
    private int screenWidth;
    private int screenHeight;
    private ArrayList<String> rulesText;
    private LevelData levelData;
    private ArrayList<GameObject> gameObjects;
    private Bitmap[] bitmaps;

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
