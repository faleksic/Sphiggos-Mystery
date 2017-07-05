package hr.faleksic.sphiggosmystery;

import android.view.MotionEvent;

public class InputController {
    private int numClicks = 0;

    public InputController() {
    }

    public void handleInput(MotionEvent motionEvent, LevelManager l, SMView sm) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK   ) {
            case MotionEvent.ACTION_DOWN:
                if(!sm.isShowedRules()) {
                    if (!l.isRules()) {
                        l.setRules(true);
                    }
                    sm.setNumCLicks(numClicks);
                    numClicks++;
                }
        }
    }
}
