package com.e.reminder;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.app.AlarmManager;
import android.app.PendingIntent;

import java.util.ArrayList;
import java.util.PropertyPermission;
import java.util.Calendar;

public class AddProduct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);


        Button AddProductToDb = findViewById(R.id.btn_add);

        AddProductToDb.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                Intent intent_to_hp = new Intent(AddProduct.this, ListActivity.class);

                EditText editText=findViewById(R.id.name_et);
                String name=editText.getText().toString();
                editText = findViewById(R.id.ctgy_et);
                String category=editText.getText().toString();
                editText = findViewById(R.id.expiry_date_et);
                String expiry_date=editText.getText().toString();

                Product newProduct = new Product("101",name,category,expiry_date);


                String[] expdate=expiry_date.split("/");
                String date=expdate[0];
                int edate = Integer.parseInt(date);
                String month=expdate[1];
                int emonth = Integer.parseInt(month);
                String year=expdate[2];
                int eyear = Integer.parseInt(year);

                Calendar cal = Calendar.getInstance();
                int curyear=cal.get(Calendar.YEAR);
                int curdate =cal.get(Calendar.DATE);
                int curmonth=cal.get(Calendar.MONTH)+1;

                int diffy=eyear-curyear;
                int diffm=emonth-curmonth;
                int diffd=edate-curdate;

                cal.add(Calendar.YEAR,diffy);
                cal.add(Calendar.MONTH,diffm);
                cal.add(Calendar.DATE,diffd);
                cal.add(Calendar.HOUR_OF_DAY,00);
                cal.add(Calendar.MINUTE,00);
                cal.add(Calendar.SECOND,10);
                System.out.println("sending intnet"+cal);
                Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
                intent.putExtra("item_name",name);

                PendingIntent broadcast = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);

                CreateItemAsyncTask task = new CreateItemAsyncTask();
                task.execute(newProduct);

                startActivity(intent_to_hp);

            }
        });

    }

    private class CreateItemAsyncTask extends AsyncTask<Product, Void, Void> {
        @Override
        protected Void doInBackground(Product... products) {
            ProductsTableDatabaseAccess databaseAccess = ProductsTableDatabaseAccess.getInstance(AddProduct.this);
            databaseAccess.create(products[0]);
            return null;
        }
    }
}
