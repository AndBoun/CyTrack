package com.example.CyTrack.Meal

import android.app.Activity
import com.example.CyTrack.Badges.BadgeObject

/**
 * Object that holds sample badge data.
 */
object MealData {
    /**
     * A list of sample badges.
     * Each badge contains an ID, name, and description.
     */
    val MealSample = listOf(
        // Meal Name, Calories(Int), Carbs (Int), Protein (Int), Time (String), Date (String)
        MealEntry(1,"Steak",1500,32,215,"5:39", "4/22/24"),
        MealEntry(2,"Apple",123,23,0,"9:32", "4/23/24"),
        MealEntry(3,"Banana",120,32,12,"10:23", "4/24/24"),
    )
}