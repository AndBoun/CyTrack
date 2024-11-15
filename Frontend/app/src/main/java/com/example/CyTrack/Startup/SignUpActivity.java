package com.example.CyTrack.Startup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

/**
 * Activity for user sign-up.
 */
public class SignUpActivity extends AppCompatActivity {

    /**
     * URL for user sign-up endpoint.
     */
    private final String URL = UrlHolder.URL + "/user";

    /**
     * Button for sign-up action.
     */
    private Button signUpButton;

    /**
     * Button to go back to the previous screen.
     */
    private Button backButton;

    /**
     * EditText for entering the username.
     */
    private EditText usernameEditText;

    /**
     * EditText for entering the password.
     */
    private EditText passwordEditText;

    /**
     * EditText for confirming the password.
     */
    private EditText passwordAgainEditText;

    /**
     * EditText for entering the first name.
     */
    private EditText firstNameEditText;

    /**
     * EditText for entering the last name.
     */
    private EditText lastNameEditText;

    /**
     * EditText for entering the email.
     */
    private EditText emailEditText;

    /**
     * EditText for entering the phone number.
     */
    private EditText phoneEditText;

    /**
     * EditText for entering the age.
     */
    private EditText ageEditText;

    /**
     * Selected gender of the user.
     */
    private String gender;

    /**
     * Spinner for selecting the gender.
     */
    private Spinner genderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.startup_signup_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.signUpScreen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signUpButton = findViewById(R.id.resetButton);
        usernameEditText = findViewById(R.id.InputUsername);
        passwordEditText = findViewById(R.id.InputPassword);
        passwordAgainEditText = findViewById(R.id.InputPasswordAgain);
        firstNameEditText = findViewById(R.id.inputFirstName);
        lastNameEditText = findViewById(R.id.inputLastName);
        ageEditText = findViewById(R.id.inputAge);
        backButton = findViewById(R.id.backButton);

        genderSpinner = findViewById(R.id.genderSpinner);
        String[] genders = new String[]{"M", "F"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        genderSpinner.setAdapter(adapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = genders[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                gender = null;
            }
        });

        signUpButton.setOnClickListener(v -> {
            String username = usernameEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordAgain = passwordAgainEditText.getText().toString();
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String age = ageEditText.getText().toString();
            String gender = genderSpinner.getSelectedItem().toString();

            if (!validateFields()) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            } else if (!validatePassword()) {
                Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
                return;
            }

            signUpUser(username, password, firstName, lastName, age, gender);
        });

        backButton.setOnClickListener(v -> {
            finish();
        });

        clearHintOnFocus();
    }

    /**
     * Sends sign-up data to the server.
     *
     * @param username  the username
     * @param password  the password
     * @param firstName the first name
     * @param lastName  the last name
     * @param age       the age
     * @param gender    the gender
     */
    private void signUpUser(String username, String password, String firstName, String lastName, String age, String gender) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        params.put("firstName", firstName);
        params.put("lastName", lastName);
        params.put("age", age);
        params.put("gender", gender);

        NetworkUtils.postData(getApplicationContext(), URL, params, new NetworkUtils.callbackMessage() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), "Signed Up", Toast.LENGTH_LONG).show();
                navigateToLogin();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });

        Log.d("SignUpActivity", "Request added to queue");
    }

    /**
     * Clears hint text when EditText fields gain focus.
     */
    private void clearHintOnFocus() {
        FocusUtils.clearHintOnFocus(usernameEditText, "Username");
        FocusUtils.clearHintOnFocus(passwordEditText, "Password");
        FocusUtils.clearHintOnFocus(passwordAgainEditText, "Password Again");
    }

    /**
     * Navigates to the login activity.
     */
    private void navigateToLogin() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Validates if the password and confirm password fields match.
     *
     * @return true if passwords match, false otherwise
     */
    private boolean validatePassword() {
        return passwordEditText.getText().toString().equals(passwordAgainEditText.getText().toString());
    }

    /**
     * Validates if all required fields are filled.
     *
     * @return true if all fields are filled, false otherwise
     */
    private boolean validateFields() {
        return !usernameEditText.getText().toString().isEmpty() &&
                !passwordEditText.getText().toString().isEmpty() &&
                !passwordAgainEditText.getText().toString().isEmpty() &&
                !firstNameEditText.getText().toString().isEmpty() &&
                !lastNameEditText.getText().toString().isEmpty() &&
                !ageEditText.getText().toString().isEmpty()
                && gender != null;
    }
}