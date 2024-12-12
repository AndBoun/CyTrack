package com.example.CyTrack.Dashboard;

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
import androidx.core.view.WindowInsetsCompat;

import com.example.CyTrack.Meal.MyMeals;
import com.example.CyTrack.R;
import com.example.CyTrack.Social.MyProfile;
import com.example.CyTrack.Leaderboard.TimeEntryBoard.LeaderboardActivity;
import com.example.CyTrack.Statistics.MyStats;
import com.example.CyTrack.Utilities.User;
import com.example.CyTrack.Utilities.StatusBarUtil;
import com.example.CyTrack.Workouts.MyWorkouts;

/**
 * MainDashboardActivity is the main screen of the application where users can navigate
 * to different sections such as Profile Settings, Notifications, Meal Tracking, Workout Tracking,
 * Exercise Tracking, and Leaderboard.
 */
public class MainDashboardActivity extends AppCompatActivity {

    /**
     * ImageButton for navigating to Profile Settings
     */
    private ImageButton profileSettingsButton;

    /**
     * ImageButton for opening Notifications
     */
    private ImageButton notificationButton;

    /**
     * ImageButton for navigating to Meal Tracking
     */
    private ImageButton mealTrackingButton;

    /**
     * ImageButton for navigating to Workout Tracking
     */
    private ImageButton workOutTrackingButton;

    /**
     * ImageButton for navigating to Leaderboard
     */
    private ImageButton leaderboardButton;

    /**
     * ImageButton for navigating to Stats
     */
    private ImageButton StatsButton;
    /**
     * TextView to display the user's name
     */
    private TextView userNameTextView;

    /**
     * TextView to display the user's streak
     */
    private TextView userStreakTextView;

    /**
     * User object to hold the current user's data
     */
    private User user;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
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
        leaderboardButton = findViewById(R.id.LBbutton);
        StatsButton = findViewById(R.id.Statsbutton);

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
            Intent Navigate = new Intent(MainDashboardActivity.this, MyMeals.class);
            Navigate.putExtra("user", user);
            startActivity(Navigate);
        });

        workOutTrackingButton.setOnClickListener(v -> {
            // Open Workout Tracking Activity
            Intent intent = new Intent(this, MyWorkouts.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        leaderboardButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LeaderboardActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        StatsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyStats.class);
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