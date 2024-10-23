package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MealTrackingMain extends AppCompatActivity {

    private ImageButton profileSettingsButton, notificationButton, backbutton, MealsPageButton, LogPageButton, CreateMealButton, AddMealButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.meal_tracking_main);

        // Initialize Buttons
        // TOP BUTTONS
        profileSettingsButton = findViewById(R.id.profileSettingsButton);
        notificationButton = findViewById(R.id.notificationButton);
        backbutton = findViewById(R.id.backbutton);

        // Button Input Listeners
        profileSettingsButton.setOnClickListener(v -> {
            // Open Profile Settings Activity
        });

        notificationButton.setOnClickListener(v -> {
            // Open Notification Activity
        });

        backbutton.setOnClickListener(v -> {
            // @ Ethan
            // You should just use a call to:
            // finished();
            Intent Navigate = new Intent(MealTrackingMain.this, MainDashboardActivity.class);
            startActivity(Navigate);
        });

        // LEFT MAIN FUNCTION BUTTONS
        CreateMealButton = findViewById(R.id.CreateMealButton);
        AddMealButton = findViewById(R.id.AddMealButton);

        // Button Input Listeners
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

        // Button Input Listeners
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
