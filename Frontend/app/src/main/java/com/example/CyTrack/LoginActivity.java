package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button signUpButton, loginButton;
    private TextView textGetResponse;

    private final String URL = "https://f4344d81-63ed-4399-bb8d-9e065b9b9154.mock.pstmn.io//JSONOBJRequest";
//    private final String URL = "http://10.90.72.246:8080/laptops";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        usernameEditText = findViewById(R.id.Username_field);
        passwordEditText = findViewById(R.id.Password_field);
        signUpButton = findViewById(R.id.signUp_button);
        loginButton = findViewById(R.id.login_button);
        textGetResponse = findViewById(R.id.text_get_response);


        usernameEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                usernameEditText.setHint("");
            } else {
                usernameEditText.setHint("Username");
            }
        });

        passwordEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                passwordEditText.setHint("");
            } else {
                passwordEditText.setHint("Password");
            }
        });

        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        loginButton.setOnClickListener(v -> {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    response -> {
                        textGetResponse.setText(response);
                        Toast.makeText(getApplicationContext(), "Signing in", Toast.LENGTH_LONG).show();
                    },
                    error -> {
                        textGetResponse.setText("ERROR");
                        Toast.makeText(getApplicationContext(), "Failed to Sign in", Toast.LENGTH_LONG).show();
                    });
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

    }
}