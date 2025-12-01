package com.example.mhike;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "mhike.db";
    public static final int DB_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE hikes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "location TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "parking TEXT NOT NULL," +
                "length REAL NOT NULL," +
                "difficulty TEXT NOT NULL," +
                "description TEXT," +
                "runner_name TEXT," +
                "weather TEXT," +
                "image_path TEXT," +
                "optional1 TEXT," +
                "optional2 TEXT)");

        db.execSQL("CREATE TABLE observations (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "hike_id INTEGER NOT NULL," +
                "note TEXT NOT NULL," +
                "time TEXT NOT NULL," +
                "comment TEXT," +
                "FOREIGN KEY(hike_id) REFERENCES hikes(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS hikes");
        db.execSQL("DROP TABLE IF EXISTS observations");
        onCreate(db);
    }

    public boolean insertHike(String name, String location, String date, String parking,
                              double length, String difficulty, String description,
                              String runnerName, String weather, String imagepath,
                              String optional1, String optional2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("location", location);
        cv.put("date", date);
        cv.put("parking", parking);
        cv.put("length", length);
        cv.put("difficulty", difficulty);
        cv.put("description", description);
        cv.put("runner_name", runnerName);
        cv.put("weather", weather);
        cv.put("image_path", imagepath);
        cv.put("optional1", optional1);
        cv.put("optional2", optional2);
        return db.insert("hikes", null, cv) != -1;
    }

    public boolean updateHike(int id, String name, String location, String date, String parking,
                              double length, String difficulty, String description,
                              String runnerName, String weather, String imagepath,
                              String optional1, String optional2) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("location", location);
        cv.put("date", date);
        cv.put("parking", parking);
        cv.put("length", length);
        cv.put("difficulty", difficulty);
        cv.put("description", description);
        cv.put("runner_name", runnerName);
        cv.put("weather", weather);
        cv.put("image_path", imagepath);
        cv.put("optional1", optional1);
        cv.put("optional2", optional2);
        return db.update("hikes", cv, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor getAllHikes() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM hikes", null);
    }

    public Cursor getHikeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM hikes WHERE id=?", new String[]{String.valueOf(id)});
    }

    public boolean deleteHike(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("hikes", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public void deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM hikes");
        db.execSQL("DELETE FROM observations");
    }

    public boolean insertObservation(int hikeId, String note, String time, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("hike_id", hikeId);
        cv.put("note", note);
        cv.put("time", time);
        cv.put("comment", comment);
        return db.insert("observations", null, cv) != -1;
    }

    public Cursor getObservations(int hikeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM observations WHERE hike_id=?", new String[]{String.valueOf(hikeId)});
    }

    public boolean deleteObservation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("observations", "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean updateObservation(int id, String note, String time, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("note", note);
        cv.put("time", time);
        cv.put("comment", comment);
        return db.update("observations", cv, "id=?", new String[]{String.valueOf(id)}) > 0;
    }

    public Cursor searchHikes(String name, String location, String date, String lengthStr) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM hikes WHERE name LIKE ? AND location LIKE ? AND date LIKE ? AND length LIKE ?",
                new String[]{
                        "%" + name + "%",
                        "%" + location + "%",
                        "%" + date + "%",
                        "%" + lengthStr + "%"
                }
        );
    }
}