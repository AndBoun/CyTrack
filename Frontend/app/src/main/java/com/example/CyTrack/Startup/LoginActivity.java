package com.example.CyTrack.Startup;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.splashscreen.SplashScreen;

import android.widget.Button;

import android.widget.Toast;

import com.example.CyTrack.Dashboard.MainDashboardActivity;
import com.example.CyTrack.R;
import com.example.CyTrack.Utilities.UrlHolder;
import com.example.CyTrack.Utilities.User;
import com.example.CyTrack.Utilities.FocusUtils;
import com.example.CyTrack.Utilities.NetworkUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * LoginActivity handles the login process for the application.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * EditText field for the username input.
     */
    private EditText usernameEditText;

    /**
     * EditText field for the password input.
     */
    private EditText passwordEditText;

    /**
     * Button to navigate to the sign-up activity.
     */
    private Button signUpButton;

    /**
     * Button to initiate the login process.
     */
    private Button loginButton;

    /**
     * Button to navigate to the forgot password activity.
     */
    private Button forgotPasswordButton;

    /**
     * User object to store the logged-in user's information.
     */
    private User user;

    /**
     * URL for the login endpoint.
     */
    private final String URL_LOGIN = UrlHolder.URL + "/user/login";

    /**
     * URL for fetching user data.
     */
    private final String URL_GET_USER = UrlHolder.URL + "/user";

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.startup_login_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.Username_field);
        passwordEditText = findViewById(R.id.Password_field);
        signUpButton = findViewById(R.id.signUp_button);
        loginButton = findViewById(R.id.login_button);
        forgotPasswordButton = findViewById(R.id.forgotPassword_button);

        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            login(username, password);
        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        forgotPasswordButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        clearHintOnFocus();
    }

    /**
     * Initiates the login process by sending the username and password to the server.
     *
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     */
    private void login(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        NetworkUtils.postUserAndGetID(getApplicationContext(), URL_LOGIN, params, new NetworkUtils.postUserAndGetIDCallback() {
            @Override
            public void onSuccess(int id, String message) {
                if (id != 0) fetchUserData(URL_GET_USER + "/" + id);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Fetches user data from the server using the provided URL.
     *
     * @param url The URL to fetch user data from.
     */
    private void fetchUserData(String url) {
        NetworkUtils.fetchUserData(this, url, new NetworkUtils.fetchUserDataCallback() {
            @Override
            public void onSuccess(User user, String message) {
                LoginActivity.this.user = user;
                Toast.makeText(getApplicationContext(), "Signing In", Toast.LENGTH_LONG).show();
                navigateToMainDashboard();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Navigates to the main dashboard activity.
     */
    private void navigateToMainDashboard() {
        Intent intent = new Intent(LoginActivity.this, MainDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    /**
     * Clears the hint text when the EditText fields gain focus.
     */
    private void clearHintOnFocus() {
        FocusUtils.clearHintOnFocus(usernameEditText, "Username");
        FocusUtils.clearHintOnFocus(passwordEditText, "Password");
    }
}