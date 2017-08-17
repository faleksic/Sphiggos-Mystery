package hr.faleksic.sphiggosmystery;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.support.annotation.IdRes;
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

        //Edit text to write an answer
        EditText editText = new EditText(this);
        //setting id to find view easily
        editText.setId(R.id.level2_edit_text);
        //layout params to set width and height
        final LayoutParams lParams = new LayoutParams(LayoutParams.MATCH_PARENT, (int)(50 * getResources().getDisplayMetrics().density));
        //gravity bottom
        lParams.gravity = Gravity.BOTTOM;
        //setting max chars 3
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(3);
        editText.setFilters(FilterArray);
        //setting margins
        lParams.setMargins((int)(resolution.x * 0.25), 0, (int)(resolution.x * 0.3), 0);
        editText.setLayoutParams(lParams);
        //accept only numbers
        editText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //align center
        editText.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        //removing bottom line
        editText.setBackgroundColor(Color.TRANSPARENT);
        //hiding view
        editText.setVisibility(View.GONE);
        //placeholder
        editText.setHint(getResources().getString(R.string.level2_hint));

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                gameView = new SMView(this, resolution.x, resolution.y, 1);
            } else {
                gameView = new SMView(this, resolution.x, resolution.y, extras.getInt("LEVEL"));
            }
        } else {
            gameView = new SMView(this, resolution.x, resolution.y, (int) savedInstanceState.getSerializable("LEVEL"));
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
