package com.example.fitforge.data.remote.dto.nutrition;

public class Food {
    private int id;
    private String name;
    private String category;
    private float calories;
    private float protein;
    private float carbohydrates;
    private float fats;
    private float fibre;
    private String base_unit;
    private String source;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public float getCalories() { return calories; }
    public float getProtein() { return protein; }
    public float getCarbohydrates() { return carbohydrates; }
    public float getFats() { return fats; }
    public float getFibre() { return fibre; }
    public String getBaseUnit() { return base_unit; }
    public String getSource() { return source; }
}