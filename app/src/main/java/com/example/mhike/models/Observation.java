package com.example.mhike.models;

public class Observation {
    private int id;
    private int hikeId;
    private String note;
    private String time;
    private String comment;

    public Observation(int id, int hikeId, String note, String time, String comment) {
        this.id = id;
        this.hikeId = hikeId;
        this.note = note;
        this.time = time;
        this.comment = comment;
    }

    public int getId() { return id; }
    public int getHikeId() { return hikeId; }
    public String getNote() { return note; }
    public String getTime() { return time; }
    public String getComment() { return comment; }
}
