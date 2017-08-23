package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

class LevelManager {
    private Context context;
    private int level;
    private ArrayList<String> rulesText;
    private LinkedHashMap<String, GameObject> gameObjects;
    private Bitmap[] bitmaps;

    LevelManager(Context context, int level, int width, int height) {
        this.context = context;
        this.level = level;

        rulesText = new ArrayList<>();

        switch (level) {
            case 1:
                LevelData levelData;
            {
                levelData = new LevelOne(context, width, height);
                for (int i = 1; i < 7; i++) {
                    rulesText.add(getStringResourceByName("al_one_rule_" + String.valueOf(i)));
                }
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            } case 2: {
                levelData = new LevelTwo(context, width, height);
                for (int i = 1; i < 5; i++) {
                    rulesText.add(getStringResourceByName("matt_one_rule_" + String.valueOf(i)));
                }
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            } case 3: {
                levelData = new LevelThree(context, width, height);
                for (int i = 1; i < 3; i++) {
                    rulesText.add(getStringResourceByName("chris_one_rule_" + String.valueOf(i)));
                }
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            } case 4: {
                levelData = new LevelFive(context, width, height);
                for (int i = 1; i < 4; i++) {
                    rulesText.add(getStringResourceByName("matt_two_rule_" + String.valueOf(i)));
                }
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            } case 5: {
                levelData = new LevelSix(context, width, height);
                rulesText.add(getStringResourceByName("chris_two_rule"));
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            } case 6: {
                levelData = new LevelEight(context, width, height);
                for (int i = 1; i < 4; i++) {
                    rulesText.add(getStringResourceByName("matt_three_rule_" + String.valueOf(i)));
                }
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            } case 7: {
                levelData = new LevelNine(context, width, height);
                rulesText.add(getStringResourceByName("chris_two_rule"));
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            } case 8: {
                levelData = new GameFinish(context, width, height);
                gameObjects = levelData.data;
                bitmaps = new Bitmap[gameObjects.size()];
                break;
            }
        }

        prepareLevel();
    }

    private void prepareLevel() {
        int i = 0;
        for(Map.Entry<String, GameObject> go : gameObjects.entrySet()) {
            bitmaps[i] = go.getValue().prepareBitmap(context, go.getValue().getBitmapName());
            i++;
        }
    }

    private String getStringResourceByName(String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString, "string", packageName);
        return context.getString(resId);
    }

    int getLevel() {
        return level;
    }

    ArrayList<String> getRulesText() {
        return rulesText;
    }

    LinkedHashMap<String, GameObject> getGameObjects() {
        return gameObjects;
    }

    Bitmap[] getBitmaps() {
        return bitmaps;
    }
}
