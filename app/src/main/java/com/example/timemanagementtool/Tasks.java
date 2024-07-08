package com.example.timemanagementtool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Locale;

public class Tasks extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    private EditText EditTextInput;
    private TextView CountDown;

    private Button Set;
    private Button StartPause;
    private Button Reset;

    private android.os.CountDownTimer CountDownTimer;

    private boolean TimerRunning;

    private long StartTimeInMilli;
    private long TimeLeftInMilli;
    private long mEndTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);

        bottomNavigationView = findViewById(R.id.bottom_navigator);
        bottomNavigationView.setSelectedItemId(R.id.task);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.dashboard:
                        startActivity(new Intent(getApplicationContext(), Countdown.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Timer.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.task:
                        startActivity(new Intent(getApplicationContext(), Tasks.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.about:
                        startActivity(new Intent(getApplicationContext(), Contact.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

//Timer code
        {
            EditTextInput = findViewById(R.id.timerInput);
            CountDown = findViewById(R.id.countdown);

            Set = findViewById(R.id.btnSet);
            StartPause = findViewById(R.id.btnStartStop);
            Reset = findViewById(R.id.btnReset);


            Set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String input = EditTextInput.getText().toString();
                    if (input.length() == 0) {
                        Toast.makeText(Tasks.this, "Please enter a time.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long millisInput = Long.parseLong(input) * 60000;
                    if (millisInput == 0) {
                        Toast.makeText(Tasks.this, "Please enter value higher than 0", Toast.LENGTH_SHORT).show();
                    }

                    setTime(millisInput);
                    EditTextInput.setText("");
                }

            });


            StartPause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TimerRunning) {

                        pauseTimer();

                    } else {

                        startTimer();
                    }

                }
            });


            Reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    resetTimer();

                }
            });
        }





    }
    private void setTime(long milliseconds) {

        StartTimeInMilli = milliseconds;
        resetTimer();
        closeKeyb();

    }

    private void startTimer() {

        mEndTime = System.currentTimeMillis() + TimeLeftInMilli;
        CountDownTimer = new CountDownTimer(TimeLeftInMilli, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                TimeLeftInMilli = millisUntilFinished;
                updateCountdownText();

            }

            @Override
            public void onFinish() {

                TimerRunning = false;
                updateTimerInterface();
            }

        }.start();

        TimerRunning = true;
        updateTimerInterface();

    }

    private void pauseTimer() {

        CountDownTimer.cancel();
        TimerRunning = false;
        updateTimerInterface();

    }

    private void resetTimer() {

        TimeLeftInMilli = StartTimeInMilli;
        updateCountdownText();
        updateTimerInterface();
    }

    private void updateCountdownText() {
        int hours = (int) (TimeLeftInMilli / 1000) / 3600;
        int minutes = (int) ((TimeLeftInMilli / 1000) % 3600 ) / 60;
        int seconds = (int) (TimeLeftInMilli / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(), "%d: %02d : %02d", hours, minutes, seconds);

        } else {
            timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        }

        CountDown.setText(timeLeftFormatted);
    }

    private void updateTimerInterface() {
        if (TimerRunning) {
            EditTextInput.setVisibility(View.INVISIBLE);
            Set.setVisibility(View.INVISIBLE);
            Reset.setVisibility(View.INVISIBLE);
            StartPause.setText("Pause");
        } else {
            EditTextInput.setVisibility(View.VISIBLE);
            Set.setVisibility(View.VISIBLE);
            StartPause.setText("Start");

            if (TimeLeftInMilli < 1000) {
                StartPause.setVisibility(View.INVISIBLE);
            } else {
                StartPause.setVisibility(View.VISIBLE);
            }

            if (TimeLeftInMilli < StartTimeInMilli) {
                Reset.setVisibility(View.VISIBLE);
            } else {
                Reset.setVisibility(View.INVISIBLE);
            }
        }
    }
    private void closeKeyb() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("StartTimeInMilli", StartTimeInMilli);
        editor.putLong("millisLeft", TimeLeftInMilli);
        editor.putBoolean("timerRun", TimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (CountDownTimer !=null) {
            CountDownTimer.cancel();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        TimeLeftInMilli = prefs.getLong("millisLeft", StartTimeInMilli);
        TimerRunning= prefs.getBoolean("timerRunning", false);

        updateCountdownText();
        updateTimerInterface();

        if (TimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            TimeLeftInMilli = mEndTime - System.currentTimeMillis();

            if (TimeLeftInMilli < 0) {
                TimeLeftInMilli = 0;
                TimerRunning = false;
                updateCountdownText();
                updateTimerInterface();

            } else {
                startTimer();
            }
        }
    }
}