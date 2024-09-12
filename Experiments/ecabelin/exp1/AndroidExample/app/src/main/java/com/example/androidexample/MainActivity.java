package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Random;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView messageText;   // define message textview variable
    private TextView buttonText;
    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);             // link to Main activity XML

        /* initialize UI elements */

        // Title Message
        messageText = findViewById(R.id.main_msg_txt);      // link to message textview in the Main activity XML
        messageText.setText("Don't let the counter go down to 0!");

        // Button Message
        buttonText = findViewById(R.id.main_button_txt);
        button1 = findViewById(R.id.main_button_txt);

        CountDownTimer counter = new CountDownTimer(30000, 1000){
            public long count;

            public void onTick(long millisUntilFinished) {
                count = millisUntilFinished;
                buttonText.setText("seconds remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                buttonText.setText("done!");
                this.cancel();
            }

            public long getCount() {
                return count;
            }

            public void setCount(long count) {
                this.count = count;
            }
        };

        counter.start();

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                counter
            }
        });





    }
}
