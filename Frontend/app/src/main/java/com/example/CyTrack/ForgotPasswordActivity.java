package com.example.CyTrack;

import android.os.Bundle;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    private final String URL = "https://7e68d300-a3cb-4835-bf2f-66cab084d974.mock.pstmn.io/login/"; //TODO should be a different signup URL

    private EditText usernameEditText, passwordEditText, passwordAgainEditText;

    private Button resetButton, backButton;

    private int id;


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
                if (id != 0) {
                    resetPassword(URL + id, password);
                }
            } else {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            }
        });

        backButton.setOnClickListener(v -> finish());

    }

    private void checkUsernameAndPassword(String username, String password) { //  username must already exist, password must not be the same as old password
        Map<String, String> params = new HashMap<>();
        params.put("username", username);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest checkRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                response -> {
                    try {
                        id = response.getInt("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    //Toast.makeText(getApplicationContext(), "Password Reset in Progress", Toast.LENGTH_LONG).show();
                },
                error -> Toast.makeText(getApplicationContext(), "Failed to Reset Password", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(checkRequest);
    }

    private void resetPassword(String URLid, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("password", password);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest resetRequest = new JsonObjectRequest(Request.Method.PUT, URLid, jsonObject,
                response -> {
                    Toast.makeText(getApplicationContext(), "Password Reset", Toast.LENGTH_LONG).show();
                },
                error -> Toast.makeText(getApplicationContext(), "Failed to Reset Password", Toast.LENGTH_LONG).show()) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(resetRequest);
    }
}
