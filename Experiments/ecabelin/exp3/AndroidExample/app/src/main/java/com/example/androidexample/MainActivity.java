package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {
    private LinearLayoutCompat back;
    private TextView messageText;   // define message textview variable
    private TextView usernameText;  // define username textview variable
    private Button loginButton;     // define login button variable
    private Button signupButton;    // define signup button variable
    // Dark Mode Switcher
    private Switch togg;
    private boolean swit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        usernameText = findViewById(R.id.main_username_txt);// link to username textview in the Main activity XML
        loginButton = findViewById(R.id.main_login_btn);    // link to login button in the Main activity XML
        signupButton = findViewById(R.id.main_signup_btn);  // link to signup button in the Main activity XML

        // Toggle Button
        togg = findViewById(R.id.modetoggle);
        // Background
        back = findViewById(R.id.wallpaper);

        /* extract data passed into this activity from another activity */
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            messageText.setText("Home Page");
            usernameText.setVisibility(View.INVISIBLE);             // set username text invisible initially
        } else {
            messageText.setText("Welcome");
            usernameText.setText(extras.getString("USERNAME")); // this will come from LoginActivity
            loginButton.setVisibility(View.INVISIBLE);              // set login button invisible
            signupButton.setVisibility(View.INVISIBLE);             // set signup button invisible
        }

        togg.setOnCheckedChangeListener((buttonView, isChecked) ->{
            if (isChecked) {
                swit = true;
                //back.setVisibility(View.INVISIBLE);
                messageText.setTextColor(Color.BLACK);      // link to message textview in the Main activity XML
                usernameText.setTextColor(Color.BLACK); // link to username textview in the Main activity XML
                loginButton.setTextColor(Color.BLACK);    // link to login button in the Main activity XML
                signupButton.setTextColor(Color.BLACK);
            }
            else {
                swit = false;
                //back.setVisibility(View.VISIBLE);
                messageText.setTextColor(Color.GREEN);      // link to message textview in the Main activity XML
                usernameText.setTextColor(Color.GREEN); // link to username textview in the Main activity XML
                loginButton.setTextColor(Color.GREEN);    // link to login button in the Main activity XML
                signupButton.setTextColor(Color.GREEN);
            }

        });

        /* click listener on login button pressed */
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when login button is pressed, use intent to switch to Login Activity */
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("Dark mode", swit);
                startActivity(intent);
            }
        });

        /* click listener on signup button pressed */
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /* when signup button is pressed, use intent to switch to Signup Activity */
                Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                intent.putExtra("Dark mode", swit);
                startActivity(intent);
            }
        });
    }
}