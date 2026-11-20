package com.example.fitforge.data.remote.dto.exercise;

import java.util.List;

public class ExerciseUpdateRequest {
    private String name;
    private String description;
    private String primary_muscle;
    private List<String> secondary_muscles;
    private String equipment;
    private String exercise_type;
    private String instructions;

    public ExerciseUpdateRequest(String name, String primary_muscle, String equipment, String exercise_type) {
        this.name = name;
        this.primary_muscle = primary_muscle;
        this.equipment = equipment;
        this.exercise_type = exercise_type;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrimaryMuscle() { return primary_muscle; }
    public List<String> getSecondaryMuscles() { return secondary_muscles; }
    public String getEquipment() { return equipment; }
    public String getExerciseType() { return exercise_type; }
    public String getInstructions() { return instructions; }
}