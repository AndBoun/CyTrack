package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

public class MealSubmitActivity extends AppCompatActivity {

    private EditText MealName, MealCalories, MealProtein, MealCarbs;
    private ImageButton profileSettingsButton, notificationButton, MealsPageButton, LogPageButton;
    private TextView userNameTextView, userStreakTextView;

    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.meal_submit);

        // Initialize Buttons
        // TOP BUTTONS
        profileSettingsButton = findViewById(R.id.profileSettingsButton);
        notificationButton = findViewById(R.id.notificationButton);
        // RIGHT MAIN FUNCTION BUTTONS
        MealsPageButton = findViewById(R.id.MealsPageButton);
        LogPageButton = findViewById(R.id.LogPageButton);

        // USERINPUT TEXTEDIT
        MealName = findViewById(R.id.MealName);
        MealCalories = findViewById(R.id.MealCalories);
        MealProtein = findViewById(R.id.MealProtein);
        MealCalories = findViewById(R.id.MealCalories);
        MealCarbs = findViewById(R.id.MealCarbs);

        // Button Input Listeners
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

        // Button Input Listeners
        profileSettingsButton.setOnClickListener(v -> {
            // Open Profile Settings Activity
        });

        notificationButton.setOnClickListener(v -> {
            // Open Notification Activity
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
