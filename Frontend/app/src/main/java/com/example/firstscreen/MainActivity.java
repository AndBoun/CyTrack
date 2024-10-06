package com.example.firstscreen;

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


public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button signUpButton, loginButton;
    private TextView textGetResponse;

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
            String response;

//            response = NetworkHelper.sendGetRequest("https://f4344d81-63ed-4399-bb8d-9e065b9b9154.mock.pstmn.io//JSONOBJRequest");
            response = NetworkHelper.sendGetRequest("http://10.90.72.246:8080/laptops");

            textGetResponse.setText(response);

            // TODO: Implement actual login verification in helper method
            // IF username and password are correct, go to home screen
        });

        signUpButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

    }

}