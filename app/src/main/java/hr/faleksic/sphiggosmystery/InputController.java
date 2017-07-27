package hr.faleksic.sphiggosmystery;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

public class InputController {

    private int screenWidth;
    private int screenHeight;

    public InputController(int screenWidth, int screenHeigth) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeigth;
    }

    public void handleInput(MotionEvent event, SMView sm, LevelManager lm) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                if (!sm.isShowedRules()) {
                    sm.setNumCLicks(sm.getNumCLicks() + 1);
                } else if (sm.isMiniGame()) {
                    switch (lm.getLevel()) {
                        case 1:
                            Rect sheepButton = new Rect((int) (screenWidth * 0.12), (int) (screenHeight * 0.74),
                                    (int) (screenWidth * 0.26), screenHeight);
                            Rect wolfButton = new Rect((int) (screenWidth * 0.3), (int) (screenHeight * 0.74),
                                    (int) (screenWidth * 0.44), screenHeight);
                            Rect cabbageButton = new Rect((int) (screenWidth * 0.48), (int) (screenHeight * 0.74),
                                    (int) (screenWidth * 0.61), screenHeight);
                            Rect boatButton = new Rect((int) (screenWidth * 0.66), (int) (screenHeight * 0.74),
                                    (int) (screenWidth * 0.8), screenHeight);
                            if (sheepButton.contains(x, y)) {
                                sm.moveSheep();
                            } else if(wolfButton.contains(x, y)) {
                                sm.moveWolf();
                            } else if(cabbageButton.contains(x, y)) {
                                sm.moveCabbage();
                            } else if(boatButton.contains(x, y)) {
                                sm.moveBoat();
                            }
                    }
                } else {
                    sm.killAnimation();
                }
            }
        }
    }
}
