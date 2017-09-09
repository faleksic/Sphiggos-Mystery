package hr.faleksic.sphiggosmystery;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
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
        final LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int)(50 * getResources().getDisplayMetrics().density));
        lParams.gravity = Gravity.BOTTOM;
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(3);
        editText.setFilters(FilterArray);
        lParams.setMargins((int)(resolution.x * 0.25), 0, (int)(resolution.x * 0.3), 0);
        editText.setLayoutParams(lParams);
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        editText.setBackgroundColor(Color.TRANSPARENT);
        editText.setVisibility(View.GONE);
        editText.setHint(getResources().getString(R.string.level2_hint));

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                gameView = new SMView(this, resolution.x, resolution.y, 1);
            } else {
                gameView = new SMView(this, resolution.x, resolution.y, extras.getInt("LEVEL"));
            }
        } else {
            if(savedInstanceState.getSerializable("LEVEL") != null) {
                gameView = new SMView(this, resolution.x, resolution.y, (int) savedInstanceState.getSerializable("LEVEL"));
            } else {
                gameView = new SMView(this, resolution.x, resolution.y, 1);
            }
        }

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
