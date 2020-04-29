package com.e.afinal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String TAG = "DynamoDb_Demo";
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button buttonAddItem = findViewById(R.id.button_add);

        buttonAddItem.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                EditText editText=findViewById(R.id.name);
                String name=editText.getText().toString();
                editText = findViewById(R.id.category);
                String category=editText.getText().toString();
                editText = findViewById(R.id.expiry_date);
                String expiry_date=editText.getText().toString();
                System.out.println(name);
                System.out.println(category);
                System.out.println(expiry_date);
                Document newProduct = new Document();
                newProduct.put("item_name",name);
                newProduct.put("category",category);
                newProduct.put("expiry_date",expiry_date);
                System.out.println(newProduct);


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
                System.out.println(curmonth);
                System.out.println(curdate);
                System.out.println(curyear);
                System.out.println("exppppppppppp");
                System.out.println(emonth);
                System.out.println(edate);
                System.out.println(eyear);
                System.out.println("difffffffffffff");
                int diffy=eyear-curyear;
                int diffm=emonth-curmonth;
                int diffd=edate-curdate;
                System.out.println(diffm);
                System.out.println("diff date"+diffd);
                System.out.println(diffy);
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
            }
        });

        Button buttonDeleteItem = findViewById(R.id.button_delete);
        buttonDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText=findViewById(R.id.name);
                String name=editText.getText().toString();

                Document newProduct = new Document();
                newProduct.put("item_name",name);

                DeleteItemAsyncTask task = new DeleteItemAsyncTask();
                task.execute(newProduct);
            }
        });

        Button buttonUpdateItem = findViewById(R.id.button_update);
        buttonUpdateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText=findViewById(R.id.name);
                String name=editText.getText().toString();
                editText = findViewById(R.id.category);
                String category=editText.getText().toString();
                editText = findViewById(R.id.expiry_date);
                String expiry_date=editText.getText().toString();
                System.out.println(name);
                Document editProduct = new Document();
                editProduct.put("item_name",name);
                editProduct.put("category",category);
                editProduct.put("expiry_date",expiry_date);

                UpdateItemAsyncTask task = new UpdateItemAsyncTask();
                task.execute(editProduct);
            }
        });

        Button buttonGet = findViewById(R.id.button_alldata);

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Getting all devices...");
                GetAllItemsAsyncTask getAllDevicesTask = new GetAllItemsAsyncTask();
                getAllDevicesTask.execute();
            }
        });

    }


    private class CreateItemAsyncTask extends AsyncTask<Document, Void, Void> {
        @Override
        protected Void doInBackground(Document... documents) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            Log.d(TAG, "product sending content"+documents[0].toString());
            databaseAccess.create(documents[0]);

            return null;
        }
    }

    private class DeleteItemAsyncTask extends AsyncTask<Document, Void, Void> {
        @Override
        protected Void doInBackground(Document... documents) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            databaseAccess.delete(documents[0]);
            return null;
        }
    }

    private class UpdateItemAsyncTask extends AsyncTask<Document, Void, Void> {
        @Override
        protected Void doInBackground(Document... documents) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            databaseAccess.update(documents[0]);
            return null;
        }
    }

    private class GetAllItemsAsyncTask extends AsyncTask<Void,Void,List<Document>>{
        @Override
        protected List<Document> doInBackground(Void... params) {
            DatabaseAccess databaseAccess = DatabaseAccess.getInstance(MainActivity.this);
            Log.d(TAG, "databases content"+databaseAccess.getAllContents().toString());
            return databaseAccess.getAllContents();
        }

        @Override
        protected void onPostExecute(List<Document> documents) {
        }
    }
}


