package com.e.reminder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.app.AlarmManager ;
import android.app.PendingIntent ;
import android.content.Intent ;
import android.os.Bundle ;
import android.util.Log;
import android.view.View ;
import android.widget.Button;

import java.util.Calendar ;

public class MainActivity extends AppCompatActivity {
    Button btn1;
    private String TAG = "notification";
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1=findViewById(R.id.btn1);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "databases content"+ " button clicked");
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.HOUR_OF_DAY,0);
                cal.add(Calendar.MINUTE,02);
                cal.add(Calendar.SECOND,00);
                Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);

                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            }
        });




    }

    }


