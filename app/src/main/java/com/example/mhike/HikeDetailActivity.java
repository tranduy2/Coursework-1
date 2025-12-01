package com.example.mhike;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

public class HikeDetailActivity extends AppCompatActivity {

    EditText name, location, date, length, description, runner, weather;
    RadioGroup rgParking, rgDifficulty;
    RadioButton rbYes, rbNo, rbEasy, rbMedium, rbHard;
    Button btnUpdate, btnDelete, btnObs, btnTakeNew, btnSelectNew;
    ImageView ivDetailImage;

    DatabaseHelper db;
    int hikeId;
    String currentImagePath = "";
    Uri photoURI;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    currentImagePath = uri.toString();
                    Glide.with(this).load(uri).centerCrop().into(ivDetailImage);
                }
            });

    ActivityResultLauncher<Uri> takePictureLauncher = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            success -> {
                if (success) {
                    currentImagePath = photoURI.toString();
                    Glide.with(this).load(photoURI).centerCrop().into(ivDetailImage);
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
        setContentView(R.layout.activity_hike_detail);

        db = new DatabaseHelper(this);
        hikeId = getIntent().getIntExtra("id", -1);

        name = findViewById(R.id.etName2);
        location = findViewById(R.id.etLocation2);
        runner = findViewById(R.id.etRunner2);
        date = findViewById(R.id.etDate2);
        length = findViewById(R.id.etLength2);
        description = findViewById(R.id.etDescription2);
        weather = findViewById(R.id.etWeather2);

        rgParking = findViewById(R.id.rgParking2);
        rgDifficulty = findViewById(R.id.rgDifficulty2);

        rbYes = findViewById(R.id.rbYes2);
        rbNo = findViewById(R.id.rbNo2);
        rbEasy = findViewById(R.id.rbEasy2);
        rbMedium = findViewById(R.id.rbMedium2);
        rbHard = findViewById(R.id.rbHard2);

        ivDetailImage = findViewById(R.id.ivDetailImage);
        btnTakeNew = findViewById(R.id.btnTakeNewPhoto);
        btnSelectNew = findViewById(R.id.btnSelectNewImage);

        btnUpdate = findViewById(R.id.btnUpdateHike);
        btnDelete = findViewById(R.id.btnDeleteHike);
        btnObs = findViewById(R.id.btnViewObs);

        loadHike();

        date.setOnClickListener(v -> showDatePicker());
        btnUpdate.setOnClickListener(v -> updateHike());
        btnDelete.setOnClickListener(v -> deleteHike());

        btnSelectNew.setOnClickListener(v -> mGetContent.launch("image/*"));
        btnTakeNew.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });

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

            String parkingVal = c.getString(4);
            if (parkingVal != null && parkingVal.equals("Yes")) rbYes.setChecked(true);
            else rbNo.setChecked(true);

            length.setText(String.valueOf(c.getDouble(5)));

            String diffVal = c.getString(6);
            if (diffVal != null) {
                if (diffVal.equals("Easy")) rbEasy.setChecked(true);
                else if (diffVal.equals("Medium")) rbMedium.setChecked(true);
                else rbHard.setChecked(true);
            }

            description.setText(c.getString(7));

            int runnerIndex = c.getColumnIndex("runner_name");
            if (runnerIndex != -1) runner.setText(c.getString(runnerIndex));

            int weatherIndex = c.getColumnIndex("weather");
            if(weatherIndex != -1) weather.setText(c.getString(weatherIndex));

            int imageIndex = c.getColumnIndex("image_path");
            if (imageIndex != -1) {
                currentImagePath = c.getString(imageIndex);
                if (currentImagePath != null && !currentImagePath.isEmpty()) {
                    try {
                        Glide.with(this).load(Uri.parse(currentImagePath)).centerCrop().into(ivDetailImage);
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }
        }
        c.close();
    }

    private void updateHike() {
        RadioButton rbP = findViewById(rgParking.getCheckedRadioButtonId());
        String parkingVal = rbP.getText().toString();

        RadioButton rbD = findViewById(rgDifficulty.getCheckedRadioButtonId());
        String diffVal = rbD.getText().toString();

        String runnerName = runner.getText().toString().trim();
        String w = weather.getText().toString().trim();

        boolean updated = db.updateHike(
                hikeId,
                name.getText().toString(),
                location.getText().toString(),
                date.getText().toString(),
                parkingVal,
                Double.parseDouble(length.getText().toString()),
                diffVal,
                description.getText().toString(),
                runnerName,
                w,
                currentImagePath,
                "", ""
        );

        if (updated) {
            Toast.makeText(this, "Hike updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteHike() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this hike?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    db.deleteHike(hikeId);
                    Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(this, (view, y, m, d) ->
                date.setText(d + "/" + (m + 1) + "/" + y), year, month, day).show();
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
}