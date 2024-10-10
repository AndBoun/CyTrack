package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class MealSubmitActivity extends AppCompatActivity {

    private EditText MealName, MealCalories, MealProtein, MealCarbs;
    private ImageButton profileSettingsButton, notificationButton, MealsPageButton, LogPageButton, SubmitMeal;
    private TextView userNameTextView, userStreakTextView;

    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.meal_submit);

        // Initialize Buttons
        profileSettingsButton = findViewById(R.id.profileSettingsButton);
        notificationButton = findViewById(R.id.notificationButton);
        MealsPageButton = findViewById(R.id.MealsPageButton);
        LogPageButton = findViewById(R.id.LogPageButton);
        SubmitMeal = findViewById(R.id.SubmitMeal);

        // USERINPUT TEXTEDIT
        MealName = findViewById(R.id.MealName);
        MealCalories = findViewById(R.id.MealCalories);
        MealProtein = findViewById(R.id.MealProtein);
        MealCalories = findViewById(R.id.MealCalories);
        MealCarbs = findViewById(R.id.MealCarbs);

        // Button Input Listeners
        profileSettingsButton.setOnClickListener(v -> {
            // Open Profile Settings Activity
        });

        notificationButton.setOnClickListener(v -> {
            // Open Notification Activity
        });

        MealsPageButton.setOnClickListener(v -> {
            //TODO: Send to MealPage
            Intent Navigate = new Intent(MealSubmitActivity.this, MealTrackingMain.class);
            startActivity(Navigate);

        });

        LogPageButton.setOnClickListener(v -> {
            //TODO: Send to LogPage
            Intent Navigate = new Intent(MealSubmitActivity.this, MealLogActivity.class);
            startActivity(Navigate);
        });

        SubmitMeal.setOnClickListener(v -> {
            //TODO: Ensure String Data can be sent
            String mealname = MealName.getText().toString();
            String mealcalories = MealCalories.getText().toString();
            String mealprotein = MealProtein.getText().toString();
            String mealcarbs = MealCarbs.getText().toString();
            //TODO: Add Favorite Checking

            if (mealname.isEmpty() || mealcalories.isEmpty() || mealprotein.isEmpty() || mealcarbs.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            mealsubmit(mealname, mealcalories, mealprotein, mealcarbs);
        });



        // Handle back press
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Do nothing to disable back press
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    // MEAL SUBMISSION
    private void mealsubmit(String mealnm, String mealcals, String mealprotein, String mealcarbs) {
        // TODO: Verify if input is done properly
        Map<String, String> params = new HashMap<>();
        params.put("mealName", mealnm);
        params.put("calories", mealcals);
        params.put("protein", mealprotein);
        params.put("carbs", mealcarbs);


        NetworkUtils.postUserAndGetID(getApplicationContext(), URL, params, new NetworkUtils.postUserAndGetIDCallback() {
            @Override
            public void onSuccess(int id) {
                //if (id != 0) fetchUserData(URL + id);
                Toast.makeText(getApplicationContext(), "Meal Logged!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to log Meal!", Toast.LENGTH_LONG).show();
            }
        });
    }
}

