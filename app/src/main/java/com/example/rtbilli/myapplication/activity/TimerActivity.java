package com.example.rtbilli.myapplication.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.NumberPicker;

import com.example.rtbilli.myapplication.R;

/**
 * Created by rtbilli on 11/6/2016.
 */

public class TimerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        NumberPicker npDay = (NumberPicker)findViewById(R.id.numberPickerDay);
        npDay.setMinValue(0);
        NumberPicker npHour = (NumberPicker)findViewById(R.id.numberPickerHour);
        npHour.setMinValue(0);
        npHour.setMaxValue(23);
        NumberPicker npMin = (NumberPicker)findViewById(R.id.numberPickerMin);
        npMin.setMinValue(0);
        npMin.setMaxValue(59);
    }

}

