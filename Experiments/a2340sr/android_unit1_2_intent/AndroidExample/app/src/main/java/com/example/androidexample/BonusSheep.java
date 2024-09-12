package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BonusSheep extends AppCompatActivity{
    private Button backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_sheep);

        backBtn = findViewById(R.id.home_btn);
        Bundle extras = getIntent().getExtras();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BonusSheep.this, MainActivity.class);
                if(extras != null) {
                    String number = extras.getString("NUM");  // this will come from LoginActivity
                    intent.putExtra("NUM", number);
                }
                startActivity(intent);
            }
        });
    }
}
