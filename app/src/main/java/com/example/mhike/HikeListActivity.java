package com.example.mhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Toast;
import com.example.mhike.models.Hike;

import java.util.ArrayList;

public class HikeListActivity extends AppCompatActivity {

    ListView listView;
    Button btnDeleteAll;
    ArrayList<Hike> hikes = new ArrayList<>();
    HikeAdapter adapter;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_list);

        listView = findViewById(R.id.hikeListView);
        btnDeleteAll = findViewById(R.id.btnDeleteAll);
        db = new DatabaseHelper(this);

        loadData();

        btnDeleteAll.setOnClickListener(v -> confirmDeleteAll());

        listView.setOnItemClickListener((parent, view, position, id) -> {
            int hikeId = hikes.get(position).getId();
            Intent intent = new Intent(this, HikeDetailActivity.class);
            intent.putExtra("id", hikeId);
            startActivity(intent);
        });
    }

    private void confirmDeleteAll() {
        new AlertDialog.Builder(this)
                .setTitle("WARNING")
                .setMessage("Are you sure you want to delete ALL hikes? This cannot be undone.")
                .setPositiveButton("DELETE ALL", (dialog, which) -> {
                    db.deleteAllHikes();
                    Toast.makeText(this, "All data deleted", Toast.LENGTH_SHORT).show();
                    loadData();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadData() {
        hikes.clear();
        Cursor c = db.getAllHikes();
        if (c != null && c.moveToFirst()) {
            do {
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
                        c.getString(9),
                        c.getString(10),
                        c.getString(11),
                        c.getString(12)
                ));
            } while (c.moveToNext());
            c.close();
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