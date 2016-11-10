package com.example.rtbilli.myapplication.activity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.rtbilli.myapplication.R;
import com.example.rtbilli.myapplication.receiver.LocationReceiver;

import java.util.concurrent.TimeUnit;

/**
 * Created by rtbilli on 11/6/2016.
 */

public class TimerActivity extends AppCompatActivity {
    int change = 0;
    CountDownTimer cdt;
    private EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        msg = (EditText)findViewById(R.id.edit_timer_message);

        NumberPicker second = (NumberPicker)findViewById(R.id.sec);
        second.setMinValue(0);
        second.setMaxValue(60)
        ;
        NumberPicker minute = (NumberPicker)findViewById(R.id.min);
        minute.setMinValue(0);
        minute.setMaxValue(60);

        NumberPicker hours = (NumberPicker)findViewById(R.id.hour);
        hours.setMinValue(0);
        hours.setMaxValue(23);

        Button b = (Button)findViewById(R.id.button);
        b.setText("Start");
        b.setOnClickListener(new View.OnClickListener() {
            Button b = (Button) findViewById(R.id.button);
            public void onClick(View v) {
                if (change % 2 == 0) {
                    b.setText("Stop");
                    int secTime = 0;
                    int minTime = 0;
                    int hourTime = 0;
                    NumberPicker second = (NumberPicker) findViewById(R.id.sec);
                    second.setMinValue(0);
                    second.setMaxValue(60);
                    NumberPicker minute = (NumberPicker) findViewById(R.id.min);
                    minute.setMinValue(0);
                    minute.setMaxValue(60);
                    NumberPicker hours = (NumberPicker) findViewById(R.id.hour);
                    hours.setMinValue(0);
                    hours.setMaxValue(23);
                    secTime = second.getValue();
                    minTime = minute.getValue();
                    hourTime = hours.getValue();
                    int seconds = secTime * 1000;
                    int minutes = minTime * 60 * 1000;
                    int hour = hourTime * 60 * 60 * 1000;
                    long time = seconds + minutes + hour;
                    cdt = new CountDownTimer(time, 1000) { // adjust the milli seconds here
                        TextView tv = (TextView) findViewById(R.id.timerTextView);
                        public void onTick(long millisUntilFinished) {
                            tv.setText("" + String.format("%d hour, %d min, %d sec",
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                        }

                        public void onFinish() {
                            sendNotification();
                            tv.setText("done!");
                            b.setText("Reset");
                        }
                    }.start();
                    change++;
                }
                else{
                    Button b = (Button) findViewById(R.id.button);
                    b.setText("Start");
                    TextView tv = (TextView) findViewById(R.id.timerTextView);
                    tv.setText("" + String.format("0 hour, 0 min, 0 sec"));
                    cdt.cancel();
                    change++;
                }
            }
        });
    }

    private void sendNotification() {
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, TimerActivity.class), 0);

        NotificationManager alarmNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder alarmNotificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(
                this).setContentTitle("Alarm").setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg.getText()))
                .setContentText(msg.getText());


        alarmNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alarmNotificationBuilder.build());
    }
}
