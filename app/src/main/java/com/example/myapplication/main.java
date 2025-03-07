package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class main extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView mediaButton = findViewById(R.id.media);
        ImageView plusButton = findViewById(R.id.plus);
        ImageView settingButton = findViewById(R.id.setting);

        mediaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(main.this, tracks.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error starting tracks activity", e);
                }
            }
        });

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(main.this, add_new.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error starting add_new activity", e);
                }
            }
        });

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(main.this, settings.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "Error starting settings activity", e);
                }
            }
        });


    }
}