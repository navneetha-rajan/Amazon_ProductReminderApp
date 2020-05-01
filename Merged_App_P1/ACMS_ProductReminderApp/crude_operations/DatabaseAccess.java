package com.e.afinal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Document;
import com.amazonaws.mobileconnectors.dynamodbv2.document.datatype.Primitive;
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
            @Override
            public void onClick(View v) {

                EditText editText=findViewById(R.id.name);
                String name=editText.getText().toString();
                editText = findViewById(R.id.category);
                String category=editText.getText().toString();
                editText = findViewById(R.id.expiry_date);
                String expiry_date=editText.getText().toString();
                System.out.println(name);
                Document newProduct = new Document();
                newProduct.put("name",name);
                newProduct.put("category",category);
                newProduct.put("expiry_date",expiry_date);

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
                newProduct.put("name",name);

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
                editProduct.put("name",name);
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

