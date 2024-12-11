package com.example.CyTrack.Meal;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a Meal with nutritional information.
 */
public class NutrientSummary implements Serializable {
    /**
     * The calories in the created summary.
     */
    private int calories;

    /**
     * The carbohydrates in the created summary.
     */
    private int carbs;

    /**
     * The protein in the created summary.
     */
    private int protein;

    /**
     * The date of the created summary.
     */
    private String time;

    /**
     * The date of the created summary.
     */
    private String date;

    /**
     * Constructs a new Nutrient Summary with the specified attributes.
     *
     * @param calories the calories in the summary.
     * @param carbs    the carbohydrates in the summary.
     * @param protein  the protein in the summary.
     */
    NutrientSummary(int calories, int carbs, int protein, String time, String date) {
        this.calories = calories;
        this.carbs = carbs;
        this.protein = protein;
        this.date = date;
    }

    /**
     * Returns the calories in the summary.
     *
     * @return the calories in the summary.
     */
    int getCalories() {
        return calories;
    }

    /**
     * Sets the calories in the summary.
     *
     * @param calories the new calories in the summary.
     */
    void setCalories(int calories) {
        this.calories = calories;
    }

    /**
     * Returns the carbohydrates in the summary.
     *
     * @return the carbohydrates in the summary.
     */
    int getCarbs() {
        return carbs;
    }

    /**
     * Sets the carbohydrates in the summary.
     *
     * @param carbs the new carbohydrates in the summary.
     */
    void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    /**
     * Returns the protein in the summary.
     *
     * @return the protein in the summary.
     */
    int getProtein() {
        return protein;
    }

    /**
     * Sets the protein in the summary.
     *
     * @param protein the new protein in the summary.
     */
    void setProtein(int protein) {
        this.protein = protein;
    }

    /**
     * Returns the time of the summary.
     *
     * @return the time of the summary.
     */
    String getTime() {
        return time;
    }

    /**
     * Sets the Time in the summary.
     *
     * @param time the new time in the summary.
     */
    void setTime(String time) {this.time = time; }

    /**
     * Returns the time of the summary.
     *
     * @return the time of the summary.
     */
    String getDate() {
        return date;
    }

    /**
     * Sets the Date in the summary.
     *
     * @param date the new date in the summary.
     */
    void setDate(String date) {this.date = date; }
    /**
     * Returns a string representation of the summary..
     *
     * @return a string representation of the summary.
     */
    @NonNull
    @Override
    public String toString() {
        return "Summary{" +
                ", calories=" + calories +
                ", carbs='" + carbs + '\'' +
                ", protein=" + protein +
                ", time=" + time +
                ", date=" + date +
                '}';
    }
}