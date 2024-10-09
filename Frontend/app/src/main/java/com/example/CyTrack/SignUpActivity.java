package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private final String URL = "https://7e68d300-a3cb-4835-bf2f-66cab084d974.mock.pstmn.io/login/"; //TODO should be a different signup URL
//    private final String URL = "http://10.90.72.246:8080/"; //TODO should be a different signup URL
    private Button signUpButton, backButton;
    private EditText usernameEditText, passwordEditText, passwordAgainEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.signup_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUpScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpButton = findViewById(R.id.resetButton);
        usernameEditText = findViewById(R.id.InputUsername);
        passwordEditText = findViewById(R.id.InputPassword);
        passwordAgainEditText = findViewById(R.id.InputPasswordAgain);
        backButton = findViewById(R.id.backButton);

        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordAgain = passwordAgainEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            } else if (!password.equals(passwordAgain)) {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                return;
            }

            signUpUser(username, password);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
        });


        clearHintOnFocus();
    }

    private void signUpUser(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        NetworkUtils.postUserData(getApplicationContext(), URL, params, response -> {
            Toast.makeText(getApplicationContext(), "Signed Up", Toast.LENGTH_LONG).show();
            navigateToLogin();
        }, error -> {
            // Handle error
            Toast.makeText(getApplicationContext(), "Failed to Sign In", Toast.LENGTH_LONG).show();
        });
        Log.d("SignUpActivity", "Request added to queue");
    }

    private void clearHintOnFocus(){
        FocusUtils.clearHintOnFocus(usernameEditText, "Username");
        FocusUtils.clearHintOnFocus(passwordEditText, "Password");
        FocusUtils.clearHintOnFocus(passwordAgainEditText, "Password Again");
    }

    private void navigateToLogin(){
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
