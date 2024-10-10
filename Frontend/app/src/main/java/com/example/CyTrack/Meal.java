package com.example.CyTrack;

import androidx.annotation.NonNull;

import java.io.Serializable;

class Meal implements Serializable {
    private final int ID;

    private String name;

    private int calories;

    private int carbs;

    private int protein;

    Meal(int ID, String name, int calories, int carbs, int protein) {
        this.ID = ID;
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

    int getCalories() {return calories;}

    void setCalories(int calories) {
        this.calories = calories;
    }

    int getCarbs() {return carbs;}

    void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    int getProtein() {return protein;}

    void setProtein(int protein) {
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
