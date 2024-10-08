package com.example.CyTrack;

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
import android.widget.TextView;

import android.widget.Toast;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button signUpButton, loginButton, forgotPasswordButton;
    private TextView textGetResponse;

    private User user;

    private final String URL = "https://7e68d300-a3cb-4835-bf2f-66cab084d974.mock.pstmn.io/login/";
//    private final String URL = "http://10.90.72.246:8080/laptops";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.login_activity);
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
        textGetResponse = findViewById(R.id.text_get_response);


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

    private void login(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        NetworkUtils.postUserAndGetID(getApplicationContext(), URL, params, new NetworkUtils.postUserAndGetIDCallback() {
            @Override
            public void onSuccess(int id) {
                if (id != 0) fetchUserData(URL + id);
                Toast.makeText(getApplicationContext(), "Signing In", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to Sign In", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fetchUserData(String url) {
        NetworkUtils.fetchUserData(this, url, new NetworkUtils.fetchUserDataCallback() {
            @Override
            public void onSuccess(User user) {
                LoginActivity.this.user = user;
                textGetResponse.setText(user.toString());
                navigateToMainDashboard();
            }

            @Override
            public void onError(Exception e) {
                textGetResponse.setText("ERROR");
                Toast.makeText(getApplicationContext(), "Failed to Fetch Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void navigateToMainDashboard(){
        Intent intent = new Intent(LoginActivity.this, MainDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void clearHintOnFocus(){
        FocusUtils.clearHintOnFocus(usernameEditText, "Username");
        FocusUtils.clearHintOnFocus(passwordEditText, "Password");
    }
}