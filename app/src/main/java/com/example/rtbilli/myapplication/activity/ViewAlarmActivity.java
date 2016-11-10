package com.example.rtbilli.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

import com.example.rtbilli.myapplication.R;

/**
 * Created by Joel Gordon on 11/9/2016.
 */

public class ViewAlarmActivity extends AppCompatActivity{

    String [] strings = {"A", "few", "Things", "in", "there"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_alarm);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, strings);
    }
}
