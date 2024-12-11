package com.example.CyTrack.Meal;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a Meal with nutritional information.
 */
public class MealEntry implements Serializable {
    /**
     * The unique identifier for the meal.
     */
    private int ID;

    /**
     * The name of the meal.
     */
    private String name;

    /**
     * The calories in the meal.
     */
    private int calories;

    /**
     * The carbohydrates in the meal.
     */
    private int carbs;

    /**
     * The protein in the meal.
     */
    private int protein;

    /**
     * The time of the meal.
     */
    private String time;

    /**
     * The date of the meal.
     */
    private String date;

    /**
     * Constructs a new Meal with the specified attributes.
     *
     * @param name     the name of the meal
     * @param calories the calories in the meal
     * @param carbs    the carbohydrates in the meal
     * @param protein  the protein in the meal
     */
    MealEntry(int ID, String name, int calories, int carbs, int protein, String time, String date) {
        this.calories = calories;
        this.carbs = carbs;
        this.name = name;
        this.protein = protein;
        this.time = time;
        this.date = date;
        this.ID = ID;
    }


    /**
     * Returns the ID of the meal.
     *
     * @return the ID of the meal
     */
    int getID() {
        return ID;
    }

    /**
     * Returns the name of the meal.
     *
     * @return the name of the meal
     */
    String getName() {
        return name;
    }

    /**
     * Sets the name of the meal.
     *
     * @param name the new name of the meal
     */
    void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the calories in the meal.
     *
     * @return the calories in the meal
     */
    int getCalories() {
        return calories;
    }

    /**
     * Sets the calories in the meal.
     *
     * @param calories the new calories in the meal
     */
    void setCalories(int calories) {
        this.calories = calories;
    }

    /**
     * Returns the carbohydrates in the meal.
     *
     * @return the carbohydrates in the meal
     */
    int getCarbs() {
        return carbs;
    }

    /**
     * Sets the carbohydrates in the meal.
     *
     * @param carbs the new carbohydrates in the meal
     */
    void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    /**
     * Returns the protein in the meal.
     *
     * @return the protein in the meal
     */
    int getProtein() {
        return protein;
    }

    /**
     * Sets the protein in the meal.
     *
     * @param protein the new protein in the meal
     */
    void setProtein(int protein) {
        this.protein = protein;
    }

    /**
     * Returns the time in the meal.
     *
     * @return the time in the meal
     */
    String getTime() {
        return time;
    }

    /**
     * Sets the time in the meal.
     *
     * @param time the new time in the meal
     */
    void setTime(String time) { this.time = time; }

    /**
     * Returns the date in the meal.
     *
     * @return the date in the meal
     */
    String getDate() {
        return date;
    }

    /**
     * Sets the date in the meal.
     *
     * @param date the new date in the meal
     */
    void setDate(String date) { this.time = date; }

    /**
     * Returns a string representation of the meal.
     *
     * @return a string representation of the meal
     */
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