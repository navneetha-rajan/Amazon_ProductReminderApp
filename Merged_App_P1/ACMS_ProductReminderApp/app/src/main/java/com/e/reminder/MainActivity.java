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

        Button buttonLogin = findViewById(R.id.btn_sign_in);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);

            }
        });




    }

    }


