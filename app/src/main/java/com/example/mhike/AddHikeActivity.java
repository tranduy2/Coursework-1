package com.example.mhike;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddHikeActivity extends AppCompatActivity {

    EditText name, location, date, parking, length, difficulty, description, opt1, opt2;
    Button btnSave;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        db = new DatabaseHelper(this);

        name = findViewById(R.id.etName);
        location = findViewById(R.id.etLocation);
        date = findViewById(R.id.etDate);
        parking = findViewById(R.id.etParking);
        length = findViewById(R.id.etLength);
        difficulty = findViewById(R.id.etDifficulty);
        description = findViewById(R.id.etDescription);
        opt1 = findViewById(R.id.etOptional1);
        opt2 = findViewById(R.id.etOptional2);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> validateAndConfirm());
    }

    private void validateAndConfirm() {
        String n = name.getText().toString().trim();
        String l = location.getText().toString().trim();
        String d = date.getText().toString().trim();
        String p = parking.getText().toString().trim();
        String len = length.getText().toString().trim();
        String dif = difficulty.getText().toString().trim();

        if (n.isEmpty() || l.isEmpty() || d.isEmpty() || p.isEmpty() || len.isEmpty() || dif.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String msg =
                "Name: " + n + "\n" +
                        "Location: " + l + "\n" +
                        "Date: " + d + "\n" +
                        "Parking: " + p + "\n" +
                        "Length: " + len + "\n" +
                        "Difficulty: " + dif;

        new AlertDialog.Builder(this)
                .setTitle("Confirm Hike")
                .setMessage(msg)
                .setPositiveButton("Confirm", (dialog, which) -> saveToDB())
                .setNegativeButton("Edit", null)
                .show();
    }

    private void saveToDB() {
        boolean inserted = db.insertHike(
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

        if (inserted) {
            Toast.makeText(this, "Hike added!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving hike", Toast.LENGTH_SHORT).show();
        }
    }
}
