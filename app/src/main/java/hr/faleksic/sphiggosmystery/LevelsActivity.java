package hr.faleksic.sphiggosmystery;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LevelsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.preference_file_key), MODE_PRIVATE);
        int level = prefs.getInt(getResources().getString(R.string.level), 0);

        findViewById(R.id.button_levels_level1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(1);
            }
        });

        if(level >= 2) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level2);
            imageButton.setImageResource(R.drawable.level_2);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(2);
                    imageButton.setClickable(false);
                    imageButton.setEnabled(false);
                }
            });
        }

        if(level >= 3) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level3);
            imageButton.setImageResource(R.drawable.level_3);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(3);
                    imageButton.setClickable(false);
                    imageButton.setEnabled(false);
                }
            });
        }

        if(level >= 4) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level4);
            imageButton.setImageResource(R.drawable.level_4);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(4);
                    imageButton.setClickable(false);
                    imageButton.setEnabled(false);
                }
            });
        }

        if(level >= 5) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level5);
            imageButton.setImageResource(R.drawable.level_5);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(5);
                    imageButton.setClickable(false);
                    imageButton.setEnabled(false);
                }
            });
        }

        if(level >= 6) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level6);
            imageButton.setImageResource(R.drawable.level_6);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(6);
                    imageButton.setClickable(false);
                    imageButton.setEnabled(false);
                }
            });
        }

        if(level >= 7) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level7);
            imageButton.setImageResource(R.drawable.level_7);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click(7);
                    imageButton.setClickable(false);
                    imageButton.setEnabled(false);
                }
            });
        }
    }

    public void click(int num) {
        Intent i = new Intent(this, GameActivity.class);
        i.putExtra("LEVEL", num);
        startActivity(i);
        finish();
    }
}
