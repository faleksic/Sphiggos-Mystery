package hr.faleksic.sphiggosmystery;

import android.content.Context;

import java.util.LinkedHashMap;

public class GameFinish extends LevelData {

    public GameFinish(Context context, int screenWidth, int screenHeight) {
        data = new LinkedHashMap<>();

        data.put("finish", new Victory(screenWidth, screenHeight, 0, 0));

    }
}
