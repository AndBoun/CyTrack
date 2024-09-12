package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class MathActivity extends AppCompatActivity {

    private TextView numberTxt; // define number textview variable

    private Button Restart; // define increase button variable
    private Button Submit; // define decrease button variable

    private Button backBtn;     // define back button variable

    private TextView prompt;
    private TextView answer;

    // Difficulty Modes
    private boolean gameon = true;
    private int difficulty = 2;

    // Score
    private int counter = 0;    // counter variable

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mathtime);

        // Game Trackers
        numberTxt = findViewById(R.id.number);
        Restart = findViewById(R.id.restart);

        // Config Btn for Difficulty Settings
        Submit = findViewById(R.id.submit);

        // Main Menu Button
        backBtn = findViewById(R.id.counter_back_btn);

        // Prompt and Answer
        prompt = findViewById(R.id.equation); // Random Prompt
        answer = findViewById(R.id.equationtext); // Input - Password


        // Random Sign
        Random rand = new Random();

        /* when restart btn is pressed, reset number textview */
        Restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberTxt.setText(String.valueOf(0));
                counter = 0;
                /* when config btn is pressed, switch to ConfigActivity */
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Game
                int sign = rand.nextInt(difficulty);

                // Operand Decider
                int solution = 0;
                String operand = "";
                int first = rand.nextInt(100);
                int last = rand.nextInt(100);

                if (sign == 0) {
                    operand = "+";
                    solution = first + last;
                }
                else if (sign == 1) {
                    operand = "*";
                    solution = first * last;
                }

                // Prompt Change
                String prompttext = String.valueOf(first) + operand + String.valueOf(last);
                prompt.setText(prompttext);

                // Solution Change - (Problem with code Here)
                String userinput = answer.getText().toString();
                //Test: numberTxt.setText(String.valueOf(solution));
                //Test: prompt.setText(userinput);
                if (String.valueOf(solution).equals(userinput)){
                    counter+=1;
                    numberTxt.setText(String.valueOf(counter));
                    //Test: numberTxt.setText(counter);
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