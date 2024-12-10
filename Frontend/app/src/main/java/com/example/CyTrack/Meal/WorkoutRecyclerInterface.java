package com.example.CyTrack.Meal;

/**
 * Interface definition for a callback to be invoked when a workout item is clicked.
 */
public interface WorkoutRecyclerInterface {
    /**
     * Called when a workout item has been clicked.
     *
     * @param position The position of the clicked item in the list.
     */
    void onWorkoutClick(int position);
}