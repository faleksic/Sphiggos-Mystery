package hr.faleksic.sphiggosmystery;

import android.view.MotionEvent;

public class InputController {

    public InputController() {
    }

    public void handleInput(MotionEvent motionEvent, SMView sm) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if(!sm.isShowedRules()) {
                    sm.setNumCLicks(sm.getNumCLicks() + 1);
                }
        }
    }
}
