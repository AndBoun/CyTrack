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

    private User user;

//    private final String URL_LOGIN = "https://e8d89384-93d7-4dee-b704-f1103033e07d.mock.pstmn.io/user/login";
//    private final String URL_GET_USER = "https://7e68d300-a3cb-4835-bf2f-66cab084d974.mock.pstmn.io/user";

    private final String URL_LOGIN = "http://10.90.72.246:8080/user/login";
    private final String URL_GET_USER = "http://10.90.72.246:8080/user";

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