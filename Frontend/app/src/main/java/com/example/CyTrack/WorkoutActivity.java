package com.example.CyTrack;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkoutActivity extends AppCompatActivity {

    private static final String URL = "http://10.90.72.246:8080/workout";

    ImageButton addWorkoutButton;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.workout_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.workoutScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getSerializableExtra("user");

        ImageButton addWorkoutButton = findViewById(R.id.addWorkoutButton);

        addWorkoutButton.setOnClickListener(v -> showAddWorkoutDialog());
    }


    private void showAddWorkoutDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_workout, null);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Add Workout")
                .setPositiveButton("Add", (dialog, which) -> {
                    // Handle the add button click
                    EditText inputExerciseType = dialogView.findViewById(R.id.inputExerciseType);
                    EditText inputWorkoutDuration = dialogView.findViewById(R.id.inputWorkoutDuration2);
                    EditText inputCalories = dialogView.findViewById(R.id.inputCalories);

                    String exerciseType = inputExerciseType.getText().toString();
                    String duration = inputWorkoutDuration.getText().toString();
                    String calories = inputCalories.getText().toString();
                    String time = getTimeAndDate();


                    // Add your logic to handle the input data
                    postWorkout(exerciseType, duration, calories, time);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private String getTimeAndDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

        // Get the current date and time
        Date date = new Date();

        // Format the date and time into a string
        String timeString = formatter.format(date);

        // Print the time string
        return("Current Time: " + timeString);
    }

    private void postWorkout(String exerciseType, String duration, String calories, String time) {
        Map<String, String> params = new HashMap<>();
        params.put("userID", String.valueOf(user.getID()));
        params.put("exerciseType", exerciseType);
        params.put("duration", duration);
        params.put("calories", calories);
        params.put("time", time);

        NetworkUtils.postData(this, URL, params, new NetworkUtils.callbackMessage() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(WorkoutActivity.this, "Workout Added", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(String message) {
                Toast.makeText(WorkoutActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
