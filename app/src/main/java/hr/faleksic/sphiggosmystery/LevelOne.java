package hr.faleksic.sphiggosmystery;

import android.content.Context;

import java.util.LinkedHashMap;

public class LevelOne extends LevelData {

    public LevelOne(Context context, int screenWidth, int screenHeight){
        data = new LinkedHashMap<>();
        //puting background
        data.put("background", new Background("game_background", screenWidth, screenHeight, 0, 0));
        //puting enemy Al
        data.put("enemy", new Enemy("al", (int)(screenWidth*0.1), (int)(screenHeight*0.35), (int)(screenWidth/1.5), (int)(screenHeight*0.405)));
        //puting sheep
        data.put("sheep", new Sheep((int)(screenWidth*0.15), (int)(screenHeight*0.1), (int)(screenWidth/1.3), (int)(screenHeight*0.1), screenWidth, screenHeight));
        //puting wolf
        data.put("wolf", new Wolf((int)(screenWidth*0.15), (int)(screenHeight*0.1), (int)(screenWidth/1.25), (int)(screenHeight*0.3), screenWidth, screenHeight));
        //puting cabbage
        data.put("cabbage", new Cabbage((int)(screenWidth*0.1), (int)(screenHeight*0.1), (int)(screenWidth/1.2), (int)(screenHeight*0.5), screenWidth, screenHeight));
        //puting boat
        data.put("boat", new Boat((int)(screenWidth*0.3), (int)(screenHeight*0.2), screenWidth/2, (int)(screenHeight*0.2), screenWidth));
        //puting player
        data.put("player", new Player(context, screenWidth/5, (int)(screenHeight/2.5)));
        //puting doors
        data.put("door", new Door((int)(screenWidth*0.2), (int)(screenHeight*0.405), screenWidth / 2 - (int)(screenWidth*0.2) / 2, 0));
        //puting game on table
        data.put("gameOnTable", new GameOnTable("game1", (int)(screenWidth*0.1), (int)(screenHeight*0.15),
                screenWidth / 2 - (int)(screenWidth*0.1) / 2, (int) (screenHeight * 0.47)));
        //puting background for writing rules
        data.put("rulesBox", new RulesBox(screenWidth, (int)(screenHeight*0.3), 0, (int)(screenHeight - screenHeight*0.3)));
        //toxic cloud
        data.put("toxic", new Toxic(screenWidth, screenHeight, 0, 0));
        //game over screen
        data.put("gameOver", new GameOver(screenWidth, screenHeight, 0, 0));
        //retry button and retry button clicked
        data.put("retry", new GameButton("retry", screenWidth / 5, screenHeight / 8, (int)(screenWidth * 0.1), (int)(screenHeight * 0.8)));
        data.put("retryClick", new GameButton("retry_click", screenWidth / 5, screenHeight / 8, (int)(screenWidth * 0.1), (int)(screenHeight * 0.8)));
        //quit button and quit button clicked
        data.put("quit", new GameButton("quit", screenWidth / 5, screenHeight / 8, (int)(screenWidth * 0.7), (int)(screenHeight * 0.8)));
        data.put("quitClick", new GameButton("quit_click", screenWidth / 5, screenHeight / 8, (int)(screenWidth * 0.7), (int)(screenHeight * 0.8)));
    }
}
