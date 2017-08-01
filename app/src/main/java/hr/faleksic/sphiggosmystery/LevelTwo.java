package hr.faleksic.sphiggosmystery;

import android.content.Context;

import java.util.LinkedHashMap;

public class LevelTwo extends LevelData {

    public LevelTwo(Context context, int screenWidth, int screenHeight) {
        data = new LinkedHashMap<>();

        data.put("background", new Background("game_background2", screenWidth, screenHeight, 0, 0));

        data.put("enemy", new Enemy("matt", (int)(screenWidth*0.1), (int)(screenHeight*0.35), (int)(screenWidth/1.5), (int)(screenHeight*0.405)));

        data.put("door", new Door((int)(screenWidth*0.2), (int)(screenHeight*0.405), screenWidth / 2 - (int)(screenWidth*0.1), 0));

        data.put("gameOnTable", new GameOnTable("game2", (int)(screenWidth*0.17), (int)(screenHeight*0.05),
                screenWidth / 2 - (int)(screenWidth*0.1), (int) (screenHeight * 0.57)));

        data.put("player", new Player(context, screenWidth/5, (int)(screenHeight/2.5)));

        data.put("rulesBox", new RulesBox(screenWidth, (int)(screenHeight*0.3), 0, (int)(screenHeight - screenHeight*0.3)));
    }
}
