package com.coms309.helloworldroundtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.textViewResult);

        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Fetch data from backend
        fetchDataFromBackend();
    }

    private void fetchDataFromBackend() {
        // String url = "http://10.0.2.2:8080/hello";
        // String url = "http://localhost:8080/hello";
        // private static final String URL_JSON_ARRAY = "https://jsonplaceholder.typicode.com/users";
        String url = "https://c758bb57-7968-47fc-920d-901ec040fa57.mock.pstmn.io/hello";

        // Create a request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the response from the server
                        textViewResult.setText(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Display error message
                textViewResult.setText("Failed to retrieve data 1"+error);
            }
        });

        // Add the request to the request queue
        requestQueue.add(stringRequest);
    }
}