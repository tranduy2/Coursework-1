package com.example.mhike.models;

public class Hike {
    private int id;
    private String name;
    private String location;
    private String date;
    private String parking;
    private double length;
    private String difficulty;
    private String description;
    private String optional1;
    private String optional2;

    public Hike(int id, String name, String location, String date, String parking,
                double length, String difficulty, String description,
                String optional1, String optional2) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parking = parking;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
        this.optional1 = optional1;
        this.optional2 = optional2;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getParking() { return parking; }
    public double getLength() { return length; }
    public String getDifficulty() { return difficulty; }
    public String getDescription() { return description; }
    public String getOptional1() { return optional1; }
    public String getOptional2() { return optional2; }
}
