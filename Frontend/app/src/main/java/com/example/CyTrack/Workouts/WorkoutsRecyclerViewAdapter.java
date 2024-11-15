package com.example.CyTrack.Workouts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.CyTrack.R;

import java.util.ArrayList;

/**
 * Adapter class for managing and displaying workout items in a RecyclerView.
 */
public class WorkoutsRecyclerViewAdapter extends RecyclerView.Adapter<WorkoutsRecyclerViewAdapter.ViewHolder> {

    /**
     * Interface for handling item clicks in the RecyclerView.
     */
    private final WorkoutRecyclerInterface workoutRecyclerInterface;

    /**
     * The context in which the adapter is being used.
     */
    private final Context context;

    /**
     * The list of workout objects to be displayed.
     */
    private final ArrayList<WorkoutObject> workoutList;

    /**
     * Constructor for WorkoutsRecyclerViewAdapter.
     *
     * @param context                  The context in which the adapter is being used.
     * @param workoutList              The list of workout objects to be displayed.
     * @param workoutRecyclerInterface The interface for handling item clicks.
     */
    public WorkoutsRecyclerViewAdapter(Context context, ArrayList<WorkoutObject> workoutList, WorkoutRecyclerInterface workoutRecyclerInterface) {
        this.context = context;
        this.workoutList = workoutList;
        this.workoutRecyclerInterface = workoutRecyclerInterface;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public WorkoutsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workouts_recycle_layout, parent, false);
        return new WorkoutsRecyclerViewAdapter.ViewHolder(view, workoutRecyclerInterface);
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull WorkoutsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.exerciseType.setText(workoutList.get(position).getExerciseType());
        holder.duration.setText(workoutList.get(position).getDuration() + "m");
        holder.caloriesBurned.setText("calories burned: " + workoutList.get(position).getCaloriesBurned());
        holder.time.setText(workoutList.get(position).getDate());
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    /**
     * ViewHolder class for holding and recycling views as they are scrolled off screen.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView exerciseType, duration, caloriesBurned, time;

        /**
         * Constructor for ViewHolder.
         *
         * @param itemView                 The view of the item.
         * @param workoutRecyclerInterface The interface for handling item clicks.
         */
        public ViewHolder(@NonNull View itemView, WorkoutRecyclerInterface workoutRecyclerInterface) {
            super(itemView);

            exerciseType = itemView.findViewById(R.id.exerciseType);
            duration = itemView.findViewById(R.id.duration);
            caloriesBurned = itemView.findViewById(R.id.caloriesBurned);
            time = itemView.findViewById(R.id.time);

            itemView.setOnClickListener(v -> {
                if (workoutRecyclerInterface != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        workoutRecyclerInterface.onWorkoutClick(position);
                    }
                }
            });
        }
    }
}
