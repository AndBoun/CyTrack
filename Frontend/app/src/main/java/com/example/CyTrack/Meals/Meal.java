package com.example.CyTrack.Meals;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a Meal with nutritional information.
 */
class Meal implements Serializable {
    /**
     * The unique identifier for the meal.
     */
    private final int ID = 0;

    /**
     * The name of the meal.
     */
    private String name;

    /**
     * The calories in the meal.
     */
    private String calories;

    /**
     * The carbohydrates in the meal.
     */
    private String carbs;

    /**
     * The protein in the meal.
     */
    private String protein;

    /**
     * Constructs a new Meal with the specified attributes.
     *
     * @param name     the name of the meal
     * @param calories the calories in the meal
     * @param carbs    the carbohydrates in the meal
     * @param protein  the protein in the meal
     */
    Meal(String name, String calories, String carbs, String protein) {
        this.calories = calories;
        this.carbs = carbs;
        this.name = name;
        this.protein = protein;
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
    String getCalories() {
        return calories;
    }

    /**
     * Sets the calories in the meal.
     *
     * @param calories the new calories in the meal
     */
    void setCalories(String calories) {
        this.calories = calories;
    }

    /**
     * Returns the carbohydrates in the meal.
     *
     * @return the carbohydrates in the meal
     */
    String getCarbs() {
        return carbs;
    }

    /**
     * Sets the carbohydrates in the meal.
     *
     * @param carbs the new carbohydrates in the meal
     */
    void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    /**
     * Returns the protein in the meal.
     *
     * @return the protein in the meal
     */
    String getProtein() {
        return protein;
    }

    /**
     * Sets the protein in the meal.
     *
     * @param protein the new protein in the meal
     */
    void setProtein(String protein) {
        this.protein = protein;
    }

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