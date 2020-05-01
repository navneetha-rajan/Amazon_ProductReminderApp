package com.e.reminder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private String TAG = "Listing_Page";
    private ListView listView;
    private SearchView searchView;
    private ArrayList<Product> productsToDisplay = new ArrayList<>();
    private AsyncTask searchTask = new SearchForItemsAsyncTask();

    ProductAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        searchView = findViewById(R.id.searchView);
        listView = findViewById(R.id.listview);
        searchTask.execute(new String[]{""});

        Button buttonAddProduct = findViewById(R.id.btn_add);

        buttonAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                Intent intent = new Intent(ListActivity.this, AddProduct.class);
                startActivity(intent);

            }
        });
    }

    private class SearchForItemsAsyncTask extends AsyncTask<String, Void, ArrayList<Product>> {
        @Override
        protected ArrayList<Product> doInBackground(String... params) {
            ProductsTableDatabaseAccess productsTableDatabaseAccess = ProductsTableDatabaseAccess.getInstance(ListActivity.this);
            Log.d(TAG, "databases content" + productsTableDatabaseAccess.getAllContents().toString());
            return new ArrayList<>(productsTableDatabaseAccess.getAllContents());
        }


        @Override
        protected void onPostExecute(ArrayList<Product> offerList) {
            ListActivity.this.adapter = new ProductAdapter(ListActivity.this, offerList);
            ListActivity.this.listView.setAdapter(ListActivity.this.adapter);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    adapter.getFilter().filter(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
    }
}
