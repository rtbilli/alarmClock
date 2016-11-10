package com.example.rtbilli.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rtbilli.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button)findViewById(R.id.button_alarm);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, DateTimeActivity.class);
                startActivity(i);
            }
        });
        Button b1 = (Button)findViewById(R.id.button_timer);
        b1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(i);
            }
        });
        Button b2 = (Button)findViewById(R.id.button_location);
        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(i);
            }
        });
    }
}
