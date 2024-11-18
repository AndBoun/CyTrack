package com.example.CyTrack.Meals;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.CyTrack.Dashboard.MainDashboardActivity;
import com.example.CyTrack.R;

/**
 * Main activity for meal tracking.
 */
public class MealTrackingMain extends AppCompatActivity {


    /**
     * ImageButton for profile settings.
     */
    private ImageButton profileSettingsButton;

    /**
     * ImageButton for notifications.
     */
    private ImageButton notificationButton;

    /**
     * ImageButton for navigating back.
     */
    private ImageButton backbutton;

    /**
     * ImageButton for navigating to the Meals Page.
     */
    private ImageButton MealsPageButton;

    /**
     * ImageButton for navigating to the Log Page.
     */
    private ImageButton LogPageButton;

    /**
     * ImageButton for creating a new meal.
     */
    private ImageButton CreateMealButton;

    /**
     * ImageButton for adding a meal.
     */
    private ImageButton AddMealButton;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.meal_tracking_main);

        // Initialize Buttons
        // TOP BUTTONS
        profileSettingsButton = findViewById(R.id.profileSettingsButton);
        notificationButton = findViewById(R.id.notificationButton);
        backbutton = findViewById(R.id.backbutton);

        // Set click listeners for top buttons
        profileSettingsButton.setOnClickListener(v -> {
            // Open Profile Settings Activity
        });

        notificationButton.setOnClickListener(v -> {
            // Open Notification Activity
        });

        backbutton.setOnClickListener(v -> {
            Intent Navigate = new Intent(MealTrackingMain.this, MainDashboardActivity.class);
            startActivity(Navigate);
        });

        // LEFT MAIN FUNCTION BUTTONS
        CreateMealButton = findViewById(R.id.CreateMealButton);
        AddMealButton = findViewById(R.id.AddMealButton);

        // Set click listeners for left main function buttons
        CreateMealButton.setOnClickListener(v -> {
            //TODO: Display Pop-Up Window
            //TODO: Submit User Data
            Intent Navigate = new Intent(MealTrackingMain.this, MealSubmitActivity.class);
            startActivity(Navigate);
        });

        AddMealButton.setOnClickListener(v -> {
            //TODO: Display Pop-Up Window
        });

        // RIGHT MAIN FUNCTION BUTTONS
        MealsPageButton = findViewById(R.id.MealsPageButton);
        LogPageButton = findViewById(R.id.LogPageButton);

        // Set click listeners for right main function buttons
        MealsPageButton.setOnClickListener(v -> {
            //TODO: Send to MealPage
            // UNNEEDED
        });

        LogPageButton.setOnClickListener(v -> {
            //TODO: Send to LogPage
            Intent Navigate = new Intent(MealTrackingMain.this, MealLogActivity.class);
            startActivity(Navigate);
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
}