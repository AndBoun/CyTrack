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

/**
 * Activity for submitting meal information.
 */
public class MealSubmitActivity extends AppCompatActivity {

    /**
     * EditText field for meal name input.
     */
    private EditText MealName;

    /**
     * EditText field for meal calories input.
     */
    private EditText MealCalories;

    /**
     * EditText field for meal protein input.
     */
    private EditText MealProtein;

    /**
     * EditText field for meal carbohydrates input.
     */
    private EditText MealCarbs;

    /**
     * ImageButton for profile settings.
     */
    private ImageButton profileSettingsButton;

    /**
     * ImageButton for notifications.
     */
    private ImageButton notificationButton;

    /**
     * ImageButton to navigate to Meals Page.
     */
    private ImageButton MealsPageButton;

    /**
     * ImageButton to navigate to Log Page.
     */
    private ImageButton LogPageButton;

    /**
     * ImageButton to submit the meal.
     */
    private ImageButton SubmitMeal;

    /**
     * Meal object to hold meal data.
     */
    private Meal meal;

    /**
     * URL to submit meal data.
     * TODO: Find proper URL to submit hashmap over
     */
    private final String URL = "http://10.90.72.246:8080/meal";

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
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

    /**
     * Submits the meal data to the server.
     *
     * @param mealnm      The name of the meal.
     * @param mealcals    The calories of the meal.
     * @param mealprotein The protein content of the meal.
     * @param mealcarbs   The carbohydrate content of the meal.
     */
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
            }
        });

    }

    /**
     * Fetches meal data from the server.
     *
     * @param url The URL to fetch the meal data from.
     */
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

    /**
     * Clears the hint text on focus for the meal input fields.
     */
    private void clearHintOnFocus() {
        FocusUtils.clearHintOnFocus(MealName, "Meal Name");
        FocusUtils.clearHintOnFocus(MealCalories, "Meal Calories");
        FocusUtils.clearHintOnFocus(MealProtein, "Meal Protein");
        FocusUtils.clearHintOnFocus(MealCarbs, "Meal Carbs");
    }

}