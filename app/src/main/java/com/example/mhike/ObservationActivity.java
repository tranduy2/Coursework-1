package com.example.mhike;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.mhike.models.Observation;
import java.util.ArrayList;

public class ObservationActivity extends AppCompatActivity {
    EditText note, time, comment;
    Button btnAdd;
    ListView listView;
    int hikeId;
    DatabaseHelper db;
    ArrayList<Observation> obsList = new ArrayList<>();
    ObservationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        hikeId = getIntent().getIntExtra("id", -1);
        db = new DatabaseHelper(this);
        note = findViewById(R.id.etNote);
        time = findViewById(R.id.etTime);
        comment = findViewById(R.id.etComment);
        btnAdd = findViewById(R.id.btnAddObs);
        listView = findViewById(R.id.obsListView);
        loadData();
        btnAdd.setOnClickListener(v -> addObservation());
        listView.setOnItemClickListener((parent,
                                         view,
                                         position,
                                         id) -> showOptionsDialog(obsList.get(position)));
    }

    private void loadData() {
        obsList.clear();
        Cursor c = db.getObservations(hikeId);
        while (c.moveToNext()) {
            obsList.add(new Observation(c.getInt(0),
                    c.getInt(1),
                    c.getString(2),
                    c.getString(3),
                    c.getString(4)));
        }
        adapter = new ObservationAdapter(this, obsList);
        listView.setAdapter(adapter);
    }

    private void addObservation() {
        if (note.getText().toString().trim().isEmpty() || time.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Note & Time required!", Toast.LENGTH_SHORT).show();
            return;
        }
        boolean ok = db.insertObservation(hikeId,
                note.getText().toString(),
                time.getText().toString(),
                comment.getText().toString());
        Toast.makeText(this, ok ? "Added!" : "Fail", Toast.LENGTH_SHORT).show();
        loadData();
    }

    private void showOptionsDialog(Observation obs) {
        new AlertDialog.Builder(this).setTitle("Select").setItems(new String[]{"Edit", "Delete"},
                (dialog, which) -> {
            if (which == 0) showEditDialog(obs);
            else deleteObservation(obs.getId());
        }).show();
    }

    private void deleteObservation(int id) {
        db.deleteObservation(id);
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        loadData();
    }

    private void showEditDialog(Observation obs) {
        final EditText etNote = new EditText(this);
        etNote.setText(obs.getNote());
        new AlertDialog.Builder(this).setTitle("Edit Note").setView(etNote).setPositiveButton("Save", (dialog, which) -> {
            db.updateObservation(obs.getId(), etNote.getText().toString(), obs.getTime(), obs.getComment());
            loadData();
        }).setNegativeButton("Cancel", null).show();
    }
}