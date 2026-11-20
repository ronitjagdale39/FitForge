package com.example.fitforge.data.remote.dto.exercise;

import java.util.List;

public class Exercise {
    private int id;
    private String name;
    private String description;
    private String primary_muscle;
    private List<String> secondary_muscles;
    private String equipment;
    private String exercise_type;
    private boolean is_active;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrimaryMuscle() { return primary_muscle; }
    public List<String> getSecondaryMuscles() { return secondary_muscles; }
    public String getEquipment() { return equipment; }
    public String getExerciseType() { return exercise_type; }
    public boolean isActive() { return is_active; }
}