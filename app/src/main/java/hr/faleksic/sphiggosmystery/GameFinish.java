package hr.faleksic.sphiggosmystery;

import android.content.Context;

import java.util.LinkedHashMap;

class GameFinish extends LevelData {

    GameFinish(Context context, int screenWidth, int screenHeight) {
        data = new LinkedHashMap<>();

        data.put("finish", new Victory(screenWidth, screenHeight, 0, 0));

    }
}
