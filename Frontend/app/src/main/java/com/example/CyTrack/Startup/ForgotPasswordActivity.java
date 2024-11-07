package com.example.CyTrack.Startup;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.CyTrack.R;
import com.example.CyTrack.Utilities.FocusUtils;
import com.example.CyTrack.Utilities.NetworkUtils;
import com.example.CyTrack.Utilities.UrlHolder;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {


    private final String URL_RESET_PASSWORD = UrlHolder.URL + "/user/resetPassword";

    private EditText usernameEditText, passwordEditText, passwordAgainEditText;

    private Button resetButton, backButton;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.forgot_password_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.forgotPasswordScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.InputUsername);
        passwordEditText = findViewById(R.id.InputPassword);
        passwordAgainEditText = findViewById(R.id.InputPasswordAgain);
        backButton = findViewById(R.id.backButton);
        resetButton = findViewById(R.id.resetButton);


        resetButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordAgain = passwordAgainEditText.getText().toString();

            if (username.isEmpty() || password.isEmpty() || passwordAgain.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            if (password.equals(passwordAgain)) {
                checkUsernameAndPassword(username, password);
            } else {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            }
        });

        backButton.setOnClickListener(v -> finish());


        clearHintOnFocus();
    }


    private void checkUsernameAndPassword(String username, String password) { //  username must already exist, password must not be the same as old password
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        NetworkUtils.postUserAndGetID(getApplicationContext(), URL_RESET_PASSWORD, params, new NetworkUtils.postUserAndGetIDCallback() {
            @Override
            public void onSuccess(int userID, String message) {
                if (userID != 0) resetPassword(URL_RESET_PASSWORD +  "/" + userID, password);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void resetPassword(String url, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("password", password);

        NetworkUtils.modifyData(getApplicationContext(), url, params, new NetworkUtils.callbackMessage() {

            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), "Password Reset", Toast.LENGTH_LONG).show();
                switchToLogin();
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearHintOnFocus(){
        FocusUtils.clearHintOnFocus(usernameEditText, "Username");
        FocusUtils.clearHintOnFocus(passwordEditText, "Password");
        FocusUtils.clearHintOnFocus(passwordAgainEditText, "Password Again");
    }

    private void switchToLogin(){
        finish();
    }
}
