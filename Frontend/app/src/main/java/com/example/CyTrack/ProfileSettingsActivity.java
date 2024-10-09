package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ProfileSettingsActivity extends AppCompatActivity {

    private TextView nameTextView;

    private ImageButton deleteButton, backButton;

    private User user;

    private final String URL = "https://7e68d300-a3cb-4835-bf2f-66cab084d974.mock.pstmn.io/login/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.profile_settings_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profileSettingsScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getSerializableExtra("user");

        nameTextView = findViewById(R.id.nameTextView);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        nameTextView.setText(user.getName());

        deleteButton.setOnClickListener(v -> {
            deleteUser();
        });

        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void deleteUser() {
        NetworkUtils.deleteRequest(getApplicationContext(), URL + user.getID(), response -> {
            Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
            switchToLoginActivity();
        }, error -> {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(), "Account Could Not Be Deleted", Toast.LENGTH_SHORT).show();
        });
    }

}
