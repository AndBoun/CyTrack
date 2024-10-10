package com.example.CyTrack;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MealLogActivity extends AppCompatActivity {

    private ImageButton profileSettingsButton, notificationButton, MealsPageButton, LogPageButton;
    private ScrollView MealTable;
    private LinearLayout MealTableDisplay;
    private ArrayList<Meal> meals = new ArrayList<Meal>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.meal_log);

        // Initialize Buttons
        // TOP BUTTONS
        profileSettingsButton = findViewById(R.id.profileSettingsButton);
        notificationButton = findViewById(R.id.notificationButton);
        // RIGHT MAIN FUNCTION BUTTONS
        MealsPageButton = findViewById(R.id.MealsPageButton);
        LogPageButton = findViewById(R.id.LogPageButton);
        // Initialize Meal Table
        MealTable = findViewById(R.id.MealScroll);
        MealTableDisplay = findViewById(R.id.MealScrollDisplay);

        // Button Input Listeners
        profileSettingsButton.setOnClickListener(v -> {
            // Open Profile Settings Activity
        });

        notificationButton.setOnClickListener(v -> {
            // Open Notification Activity
        });

        MealsPageButton.setOnClickListener(v -> {
            //TODO: Send to MealPage
            Intent Navigate = new Intent(MealLogActivity.this, MealTrackingMain.class);
            startActivity(Navigate);

        });

        LogPageButton.setOnClickListener(v -> {
            //TODO: Send to LogPage
            //NOT NEEDED
        });

        // APPENDING MEALS
        for (int i = 0; i < 20; i++){
            Meal input = new Meal(12, "Alex", "23", "42", "22");
            TableLayout tableLayout = new TableLayout(this);
            tableLayout.setLayoutParams(new TableLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            if (i == 0){
                addTableRow(tableLayout, 0, "|Meal ID|", "|Calories|", "|Carbs|", "|Meal Name|", "|Protein|");
            }
            else {
                String id = String.valueOf(input.getID());
                id = String.format("MID:  %s    ", id);
                String name = input.getName();
                name = String.format("   %s,", name);
                String calories = input.getCalories();
                calories = String.format("   %s cals,", calories);
                String carbs = input.getCarbs();
                carbs = String.format("   %s g,", carbs);
                String protein = input.getProtein();
                protein = String.format("   %s g", protein);
                addTableRow(tableLayout, i, id, calories, carbs, name, protein);
            }
            MealTableDisplay.addView(tableLayout);
        }


    }

    private void addTableRow(TableLayout tableLayout, int m, String... values) {
        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        for (String value : values) {
            TextView textView = new TextView(this);
            textView.setText(value);
            textView.setPadding(4, 4, 4, 4);
            textView.setTextSize(10);
            tableRow.addView(textView);
            if (m == 0){
                tableRow.setBackgroundColor(Color.parseColor("#C8102E"));
                textView.setTextColor(Color.parseColor("#FFFFFF"));

            }
            if (m % 2 == 1){
                tableRow.setBackgroundColor(Color.parseColor("#f0e1b9"));
            }
        }


        tableLayout.addView(tableRow);
    }

    private void fetchMealData(String url) {
        MealUtils.fetchMealData(this, url, new MealUtils.fetchMealDataCallback() {
            @Override
            public void onSuccess(Meal meal, String message) {
                meals.add(meal);
                Toast.makeText(getApplicationContext(), "Meal Loaded", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
            }
        });
    }

}
