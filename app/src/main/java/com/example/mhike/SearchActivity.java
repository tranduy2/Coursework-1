package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.mhike.models.Hike;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText sName, sLocation, sDate;
    Button btnSearch;
    ListView listView;

    DatabaseHelper db;

    ArrayList<Hike> results = new ArrayList<>();
    HikeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        db = new DatabaseHelper(this);

        sName = findViewById(R.id.searchName);
        sLocation = findViewById(R.id.searchLocation);
        sDate = findViewById(R.id.searchDate);
        btnSearch = findViewById(R.id.btnDoSearch);
        listView = findViewById(R.id.searchListView);

        btnSearch.setOnClickListener(v -> doSearch());
    }

    private void doSearch() {
        results.clear();

        Cursor c = db.searchHikes(
                sName.getText().toString(),
                sLocation.getText().toString(),
                sDate.getText().toString()
        );

        while (c.moveToNext()) {
            results.add(new Hike(
                    c.getInt(0),
                    c.getString(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4),
                    c.getDouble(5),
                    c.getString(6),
                    c.getString(7),
                    c.getString(8),
                    c.getString(9)
            ));
        }

        adapter = new HikeAdapter(this, results);
        listView.setAdapter(adapter);
    }
}
