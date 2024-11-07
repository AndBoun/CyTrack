package com.example.CyTrack.Dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.CyTrack.R;
import com.example.CyTrack.Startup.LoginActivity;
import com.example.CyTrack.Utilities.UrlHolder;
import com.example.CyTrack.Utilities.User;
import com.example.CyTrack.Utilities.NetworkUtils;

public class ProfileSettingsActivity extends AppCompatActivity {

    private TextView nameTextView;

    private ImageButton deleteButton, backButton, logOutButton;

    private User user;


    private final String DELETE_URL = UrlHolder.URL + "/user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.settings_profile_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.profileSettingsScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        user = (User) getIntent().getSerializableExtra("user");

        nameTextView = findViewById(R.id.nameTextView);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);
        logOutButton = findViewById(R.id.logOutButton);

        nameTextView.setText(user.getFirstName());

        deleteButton.setOnClickListener(v -> {
            deleteUser();
        });

        backButton.setOnClickListener(v -> {
            finish();
        });

        logOutButton.setOnClickListener(v -> {
            switchToLoginActivity();
        });
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void deleteUser() {
        NetworkUtils.deleteRequest(getApplicationContext(), DELETE_URL + "/" + user.getID(), new NetworkUtils.callbackMessage() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                switchToLoginActivity();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
