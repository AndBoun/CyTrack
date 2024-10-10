package com.example.CyTrack;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import java.util.HashMap;
import java.util.Map;

public class MealSubmitActivity extends AppCompatActivity {

    private EditText MealName, MealCalories, MealProtein, MealCarbs;
    private ImageButton profileSettingsButton, notificationButton, MealsPageButton, LogPageButton, SubmitMeal;

    private Meal meal;

    // TODO: Find proper URL to submit hashmap over
    private final String URL = "http://coms-3090-040.class.las.iastate.edu:8082/";

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
        Map<String, String> params = new HashMap<>();
        params.put("name", mealnm);
        params.put("calories", mealcals);
        params.put("protein", mealprotein);
        params.put("carbs", mealcarbs);

        //POST
        MealUtils.postMealAndGetID(getApplicationContext(), URL, params, new MealUtils.postMealAndGetIDCallback() {
            @Override
            public void onSuccess(int id, String message) {
                if (id != 0) fetchMealData(URL + id); // TODO: VERIFY IF URL CORRECT
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });

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

