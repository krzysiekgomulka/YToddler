package com.abc.ytoddler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.abc.ytoddler.R.layout.activity_timer;

public class TimerActivity extends AppCompatActivity{
    private EditText mEditTextInput;//

    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private TextView mTextViewCountDown;

    BroadcastService broadcastService = new BroadcastService();

    Boolean isTimerOn = false;
    Boolean resetIsVisible = false;
    Boolean isPause = false;

    long millisUntilFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_timer);

        SharedPreferences sharedPreferences = getSharedPreferences("timer", MODE_PRIVATE);

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

            String dateString = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisInput),
                    TimeUnit.MILLISECONDS.toMinutes(millisInput) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisInput)),
                    TimeUnit.MILLISECONDS.toSeconds(millisInput) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisInput)));

            mEditTextInput.setText("");
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mTextViewCountDown.setText(dateString);
                }
            }, 300);

            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            editorSharedPreferences.putLong("millis", millisInput);
            editorSharedPreferences.commit();

            closeKeyboard();

            if (isTimerOn == true){
                final Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        resetTimer();
                    }
                }, 300);
            }
        });

        mButtonStartPause.setOnClickListener(v -> {
            if(resetIsVisible == false){
                mButtonReset.setVisibility(View.VISIBLE);
            }

            if (isTimerOn == false){
                mButtonStartPause.setText("stop");

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startTimer();
                    }
                }, 300);
            }
            else{
                mButtonStartPause.setText("start");

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pauseTimer();
                    }
                }, 300);
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


    private void startTimer() {
        if (isPause == true) {
            SharedPreferences sharedPreferences = getSharedPreferences("timer", MODE_PRIVATE);
            SharedPreferences.Editor editorSharedPreferences = sharedPreferences.edit();
            editorSharedPreferences.putLong("pause_millis", millisUntilFinished);
            editorSharedPreferences.commit();

            startService(new Intent(this, BroadcastService.class));
            isTimerOn = true;
            isPause = false;
        }
        else{
            startService(new Intent(this, BroadcastService.class));
            isTimerOn = true;
            isPause = false;

        }
    }

    private void pauseTimer() {
        if (isPause == false){
            if(isTimerOn == true){
                stopService(new Intent(getApplicationContext(), BroadcastService.class));
                isTimerOn = false;
                isPause = true;
            }
        }
    }

    private void resetTimer() {
        stopService(new Intent(getApplicationContext(), BroadcastService.class));
        isTimerOn = false;
        isPause = false;
        mButtonStartPause.setText("stop");


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startTimer();
            }
        }, 300);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateGUI(intent); // or whatever method used to update your GUI fields
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(br, new IntentFilter(BroadcastService.COUNTDOWN_BR));
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override
    public void onStop() {
        try {
            unregisterReceiver(br);
        } catch (Exception e) {
            // Receiver was probably already stopped in onPause()
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        stopService(new Intent(this, BroadcastService.class));
        super.onDestroy();
    }

    private void updateGUI(Intent intent) {
        if (intent.getExtras() != null) {
            millisUntilFinished = intent.getLongExtra("countdown", 0);
            String dateString = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            mTextViewCountDown.setText(dateString);
        }
    }

}
