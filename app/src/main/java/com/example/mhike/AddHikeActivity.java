package com.example.mhike;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddHikeActivity extends AppCompatActivity {

    EditText name, location, date, length, description, weather, runner;
    RadioGroup rgParking, rgDifficulty;
    Button btnSave, btnSelectImage, btnTakePhoto;
    ImageView ivHikeImage;
    String selectedImagePath = "";
    Uri photoURI;

    DatabaseHelper db;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    selectedImagePath = uri.toString();
                    Glide.with(this).load(uri).centerCrop().into(ivHikeImage);
                }
            });

    ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success) {
                    selectedImagePath = photoURI.toString();
                    Glide.with(this).load(photoURI).centerCrop().into(ivHikeImage);
                }
            }
    );

    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) openCamera();
                else Toast.makeText(this, "Camera permission required!", Toast.LENGTH_SHORT).show();
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);

        db = new DatabaseHelper(this);

        name = findViewById(R.id.etName);
        location = findViewById(R.id.etLocation);
        date = findViewById(R.id.etDate);
        length = findViewById(R.id.etLength);
        description = findViewById(R.id.etDescription);
        runner = findViewById(R.id.etRunner);
        weather = findViewById(R.id.etWeather);

        rgParking = findViewById(R.id.rgParking);
        rgDifficulty = findViewById(R.id.rgDifficulty);

        ivHikeImage = findViewById(R.id.ivHikeImage);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnTakePhoto = findViewById(R.id.btnTakePhoto);
        btnSave = findViewById(R.id.btnAddHike);

        date.setOnClickListener(v -> showDatePicker());
        btnSave.setOnClickListener(v -> validateAndConfirm());

        btnSelectImage.setOnClickListener(v -> mGetContent.launch("image/*"));

        btnTakePhoto.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });
    }

    private void openCamera() {
        try {
            File photoFile = createImageFile();
            photoURI = FileProvider.getUriForFile(this, "com.example.mhike.fileprovider", photoFile);
            takePictureLauncher.launch(photoURI);
        } catch (IOException ex) {
            Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalCacheDir();
        return File.createTempFile("JPEG_" + timeStamp + "_", ".jpg", storageDir);
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (view, y, m, d) ->
                date.setText(d + "/" + (m + 1) + "/" + y), year, month, day).show();
    }

    private void validateAndConfirm() {
        String n = name.getText().toString().trim();
        String l = location.getText().toString().trim();
        String d = date.getText().toString().trim();
        String len = length.getText().toString().trim();
        String w = weather.getText().toString().trim();
        String r = runner.getText().toString().trim();

        int selectedParkingId = rgParking.getCheckedRadioButtonId();
        int selectedDiffId = rgDifficulty.getCheckedRadioButtonId();

        if (selectedParkingId == -1 || selectedDiffId == -1) {
            Toast.makeText(this, "Please select Parking and Difficulty", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rbParking = findViewById(selectedParkingId);
        String p = rbParking.getText().toString();

        RadioButton rbDiff = findViewById(selectedDiffId);
        String dif = rbDiff.getText().toString();

        if (n.isEmpty() || l.isEmpty() || d.isEmpty() || len.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields (*)", Toast.LENGTH_SHORT).show();
            return;
        }

        String msg = "Name: " + n + "\nLocation: " + l + "\nDate: " + d +
                "\nRunner: " + r + "\nWeather: " + w +
                "\nParking: " + p + "\nLength: " + len + "\nDifficulty: " + dif +
                "\nImage: " + (selectedImagePath.isEmpty() ? "No" : "Yes");

        new AlertDialog.Builder(this)
                .setTitle("Confirm Hike Details")
                .setMessage(msg)
                .setPositiveButton("Confirm", (dialog, which) ->
                        saveToDB(n, l, d, p, len, dif, r, w, selectedImagePath)
                )
                .setNegativeButton("Edit", null)
                .show();
    }

    private void saveToDB(String n, String l, String d, String p, String len, String dif, String r, String w, String img) {
        boolean inserted = db.insertHike(
                n, l, d, p, Double.parseDouble(len), dif,
                description.getText().toString().trim(),
                r, w, img, "", ""
        );

        if (inserted) {
            Toast.makeText(this, "Hike added successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error saving hike", Toast.LENGTH_SHORT).show();
        }
    }
}