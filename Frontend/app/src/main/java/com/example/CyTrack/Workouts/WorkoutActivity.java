package com.example.CyTrack.Workouts;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.CyTrack.Utilities.NetworkUtils;
import com.example.CyTrack.R;
import com.example.CyTrack.Utilities.UrlHolder;
import com.example.CyTrack.Utilities.User;
import com.example.CyTrack.Utilities.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Activity to manage and display workouts.
 */
public class WorkoutActivity extends AppCompatActivity implements WorkoutRecyclerInterface {

    /**
     * List to store workout objects.
     */
    private final ArrayList<WorkoutObject> workoutList = new ArrayList<>();

    /**
     * The user object representing the current user.
     */
    private User user;

    /**
     * URL for workout-related API endpoints.
     */
    private String URL = UrlHolder.URL + "/workout/";

    /**
     * Button to add a new workout.
     */
    ImageButton addWorkoutButton;

    /**
     * Button to navigate back.
     */
    ImageButton backButton;

    /**
     * RecyclerView to display the list of workouts.
     */
    private RecyclerView recyclerView;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.workouts_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.workoutScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getSerializableExtra("user");
        assert user != null;
        URL += user.getID() + "/workout";

        recyclerView = findViewById(R.id.recyclerView);
        WorkoutsRecyclerViewAdapter adapter = new WorkoutsRecyclerViewAdapter(this, workoutList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageButton addWorkoutButton = findViewById(R.id.addWorkoutButton);
        ImageButton backButton = findViewById(R.id.backButton);

        getWorkouts();

        addWorkoutButton.setOnClickListener(v -> showAddWorkoutDialog());

        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Handles the click event on a workout item.
     *
     * @param position The position of the clicked workout item.
     */
    @Override
    public void onWorkoutClick(int position) {
        WorkoutObject workout = workoutList.get(position);

        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.workouts_modify_workout_dialog, null);

        // Populate the dialog with workout data
        EditText inputExerciseType = dialogView.findViewById(R.id.inputExerciseType);
        EditText inputWorkoutDuration = dialogView.findViewById(R.id.inputWorkoutDuration);
        EditText inputCalories = dialogView.findViewById(R.id.inputCalories);
        EditText inputDate = dialogView.findViewById(R.id.inputDate);

        inputExerciseType.setText(workout.getExerciseType());
        inputWorkoutDuration.setText(workout.getDuration());
        inputCalories.setText(workout.getCaloriesBurned());
        inputDate.setText(workout.getDate());

        // Create the AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Modify Workout")
                .create();

        dialog.show();

        // Handle the modify button click
        dialogView.findViewById(R.id.modifyButton).setOnClickListener(v -> {
            workout.setExerciseType(inputExerciseType.getText().toString());
            workout.setDuration(inputWorkoutDuration.getText().toString());
            workout.setCaloriesBurned(inputCalories.getText().toString());
            workout.setDate(inputDate.getText().toString());

            modifyWorkout(workout.getWorkoutID(),
                    workout.getExerciseType(),
                    workout.getDuration(),
                    workout.getCaloriesBurned(),
                    workout.getDate(),
                    position,
                    workout);
            dialog.dismiss();
        });

        // Handle the delete button click
        dialogView.findViewById(R.id.deleteButton).setOnClickListener(v -> {
            deleteWorkout(workout.getWorkoutID(), position);
            dialog.dismiss();
        });

        // Handle the cancel button click
        dialogView.findViewById(R.id.cancelButton).setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    /**
     * Shows a dialog to add a new workout.
     */
    private void showAddWorkoutDialog() {
        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.workouts_add_workout_dialog, null);

        // Create the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setTitle("Add Workout")
                .setPositiveButton("Add", (dialog, which) -> {
                    // Handle the add button click
                    EditText inputExerciseType = dialogView.findViewById(R.id.inputExerciseType);
                    EditText inputWorkoutDuration = dialogView.findViewById(R.id.inputWorkoutDuration2);
                    EditText inputCalories = dialogView.findViewById(R.id.inputCalories);

                    if (inputExerciseType.getText().toString().isEmpty() ||
                            inputWorkoutDuration.getText().toString().isEmpty() ||
                            inputCalories.getText().toString().isEmpty()) {
                        Toast.makeText(WorkoutActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String exerciseType = inputExerciseType.getText().toString();
                    String duration = inputWorkoutDuration.getText().toString();
                    String calories = inputCalories.getText().toString();
                    String date = getCurrentDate();

                    postWorkout(exerciseType, duration, calories, date);
                    dialog.dismiss();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    /**
     * Gets the current date in MM-dd-yyyy format.
     *
     * @return The current date as a string.
     */
    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");
        Date date = new Date();
        return formatter.format(date);
    }

    /**
     * Fetches the workouts from the server.
     */
    private void getWorkouts() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                Log.d("WorkoutActivity", "URL: " + URL);

                JSONArray workouts = response.getJSONObject("data").getJSONArray("workouts");
                for (int i = 0; i < workouts.length(); i++) {
                    JSONObject workout = workouts.getJSONObject(i);
                    workoutList.add(new WorkoutObject(workout.getString("exerciseType"),
                            workout.getString("duration"),
                            workout.getString("calories"),
                            workout.getString("date"),
                            workout.getInt("workoutID")));
                    Objects.requireNonNull(recyclerView.getAdapter()).notifyItemInserted(workoutList.size() - 1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(WorkoutActivity.this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Posts a new workout to the server.
     *
     * @param exerciseType The type of exercise.
     * @param duration     The duration of the workout.
     * @param calories     The calories burned during the workout.
     * @param date         The date of the workout.
     */
    private void postWorkout(String exerciseType, String duration, String calories, String date) {
        Map<String, String> params = new HashMap<>();
        params.put("userID", String.valueOf(user.getID()));
        params.put("exerciseType", exerciseType);
        params.put("duration", duration);
        params.put("calories", calories);
        params.put("date", date);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, new JSONObject(params), response -> {
            try {
                JSONObject data = response.getJSONObject("data");
                int workoutID = data.getInt("workoutID");
                workoutList.add(new WorkoutObject(exerciseType, duration, calories, date, workoutID));
                Objects.requireNonNull(recyclerView.getAdapter()).notifyItemInserted(workoutList.size() - 1);
                Toast.makeText(this, "Workout added", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(WorkoutActivity.this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Deletes a workout from the server.
     *
     * @param workoutID The ID of the workout to delete.
     * @param position  The position of the workout in the list.
     */
    private void deleteWorkout(int workoutID, int position) {
        String deleteURL = URL + "/" + workoutID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteURL, null, response -> {
            workoutList.remove(position);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemRemoved(position);
            Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(WorkoutActivity.this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Modifies an existing workout on the server.
     *
     * @param workoutID    The ID of the workout to modify.
     * @param exerciseType The new type of exercise.
     * @param duration     The new duration of the workout.
     * @param calories     The new calories burned during the workout.
     * @param date         The new date of the workout.
     * @param position     The position of the workout in the list.
     * @param workout      The workout object to update.
     */
    private void modifyWorkout(int workoutID, String exerciseType, String duration, String calories, String date, int position, WorkoutObject workout) {
        String modifyURL = URL + "/" + workoutID;
        Map<String, String> params = new HashMap<>();
        params.put("exerciseType", exerciseType);
        params.put("duration", duration);
        params.put("calories", calories);
        params.put("date", date);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, modifyURL, new JSONObject(params), response -> {
            workoutList.set(position, workout);
            Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(position);
            Toast.makeText(this, "Workout modified", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(WorkoutActivity.this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
