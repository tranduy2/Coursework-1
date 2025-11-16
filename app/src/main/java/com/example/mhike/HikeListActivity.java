package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.content.Intent;

import com.example.mhike.models.Hike;

import java.util.ArrayList;

public class HikeListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Hike> hikes = new ArrayList<>();
    HikeAdapter adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_list);

        listView = findViewById(R.id.hikeListView);
        db = new DatabaseHelper(this);

        loadData();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int hikeId = hikes.get(position).getId();
            Intent intent = new Intent(this, HikeDetailActivity.class);
            intent.putExtra("id", hikeId);
            startActivity(intent);
        });
    }

    private void loadData() {
        hikes.clear();

        Cursor c = db.getAllHikes();
        while (c.moveToNext()) {
            hikes.add(new Hike(
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

        adapter = new HikeAdapter(this, hikes);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }
}
