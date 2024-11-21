package com.coms309.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText number1, number2;
    Button addButton, multiplyButton;
    TextView resultText;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number1 = findViewById(R.id.number1);
        number2 = findViewById(R.id.number2);
        addButton = findViewById(R.id.add_button);
        multiplyButton = findViewById(R.id.multiply_button);
        resultText = findViewById(R.id.result_text);
        requestQueue = Volley.newRequestQueue(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateNumbers("add");
            }
        });

        multiplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                operateNumbers("multiply");
            }
        });
    }

    public double add(double a, double b) {
        return a + b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public void operateNumbers(String operation) {
        String num1 = number1.getText().toString();
        String num2 = number2.getText().toString();
        String url = "http://10.0.2.2:8081/" + operation + "?num1=" + num1 + "&num2=" + num2;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            resultText.setText("Result: " + jsonObject.getInt("result"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            resultText.setText("Error parsing JSON");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultText.setText("Error: " + error.toString());
            }
        });

        requestQueue.add(stringRequest);
    }
}