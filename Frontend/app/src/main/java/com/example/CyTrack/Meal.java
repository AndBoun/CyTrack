package com.example.CyTrack;

import androidx.annotation.NonNull;

import java.io.Serializable;

class Meal implements Serializable {
    private final int ID = 0;

    private String name;

    private String calories;

    private String carbs;

    private String protein;

    Meal(String name, String calories, String carbs, String protein) {
        this.calories = calories;
        this.carbs = carbs;
        this.name = name;
        this.protein = protein;
    }
//TODO: GET AND SET MEAL ATTRIBUTES

    int getID() {
        return ID;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getCalories() {return calories;}

    void setCalories(String calories) {
        this.calories = calories;
    }

    String getCarbs() {return carbs;}

    void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    String getProtein() {return protein;}

    void setProtein(String protein) {
        this.protein = protein;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", calories=" + calories +
                ", carbs='" + carbs + '\'' +
                ", protein=" + protein +
                '}';
    }
}
