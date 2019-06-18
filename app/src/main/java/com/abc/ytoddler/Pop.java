package com.abc.ytoddler;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;



public class Pop extends Activity {

    protected Button girl_four_button, girl_seven_button, girl_ten_button,
            boy_four_button, boy_seven_button, boy_ten_button;

    public static List<String> girlFourPlayList = new ArrayList<String>();
    public static List<String> girlSevenPlayList = new ArrayList<String>();
    public static List<String> girlTenPlayList = new ArrayList<String>();
    public static List<String> boyFourPlayList = new ArrayList<String>();
    public static List<String> boySevenPlayList = new ArrayList<String>();
    public static List<String> boyTenPlayList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int) (height*.6));

        girl_four_button = (Button) findViewById(R.id.girl_four_button);
        girl_seven_button = (Button) findViewById(R.id.girl_seven_button);
        girl_ten_button = (Button) findViewById(R.id.girl_ten_button);
        boy_four_button = (Button) findViewById(R.id.boy_four_button);
        boy_seven_button = (Button) findViewById(R.id.boy_seven_button);
        boy_ten_button = (Button) findViewById(R.id.boy_ten_button);


        girl_four_button.setOnClickListener(
                v -> {
                    Intent intent = getIntent();
                    girlFourPlayList.add(intent.getStringExtra("videoId"));
                    Toast.makeText(getApplicationContext(), "Video has been added", Toast.LENGTH_SHORT).show();
                }
        );


        girl_seven_button.setOnClickListener(
                v -> {
                    Intent intent = getIntent();
                    girlSevenPlayList.add(intent.getStringExtra("videoId"));
                    Toast.makeText(getApplicationContext(), "Video has been added", Toast.LENGTH_SHORT).show();
                }
        );

        girl_ten_button.setOnClickListener(
                v -> {
                    Intent intent = getIntent();
                    girlTenPlayList.add(intent.getStringExtra("videoId"));
                    Toast.makeText(getApplicationContext(), "Video has been added", Toast.LENGTH_SHORT).show();
                }
        );

        boy_four_button.setOnClickListener(
                v -> {
                    Intent intent = getIntent();
                    boyFourPlayList.add(intent.getStringExtra("videoId"));
                    Toast.makeText(getApplicationContext(), "Video has been added", Toast.LENGTH_SHORT).show();
                }
        );

        boy_seven_button.setOnClickListener(
                v -> {
                    Intent intent = getIntent();
                    boySevenPlayList.add(intent.getStringExtra("videoId"));
                    Toast.makeText(getApplicationContext(), "Video has been added", Toast.LENGTH_SHORT).show();
                }
        );

        boy_ten_button.setOnClickListener(
                v -> {
                    Intent intent = getIntent();
                    boyTenPlayList.add(intent.getStringExtra("videoId"));
                    Toast.makeText(getApplicationContext(), "Video has been added", Toast.LENGTH_SHORT).show();
                }
        );
    }

}
