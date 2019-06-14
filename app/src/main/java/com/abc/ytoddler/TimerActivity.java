package com.abc.ytoddler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import static com.abc.ytoddler.R.layout.activity_timer;

public class TimerActivity extends AppCompatActivity{
    private EditText mEditTextInput;//

    private Button mButtonSet;//
    private Button mButtonStartPause;
    private Button mButtonReset;

    private TextView mTextViewCountDown;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long startTimeInMilliSeconds;
    private long timeLeftInMilliSeconds;
    private long finalTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_timer);

        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);

        mButtonSet.setOnClickListener(v -> {
            String input = mEditTextInput.getText().toString();
            if (input.length() == 0) {
                Toast.makeText(TimerActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            long millisInput = Long.parseLong(input) * 60000;
            if (millisInput == 0) {
                Toast.makeText(TimerActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                return;
            }

            setTime(millisInput);
            mEditTextInput.setText("");
        });

        mButtonStartPause.setOnClickListener(v -> {
            if (mTimerRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        mButtonReset.setOnClickListener(v -> resetTimer());

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(keyCode==KeyEvent.KEYCODE_BACK)
        {
            this.startActivity(new Intent(TimerActivity.this,ProfileActivity.class));
        }
        return true;
    }


    private void setTime(long milliseconds) {
        startTimeInMilliSeconds = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        finalTime = System.currentTimeMillis() + timeLeftInMilliSeconds;

        mCountDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMilliSeconds = millisUntilFinished;
                updateTheCountDown();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateTheWatch();
            }
        }.start();

        mTimerRunning = true;
        updateTheWatch();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateTheWatch();
    }

    private void resetTimer() {
        timeLeftInMilliSeconds = startTimeInMilliSeconds;
        updateTheCountDown();
        updateTheWatch();
    }

    private void updateTheCountDown() {
        int formatHours = (int) (timeLeftInMilliSeconds / 1000) / 3600;
        int formatMinutes = (int) ((timeLeftInMilliSeconds / 1000) % 3600) / 60;
        int formatSeconds = (int) (timeLeftInMilliSeconds / 1000) % 60;

        String timeLeftInReadableFormat;
        if (formatHours > 0) {
            timeLeftInReadableFormat = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", formatHours, formatMinutes, formatSeconds);
        } else {
            timeLeftInReadableFormat = String.format(Locale.getDefault(),
                    "%02d:%02d", formatMinutes, formatSeconds);
        }

        mTextViewCountDown.setText(timeLeftInReadableFormat);
    }

    private void updateTheWatch() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText(getString(R.string.PauseTimer));
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText(getString(R.string.StartTimer));

            if (timeLeftInMilliSeconds < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (timeLeftInMilliSeconds < startTimeInMilliSeconds) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void closeKeyboard() {
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

        editor.putLong("startTimeInMillis", startTimeInMilliSeconds);
        editor.putLong("millisLeft", timeLeftInMilliSeconds);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", finalTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        startTimeInMilliSeconds = prefs.getLong("startTimeInMillis", 600000);
        timeLeftInMilliSeconds = prefs.getLong("millisLeft", startTimeInMilliSeconds);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateTheCountDown();
        updateTheWatch();

        if (mTimerRunning) {
            finalTime = prefs.getLong("endTime", 0);
            timeLeftInMilliSeconds = finalTime - System.currentTimeMillis();

            if (timeLeftInMilliSeconds < 0) {
                timeLeftInMilliSeconds = 0;
                mTimerRunning = false;
                updateTheCountDown();
                updateTheWatch();
            } else {
                startTimer();
            }
        }
    }
}
