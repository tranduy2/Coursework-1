package com.example.mhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class HikeDetailActivity extends AppCompatActivity {

    EditText name, location, date, parking, length, difficulty, description, opt1, opt2;
    Button btnUpdate, btnDelete, btnObs;

    DatabaseHelper db;
    int hikeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hike_detail);

        db = new DatabaseHelper(this);

        hikeId = getIntent().getIntExtra("id", -1);

        name = findViewById(R.id.etName2);
        location = findViewById(R.id.etLocation2);
        date = findViewById(R.id.etDate2);
        parking = findViewById(R.id.etParking2);
        length = findViewById(R.id.etLength2);
        difficulty = findViewById(R.id.etDifficulty2);
        description = findViewById(R.id.etDescription2);
        opt1 = findViewById(R.id.etOptional12);
        opt2 = findViewById(R.id.etOptional22);

        btnUpdate = findViewById(R.id.btnUpdateHike);
        btnDelete = findViewById(R.id.btnDeleteHike);
        btnObs = findViewById(R.id.btnViewObs);

        loadHike();

        btnUpdate.setOnClickListener(v -> updateHike());
        btnDelete.setOnClickListener(v -> deleteHike());
        btnObs.setOnClickListener(v ->
                startActivity(new Intent(this, ObservationActivity.class).putExtra("id", hikeId))
        );
    }

    private void loadHike() {
        Cursor c = db.getHikeById(hikeId);
        if (c.moveToFirst()) {
            name.setText(c.getString(1));
            location.setText(c.getString(2));
            date.setText(c.getString(3));
            parking.setText(c.getString(4));
            length.setText(String.valueOf(c.getDouble(5)));
            difficulty.setText(c.getString(6));
            description.setText(c.getString(7));
            opt1.setText(c.getString(8));
            opt2.setText(c.getString(9));
        }
    }

    private void updateHike() {
        boolean updated = db.updateHike(
                hikeId,
                name.getText().toString(),
                location.getText().toString(),
                date.getText().toString(),
                parking.getText().toString(),
                Double.parseDouble(length.getText().toString()),
                difficulty.getText().toString(),
                description.getText().toString(),
                opt1.getText().toString(),
                opt2.getText().toString()
        );

        Toast.makeText(this, updated ? "Updated!" : "Update failed", Toast.LENGTH_SHORT).show();
    }

    private void deleteHike() {
        new AlertDialog.Builder(this)
                .setTitle("Delete?")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteHike(hikeId);
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
