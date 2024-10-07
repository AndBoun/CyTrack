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

    private int id;

    private final String URL = "https://7e68d300-a3cb-4835-bf2f-66cab084d974.mock.pstmn.io/login/";
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


        loginButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();


            postUserData(username, password);
            if (id != 0) fetchUserData(URL + id);
        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

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


    }

    private void postUserData(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);

        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject,
                response -> {
                    // Handle response
                    try {
                        id = response.getInt("id");
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    Toast.makeText(getApplicationContext(), "Signing In", Toast.LENGTH_LONG).show();
                },
                error -> {
                    // Handle error
                    Toast.makeText(getApplicationContext(), "Failed to Sign In", Toast.LENGTH_LONG).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private void fetchUserData(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        User user = new User(
                                response.getInt("id"),
                                response.getString("name"),
                                response.getInt("age"),
                                response.getString("gender"),
                                response.getInt("streak")
                        );

                        // Display the values in the TextView
                        textGetResponse.setText(user.toString());

                        //Toast.makeText(getApplicationContext(), "Fetching User Data", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        textGetResponse.setText("JSON Parsing Error");
                    }
                },
                error -> {
                    textGetResponse.setText("ERROR");
                    Toast.makeText(getApplicationContext(), "Failed to Fetch Data", Toast.LENGTH_LONG).show();
                }){
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }
}