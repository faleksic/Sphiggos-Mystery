package hr.faleksic.sphiggosmystery;

import android.graphics.Point;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class GameActivity extends AppCompatActivity {

    private SMView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();
        Point resolution = new Point();
        display.getSize(resolution);

        FrameLayout layout = new FrameLayout(this);

        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        EditText editText = new EditText(this);
        editText.setId(R.id.level2_edit_text);
        final LayoutParams lParams = new LayoutParams((int)(200 * getResources().getDisplayMetrics().density), (int)(50 * getResources().getDisplayMetrics().density));
        lParams.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
        editText.setLayoutParams(lParams);
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setVisibility(View.INVISIBLE);

        gameView = new SMView(this, resolution.x, resolution.y);
        layout.addView(gameView);
        layout.addView(editText);

        setContentView(layout);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
