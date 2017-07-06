package hr.faleksic.sphiggosmystery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;

public class LevelOne {

    private Sheep sheep;
    private Wolf wolf;
    private Cabbage cabbage;

    public LevelOne(Context context, LevelManager lm, int screenWidth, int screenHeight){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Enemy enemy = new Enemy("al", (int)(screenWidth*0.1), (int)(screenHeight*0.35), (int)(screenWidth/1.5), (int)(screenHeight*0.405));
        Sheep sheep = new Sheep((int)(screenWidth*0.2), (int)(screenHeight*0.1), (int)(screenWidth/1.3), (int)(screenHeight*0.1));
        Bitmap gameOnTable = Bitmap.createScaledBitmap(BitmapFactory.decodeResource
                (context.getResources(),R.drawable.game1, options), (int)(screenWidth*0.1), (int)(screenHeight*0.15), false);
        ArrayList<String> rulesText = new ArrayList<>();
        for(int i=1; i<6; i++) {
            rulesText.add(lm.getStringResourceByName("al_one_rule_" + String.valueOf(i)));
        }

        lm.setEnemy(enemy);
        lm.setGameOnTable(gameOnTable);
        lm.setRulesText(rulesText);
    }

    public Cabbage getCabbage() {
        return cabbage;
    }

    public void setCabbage(Cabbage cabbage) {
        this.cabbage = cabbage;
    }

    public Wolf getWolf() {
        return wolf;
    }

    public void setWolf(Wolf wolf) {
        this.wolf = wolf;
    }

    public Sheep getSheep() {
        return sheep;
    }

    public void setSheep(Sheep sheep) {
        this.sheep = sheep;
    }
}
