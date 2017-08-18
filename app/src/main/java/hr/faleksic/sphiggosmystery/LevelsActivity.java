package hr.faleksic.sphiggosmystery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class LevelsActivity extends AppCompatActivity {

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);


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
                    dialog.show();
                    click(2);
                    
                    
                }
            });
        }

        if(level >= 3) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level3);
            imageButton.setImageResource(R.drawable.level_3);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    click(3);
                    
                    
                }
            });
        }

        if(level >= 4) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level4);
            imageButton.setImageResource(R.drawable.level_4);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    click(4);
                    
                    
                }
            });
        }

        if(level >= 5) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level5);
            imageButton.setImageResource(R.drawable.level_5);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    click(5);
                    
                    
                }
            });
        }

        if(level >= 6) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level6);
            imageButton.setImageResource(R.drawable.level_6);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    click(6);
                    
                    
                }
            });
        }

        if(level >= 7) {
            final ImageButton imageButton = (ImageButton)findViewById(R.id.button_levels_level7);
            imageButton.setImageResource(R.drawable.level_7);
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.show();
                    click(7);
                    
                    
                }
            });
        }
    }

    public void click(final int num) {
        new Thread() {
            @Override
            public void run() {
                Intent i = new Intent(LevelsActivity.this, GameActivity.class);
                i.putExtra("LEVEL", num);
                startActivity(i);
                finish();
            }
        }.start();
    }
}
