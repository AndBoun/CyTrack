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
import androidx.compose.ui.platform.ComposeView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WorkoutActivity extends AppCompatActivity implements WorkoutRecyclerInterface {

    private ArrayList<WorkoutObject> workoutList = new ArrayList<>();

    private static final String URL = "https://7e68d300-a3cb-4835-bf2f-66cab084d974.mock.pstmn.io/workouts";

//    private static final String URL = "http://10.90.72.246:8080/workouts";

    ImageButton addWorkoutButton, backButton;

    private RecyclerView recyclerView;

    private ComposeView composeView;


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

        ComposeView composeView = findViewById(R.id.composeView);
        aTemp temp = new aTemp();
        temp.setComposeViewContent(this, composeView);


        recyclerView = findViewById(R.id.recyclerView);

        user = (User) getIntent().getSerializableExtra("user");

        getWorkouts();

        ImageButton addWorkoutButton = findViewById(R.id.addWorkoutButton);
        ImageButton backButton = findViewById(R.id.backButton);

        addWorkoutButton.setOnClickListener(v -> showAddWorkoutDialog());

        backButton.setOnClickListener(v -> finish());

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


                    if(inputExerciseType.getText().toString().isEmpty() || inputWorkoutDuration.getText().toString().isEmpty() || inputCalories.getText().toString().isEmpty()){
                        Toast.makeText(WorkoutActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

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


    private void getWorkouts(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                JSONArray workouts = response.getJSONObject("data").getJSONArray("workouts");
                for (int i = 0; i < workouts.length(); i++) {
                    JSONObject workout = workouts.getJSONObject(i);
                    workoutList.add(new WorkoutObject(workout.getString("exerciseType"),
                            workout.getString("duration"),
                            workout.getString("calories"),
                            workout.getString("time"),
                            workout.getInt("workoutID")));
                }
//                WorkoutsRecyclerViewAdapter adapter = new WorkoutsRecyclerViewAdapter(this, workoutList, this);
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(this));


                //Toast.makeText(this, "Workouts fetched" + workoutList.size(), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(WorkoutActivity.this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL + "/" + user.getID(), new JSONObject(params), response -> {
            try {
                JSONObject data = response.getJSONObject("data");
                int workoutID = data.getInt("workoutID");
                workoutList.add(new WorkoutObject(exerciseType, duration, calories, time, workoutID));
                recyclerView.getAdapter().notifyItemInserted(workoutList.size() - 1);
                Toast.makeText(this, "Workout added", Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(WorkoutActivity.this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onWorkoutClick(int position) {
        WorkoutObject workout = workoutList.get(position);

        // Inflate the dialog layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_modify_workout, null);

        // Populate the dialog with workout data
        EditText inputExerciseType = dialogView.findViewById(R.id.inputExerciseType);
        EditText inputWorkoutDuration = dialogView.findViewById(R.id.inputWorkoutDuration);
        EditText inputCalories = dialogView.findViewById(R.id.inputCalories);
        EditText inputTime = dialogView.findViewById(R.id.inputTime);

        inputExerciseType.setText(workout.getExerciseType());
        inputWorkoutDuration.setText(workout.getDuration());
        inputCalories.setText(workout.getCaloriesBurned());
        inputTime.setText(workout.getTime());

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
            workout.setTime(inputTime.getText().toString());
            // Update the workout in the list and notify the adapter
//            workoutList.set(position, workout);
//            recyclerView.getAdapter().notifyItemChanged(position);
            // Dismiss the dialog
            modifyWorkout(workout.getWorkoutID(),
                    workout.getExerciseType(),
                    workout.getDuration(),
                    workout.getCaloriesBurned(),
                    workout.getTime(),
                    position,
                    workout);
            dialog.dismiss();
        });

        // Handle the delete button click
        dialogView.findViewById(R.id.deleteButton).setOnClickListener(v -> {
            // Remove the workout from the list and notify the adapter
//            workoutList.remove(position);
//            recyclerView.getAdapter().notifyItemRemoved(position);
            // Dismiss the dialog
            deleteWorkout(workout.getWorkoutID(), position);
            dialog.dismiss();
        });

        // Handle the cancel button click
        dialogView.findViewById(R.id.cancelButton).setOnClickListener(v -> {
            // Dismiss the dialog
            dialog.dismiss();
        });
    }

    private void deleteWorkout(int workoutID, int position) {
        String deleteURL = URL + "/" + workoutID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteURL, null, response -> {
            workoutList.remove(position);
            recyclerView.getAdapter().notifyItemRemoved(position);
            Toast.makeText(this, "Workout deleted", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(WorkoutActivity.this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private void modifyWorkout(int workoutID, String exerciseType, String duration, String calories, String time, int position, WorkoutObject workout) {
        String modifyURL = URL + "/" + workoutID;
        Map<String, String> params = new HashMap<>();
        params.put("exerciseType", exerciseType);
        params.put("duration", duration);
        params.put("calories", calories);
        params.put("time", time);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, modifyURL, new JSONObject(params), response -> {
            workoutList.set(position, workout);
            recyclerView.getAdapter().notifyItemChanged(position);
            Toast.makeText(this, "Workout modified", Toast.LENGTH_SHORT).show();
        }, error -> {
            Toast.makeText(WorkoutActivity.this, NetworkUtils.errorResponse(error), Toast.LENGTH_SHORT).show();
        });
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
