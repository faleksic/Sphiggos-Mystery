package hr.faleksic.sphiggosmystery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);

        Crashlytics crashlyticsKit = new Crashlytics.Builder()
                .core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build())
                .build();
        Fabric.with(this, crashlyticsKit);
        setContentView(R.layout.activity_main);

        ImageButton playButton = (ImageButton) findViewById(R.id.button_main_play);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                SharedPreferences prefs = getSharedPreferences(getResources().getString(R.string.preference_file_key), MODE_PRIVATE);
                int level = prefs.getInt(getResources().getString(R.string.level), 0);
                if(level == 0 || level == 1) {
                    if(level == 0) {
                        SharedPreferences.Editor editor = getSharedPreferences(getResources().getString(R.string.preference_file_key), MODE_PRIVATE).edit();
                        editor.putInt(getResources().getString(R.string.level), 1).apply();
                    }
                    startActivity(new Intent(getApplicationContext(), IntroActivity.class));
                } else {
                    Intent i = new Intent(MainActivity.this, GameActivity.class);
                    i.putExtra("LEVEL", level);
                    startActivity(i);
                }
                finish();
            }
        });

        ImageButton levelsButton = (ImageButton) findViewById(R.id.button_main_levels);
        levelsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LevelsActivity.class));
                finish();
            }
        });

        ImageButton settingsButton = (ImageButton) findViewById(R.id.button_main_settings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
    }


}
