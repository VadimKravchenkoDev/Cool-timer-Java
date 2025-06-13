package com.kravchenkovadim.cooltimerkotlin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.PreferenceManager;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView textView;
    private Button button;
    private CountDownTimer countDownTimer;
    private boolean isTimerOn;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        isTimerOn = false;
        seekBar = findViewById(R.id.seekBar);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        seekBar.setMax(600);
        seekBar.setProgress(59);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                progress = progress * 1000;
                setTimer(progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        button = findViewById(R.id.button);
        button.setOnClickListener(view -> {
            if (!isTimerOn) {
                button.setText("STOP");
                seekBar.setEnabled(false);
                isTimerOn = true;
                countDownTimer = new CountDownTimer(seekBar.getProgress() * 1000L, 1000) {
                    @Override
                    public void onTick(long l) {
                        setTimer(l);
                    }

                    @Override
                    public void onFinish() {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        boolean isChecked = prefs.getBoolean("sound", false);
                        String sound = prefs.getString("melody", "bell");
                        int resId = getResources().getIdentifier(sound, "raw",getPackageName());
                        if(isChecked){

                            mediaPlayer = MediaPlayer.create(getApplicationContext(), resId);
                            mediaPlayer.start();
                        }
                        resetTimer();
                    }
                }.start();
            } else {
                resetTimer();
            }
        });
    }

    private void setTimer(long progress) {
        int minutes = (int) progress / 1000 / 60;
        int seconds = (int) progress / 1000 - minutes * 60;
        String stMinutes = "";
        String stSeconds = "";
        if (minutes < 10) {
            stMinutes = "0" + minutes;
        } else {
            stMinutes = "" + minutes;
        }
        if (seconds < 10) {
            stSeconds = "0" + seconds;
        } else {
            stSeconds = "" + seconds;
        }
        textView.setText(stMinutes + ":" + stSeconds);
    }

    private void resetTimer() {
        button.setText("START");
        seekBar.setEnabled(true);
        seekBar.setProgress(59);
        textView.setText("00:59");
        isTimerOn = false;
        countDownTimer.cancel();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.menu_settings){
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
            try {
                Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                m.setAccessible(true);
                m.invoke(menu, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onMenuOpened(featureId, menu);
    }
}