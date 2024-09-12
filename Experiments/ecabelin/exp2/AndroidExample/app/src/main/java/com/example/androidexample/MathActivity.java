package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class MathActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable
    private TextInputLayout prompt;
    private TextInputEditText answer;
    private Button Restart; // define increase button variable
    private Button Configs; // define decrease button variable
    private Button backBtn;     // define back button variable

    // Difficulty Modes
    private boolean gameon = true;
    private int difficulty = 0;

    // Score
    private int counter = 0;    // counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathtime);

        /* initialize UI elements */
        numberTxt = findViewById(R.id.number);
        Restart = findViewById(R.id.restart);

        Configs = findViewById(R.id.configs);
        backBtn = findViewById(R.id.counter_back_btn);

        // Prompt and Answer
        prompt = findViewById(R.id.equation); // Random Prompt
        answer = findViewById(R.id.equationtext); // Input

        /* when restart btn is pressed, reset number textview */
        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(0));
            }
        });

        /* when config btn is pressed, switch to ConfigActivity */
        Configs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameon = false;
                difficulty += 1;
                if (difficulty == 1){
                    Configs.setText("Easy");
                }
                else if (difficulty == 2){
                    Configs.setText("Medium");
                }
                else if (difficulty == 3){
                    Configs.setText("Hard");
                }
                else if (difficulty + 1 > 2){
                    difficulty = 0;
                }

            }
        });

        /* when back btn is pressed, switch back to MainActivity */
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameon = false;
                Intent intent = new Intent(MathActivity.this, MainActivity.class);
                intent.putExtra("NUM", String.valueOf(counter));  // key-value to pass to the MainActivity
                startActivity(intent);
            }
        });

    }
}