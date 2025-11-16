package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnAdd, btnList, btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAddHike);
        btnList = findViewById(R.id.btnViewHikes);
        btnSearch = findViewById(R.id.btnSearch);

        btnAdd.setOnClickListener(v ->
                startActivity(new Intent(this, AddHikeActivity.class)));

        btnList.setOnClickListener(v ->
                startActivity(new Intent(this, HikeListActivity.class)));

        btnSearch.setOnClickListener(v ->
                startActivity(new Intent(this, SearchActivity.class)));
    }
}
