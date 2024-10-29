package com.example.CyTrack.Meals;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageButton;
import android.widget.Toast;


import com.example.CyTrack.R;
import com.example.CyTrack.Utilities.FocusUtils;

import java.util.HashMap;
import java.util.Map;

public class MealSubmitActivity extends AppCompatActivity {

    private EditText MealName, MealCalories, MealProtein, MealCarbs;
    private ImageButton profileSettingsButton, notificationButton, MealsPageButton, LogPageButton, SubmitMeal;

    private Meal meal;

    // TODO: Find proper URL to submit hashmap over
    private final String URL = "http://10.90.72.246:8080/meal";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.meal_submit);

        // Initialize Buttons
        profileSettingsButton = findViewById(R.id.profileSettingsButton);
        notificationButton = findViewById(R.id.notificationButton);
        MealsPageButton = findViewById(R.id.MealsPageButton);
        LogPageButton = findViewById(R.id.LogPageButton);
        SubmitMeal = findViewById(R.id.SubmitMeal);

        // USERINPUT TEXTEDIT
        MealName = findViewById(R.id.MealName);
        MealCalories = findViewById(R.id.MealCalories);
        MealProtein = findViewById(R.id.MealProtein);
        MealCalories = findViewById(R.id.MealCalories);
        MealCarbs = findViewById(R.id.MealCarbs);

        // Button Input Listeners
        profileSettingsButton.setOnClickListener(v -> {
            // Open Profile Settings Activity
        });

        notificationButton.setOnClickListener(v -> {
            // Open Notification Activity
        });

        MealsPageButton.setOnClickListener(v -> {
            //TODO: Send to MealPage
            Intent Navigate = new Intent(MealSubmitActivity.this, MealTrackingMain.class);
            startActivity(Navigate);

        });

        LogPageButton.setOnClickListener(v -> {
            //TODO: Send to LogPage
            Intent Navigate = new Intent(MealSubmitActivity.this, MealLogActivity.class);
            startActivity(Navigate);
        });

        SubmitMeal.setOnClickListener(v -> {
            //TODO: Ensure String Data can be sent
            String mealname = MealName.getText().toString();
            String mealcalories = MealCalories.getText().toString();
            String mealprotein = MealProtein.getText().toString();
            String mealcarbs = MealCarbs.getText().toString();
            //TODO: Add Favorite Checking

            if (mealname.isEmpty() || mealcalories.isEmpty() || mealprotein.isEmpty() || mealcarbs.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_LONG).show();
                return;
            }

            mealsubmit(mealname, mealcalories, mealprotein, mealcarbs);
        });

    }

    // MEAL SUBMISSION
    private void mealsubmit(String mealnm, String mealcals, String mealprotein, String mealcarbs) {
        // TODO: Verify if input is done properly
        Integer cals = Integer.parseInt(mealcals);
        Integer prot = Integer.parseInt(mealprotein);
        Integer carb = Integer.parseInt(mealcarbs);
        //TEST
        Map<String, Object> params = new HashMap<>();
        params.put("mealName", mealnm);
        params.put("calories", mealcals);
        params.put("protein", mealprotein);
        params.put("carbs", mealcarbs);
        //Log.d("MyApp", mealprotein);

        //POST
        MealUtils.postMeal(getApplicationContext(), URL, params, new MealUtils.callbackMessage() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(getApplicationContext(), "Submitting Meal", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }});

    }

    private void fetchMealData(String url) {
        MealUtils.fetchMealData(this, url, new MealUtils.fetchMealDataCallback() {
            @Override
            public void onSuccess(Meal meal, String message) {
                MealSubmitActivity.this.meal = meal;
                Toast.makeText(getApplicationContext(), "Submitting Meal", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                Log.d("MyApp", error + " MSA: FetchMealDaata"); // TEST
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearHintOnFocus(){
        FocusUtils.clearHintOnFocus(MealName, "Meal Name");
        FocusUtils.clearHintOnFocus(MealCalories, "Meal Calories");
        FocusUtils.clearHintOnFocus(MealProtein, "Meal Protein");
        FocusUtils.clearHintOnFocus(MealCarbs, "Meal Carbs");
    }

}

