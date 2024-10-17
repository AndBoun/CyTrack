package com.example.CyTrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WorkoutsRecyclerViewAdapter extends RecyclerView.Adapter<WorkoutsRecyclerViewAdapter.ViewHolder> {

    private final WorkoutRecyclerInterface workoutRecyclerInterface;
    private Context context;

    private ArrayList<WorkoutObject> workoutList;

    public WorkoutsRecyclerViewAdapter(Context context, ArrayList<WorkoutObject> workoutList, WorkoutRecyclerInterface workoutRecyclerInterface) {
        this.context = context;
        this.workoutList = workoutList;
        this.workoutRecyclerInterface = workoutRecyclerInterface;
    }

    @NonNull
    @Override
    public WorkoutsRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.workout_recycle_layout, parent, false);
        return new WorkoutsRecyclerViewAdapter.ViewHolder(view, workoutRecyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutsRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.exerciseType.setText(workoutList.get(position).getExerciseType());
        holder.duration.setText(workoutList.get(position).getDuration() + "m");
        holder.caloriesBurned.setText("calories burned: " + workoutList.get(position).getCaloriesBurned());
        holder.time.setText("date: " + workoutList.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView exerciseType, duration, caloriesBurned, time;
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
