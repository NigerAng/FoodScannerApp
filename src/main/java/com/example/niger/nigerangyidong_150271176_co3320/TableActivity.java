package com.example.niger.nigerangyidong_150271176_co3320;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    //Adapter for ListView
    private FoodArrayAdapter mArrayAdapter;
    private ListView foodListView;
    List<String[]> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        //set up similar navigation bar to the one in MainActivity
        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        //Create a back button in the navigation bar to navigate back to MainActivity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Populate List View
        foodListView = findViewById(R.id.list);
        //Create an empty view for when there are no data in database
        View emptyView = findViewById(R.id.empty_view);
        foodListView.setEmptyView(emptyView);

        // Setup an Adapter to create a list item for each row
        mArrayAdapter = new FoodArrayAdapter(getApplicationContext(), R.layout.list_item);
        foodListView.setAdapter(mArrayAdapter);

        //Access and stream res/raw/data.csv
        //Load into FoodArray with CSVReader class
        InputStream inputStream = getResources().openRawResource(R.raw.data);
        CSVReader csv = new CSVReader(inputStream);
        foodList = csv.read();
        for(String [] foodData : foodList){
            mArrayAdapter.add(foodData);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/table_menu.xml file
        getMenuInflater().inflate(R.menu.table_menu, menu);

        // Associate searchable configuration with the SearchView
        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<String[]> tempList = new ArrayList<>();
                FoodArrayAdapter adapter = new FoodArrayAdapter(getApplicationContext(), R.layout.list_item);
                foodListView.setAdapter(adapter);

                for (String [] temp : foodList) {
                    if(temp[2].toLowerCase().contains(s.toLowerCase())){
                        adapter.add(temp);
                    }
                }
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}