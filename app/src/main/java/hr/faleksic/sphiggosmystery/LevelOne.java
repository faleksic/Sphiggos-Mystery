package hr.faleksic.sphiggosmystery;

import android.content.Context;
import java.util.ArrayList;

public class LevelOne extends LevelData {

    public LevelOne(Context context, int screenWidth, int screenHeight){
        data = new ArrayList<>();
        //adding background
        data.add(new Background("game_background", screenWidth, screenHeight, 0, 0));
        //adding enemy Al
        data.add(new Enemy("al", (int)(screenWidth*0.1), (int)(screenHeight*0.35), (int)(screenWidth/1.5), (int)(screenHeight*0.405)));
        //adding sheep
        data.add(new Sheep((int)(screenWidth*0.15), (int)(screenHeight*0.1), (int)(screenWidth/1.3), (int)(screenHeight*0.1)));
        //adding wolf
        data.add(new Wolf((int)(screenWidth*0.15), (int)(screenHeight*0.1), (int)(screenWidth/1.25), (int)(screenHeight*0.3)));
        //adding cabbage
        data.add(new Cabbage((int)(screenWidth*0.1), (int)(screenHeight*0.1), (int)(screenWidth/1.2), (int)(screenHeight*0.5)));
        //adding boat
        data.add(new Boat((int)(screenWidth*0.3), (int)(screenHeight*0.2), screenWidth/2, (int)(screenHeight*0.2)));
        //adding player
        data.add(new Player(context, screenWidth/5, (int)(screenHeight/2.5)));
        //adding doors
        data.add(new Door((int)(screenWidth*0.2), (int)(screenHeight*0.405), screenWidth / 2 - (int)(screenWidth*0.2) / 2, 0));
        //adding game on table
        data.add(new GameOnTable("game1", (int)(screenWidth*0.1), (int)(screenHeight*0.15),
                screenWidth / 2 - (int)(screenWidth*0.1) / 2, (int) (screenHeight * 0.47)));
        //adding background for writing rules
        data.add(new RulesBox(screenWidth, (int)(screenHeight*0.3), 0, (int)(screenHeight - screenHeight*0.3)));
    }
}
