package hr.faleksic.sphiggosmystery;

import android.content.Context;

import java.util.LinkedHashMap;

class LevelSix extends LevelData {

    LevelSix(Context context, int screenWidth, int screenHeight) {
        data = new LinkedHashMap<>();

        data.put("background", new Background("game_background6", screenWidth, screenHeight, 0, 0));

        data.put("enemy", new Enemy("chris", (int)(screenWidth*0.1), (int)(screenHeight*0.35), (int)(screenWidth/1.5), (int)(screenHeight*0.405)));

        data.put("door", new Door((int)(screenWidth*0.2), (int)(screenHeight*0.405), screenWidth / 2 - (int)(screenWidth*0.1), 0));

        data.put("gameOnTable", new GameOnTable("game2", (int)(screenWidth*0.17), (int)(screenHeight*0.05),
                screenWidth / 2 - (int)(screenWidth*0.1), (int) (screenHeight * 0.57)));

        data.put("player", new Player(screenWidth/5, (int)(screenHeight/2.5)));

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
