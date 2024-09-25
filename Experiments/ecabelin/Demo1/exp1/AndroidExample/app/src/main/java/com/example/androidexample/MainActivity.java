package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Time;
import java.util.Random;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView buttonText;
    private Button button1;
    private ProgressBar bar;

    // Upgrade Variables
    private Button UpGR;
    private Button ImprovePress;

    // Base Value Setup
    private int counter = 0;
    private int max = 500;
    private int value = 100;
    private int UpCost = 400;
    private int PassCost = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */

        // Title Message
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        messageText.setText("Press The Button!");

        // Button Setup
        buttonText = findViewById(R.id.main_button_txt);
        button1 = findViewById(R.id.main_button_txt);
        UpGR = findViewById(R.id.Storage);
        ImprovePress = findViewById(R.id.Passive);

        // Progress Bar
        bar = findViewById(R.id.progressBar);

        // Counter Setup
        buttonText.setText(String.valueOf(counter));
        UpGR.setText("Upgrade Max? Cost: " + String.valueOf(UpCost));
        ImprovePress.setText("Upgrade Presses? Cost: " + String.valueOf(PassCost));

        bar.setProgress(counter);
        bar.setMax(max);

        // Iteration Setup
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if ((counter + value) <= max){
                    counter += value;
                }
                if ((counter + value) > max){
                    counter = max;
                }

                bar.setProgress(counter);
                buttonText.setText(String.valueOf(counter));
                bar.setMax(max);
            }
        });

        // Storage Upgrade
        UpGR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (counter >= UpCost){
                    counter -= UpCost;
                    max += 200;
                    UpCost += 100;
                }
                bar.setProgress(counter);
                buttonText.setText(String.valueOf(counter));
                UpGR.setText("Upgrade Max? Cost: " + String.valueOf(UpCost));
                bar.setMax(max);
            }
        });

        // Press Upgrade
        ImprovePress.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                if (counter >= PassCost){
                    counter -= PassCost;
                    value += 25;
                    PassCost += 500;
                }
                bar.setProgress(counter);
                buttonText.setText(String.valueOf(counter));
                ImprovePress.setText("Upgrade Presses? Cost: " + String.valueOf(PassCost));
                bar.setMax(max);
            }
        });




    }
}
