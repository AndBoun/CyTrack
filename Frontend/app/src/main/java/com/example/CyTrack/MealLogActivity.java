package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class MealLogActivity extends AppCompatActivity {

    private ImageButton profileSettingsButton, notificationButton, MealsPageButton, LogPageButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.meal_log);

        // Initialize Buttons
        // TOP BUTTONS
        profileSettingsButton = findViewById(R.id.profileSettingsButton);
        notificationButton = findViewById(R.id.notificationButton);

        // Button Input Listeners
        profileSettingsButton.setOnClickListener(v -> {
            // Open Profile Settings Activity
        });

        notificationButton.setOnClickListener(v -> {
            // Open Notification Activity
        });

        // RIGHT MAIN FUNCTION BUTTONS
        MealsPageButton = findViewById(R.id.MealsPageButton);
        LogPageButton = findViewById(R.id.LogPageButton);

        // Button Input Listeners
        MealsPageButton.setOnClickListener(v -> {
            //TODO: Send to MealPage
            Intent Navigate = new Intent(MealLogActivity.this, MealTrackingMain.class);
            startActivity(Navigate);

        });

        LogPageButton.setOnClickListener(v -> {
            //TODO: Send to LogPage
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
