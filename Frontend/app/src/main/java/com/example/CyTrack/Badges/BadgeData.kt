package com.example.CyTrack.Badges

import android.app.Activity
import com.example.CyTrack.Badges.BadgeObject

/**
 * Object that holds sample badge data.
 */
object BadgeData {
    /**
     * A list of sample badges.
     * Each badge contains an ID, name, and description.
     */
    val BadgeSample = listOf(
        // Badge Name, Badge Desc
        BadgeObject(32, "Bench", "MAX BENCH: 315"),
        BadgeObject(32, "Squat", "MAX SQUAT: 315"),
        BadgeObject(32, "Deadlift", "MAX DEADLIFT: 315"),
        BadgeObject(32, "Clean", "MAX CLEAN: 315")
    )
}