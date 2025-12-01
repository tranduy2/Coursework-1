package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.mhike.models.Hike;
import java.util.ArrayList;
import java.util.Calendar;

public class SearchActivity extends AppCompatActivity {

    EditText sName, sLocation, sDate, sLength;
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
        sLength = findViewById(R.id.searchLength);
        btnSearch = findViewById(R.id.btnDoSearch);
        listView = findViewById(R.id.searchListView);

        sDate.setOnClickListener(v -> showDatePicker());
        btnSearch.setOnClickListener(v -> doSearch());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Hike selectedHike = results.get(position);
            Intent intent = new Intent(SearchActivity.this, HikeDetailActivity.class);
            intent.putExtra("id", selectedHike.getId());
            startActivity(intent);
        });
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, y, m, d) ->
                sDate.setText(d + "/" + (m + 1) + "/" + y),
                c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void doSearch() {
        results.clear();
        Cursor c = db.searchHikes(
                sName.getText().toString().trim(),
                sLocation.getText().toString().trim(),
                sDate.getText().toString().trim(),
                sLength.getText().toString().trim()
        );

        if (c != null && c.moveToFirst()) {
            do {
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
                        c.getString(9),
                        c.getString(10),
                        c.getString(11),
                        c.getString(12)
                ));
            } while (c.moveToNext());
            c.close();
        } else {
            Toast.makeText(this, "No hikes found", Toast.LENGTH_SHORT).show();
        }
        adapter = new HikeAdapter(this, results);
        listView.setAdapter(adapter);
    }
}