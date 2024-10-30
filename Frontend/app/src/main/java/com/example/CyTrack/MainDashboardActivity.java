package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

public class MainDashboardActivity extends AppCompatActivity {

    private ImageButton profileSettingsButton, notificationButton, mealTrackingButton, workOutTrackingButton, exerciseTrackingButton;
    private TextView userNameTextView, userStreakTextView;

    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.main_dashboard_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainDashboardScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        StatusBarUtil.setStatusBarColor(this, R.color.CyRed);

        user = (User) getIntent().getSerializableExtra("user");

        profileSettingsButton = findViewById(R.id.profilePictureButton);
        notificationButton = findViewById(R.id.notificationButton);
        mealTrackingButton = findViewById(R.id.mealTrackingButton);
        workOutTrackingButton = findViewById(R.id.workOutTrackingButton);
        exerciseTrackingButton = findViewById(R.id.exerciseTrackingButton);
        userNameTextView = findViewById(R.id.helloTextView);
        userStreakTextView = findViewById(R.id.streakTextView);

        userNameTextView.setText("Hi, " + user.getFirstName());
        userStreakTextView.setText("Streak: " + user.getStreak());

        profileSettingsButton.setOnClickListener(v -> {
            // Open Profile Settings Activity
            Intent intent = new Intent(this, ProfileSettingsActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        notificationButton.setOnClickListener(v -> {
            // Open Notification Activity
        });

        mealTrackingButton.setOnClickListener(v -> {
            // Open Meal Tracking Activity
            Intent Navigate = new Intent(MainDashboardActivity.this, MealTrackingMain.class);
            startActivity(Navigate);
        });

        workOutTrackingButton.setOnClickListener(v -> {
            // Open Workout Tracking Activity
            Intent intent = new Intent(this, WorkoutActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        Button myProfileButton = findViewById(R.id.myProfileButton);
        myProfileButton.setOnClickListener(v -> {
            // Open My Profile Activity
            Intent intent = new Intent(this, MyProfile.class);
            intent.putExtra("user", user);
            startActivity(intent);
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
