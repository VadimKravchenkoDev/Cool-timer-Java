package com.kravchenkovadim.cooltimerkotlin;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Boolean checkTimer = true;
    Button button;
    CountDownTimer timer;
    TextView textView;
    private long secondLeft = 60000;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

    }

    public void onClick(View view) {
        if (checkTimer) {
            timer = new CountDownTimer(secondLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textView.setText("00:" + String.valueOf(millisUntilFinished / 1000));
                secondLeft = millisUntilFinished;
            }

            @Override
            public void onFinish() {
            }
        };
            startTimer();
        } else {
            stopTimer();
        }
    }
    public void startTimer(){
        timer.start();
        button.setText("PAUSE");
        checkTimer = false;
    }
    public void stopTimer(){
        button.setText("START");
        checkTimer = true;
        timer.cancel();
    }
}