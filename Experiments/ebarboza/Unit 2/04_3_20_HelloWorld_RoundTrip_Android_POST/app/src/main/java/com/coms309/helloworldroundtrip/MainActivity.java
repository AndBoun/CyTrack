package com.coms309.helloworldroundtrip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextInput;
    private Button buttonSend;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextInput = findViewById(R.id.editTextInput);
        buttonSend = findViewById(R.id.buttonSend);



        // Initialize Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // button send
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = editTextInput.getText().toString();
                // Send data to Backend
                sendDataToBackend(inputText);
            }
        });
    }

    private void sendDataToBackend(final String inputText){
//        String url = "http://10.0.2.2:8080/api/endpoint";
        String url = "https://e36a532d-5a6a-454b-8726-9deb1b79de99.mock.pstmn.io";

        // Create a new request using volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle the response from the server
                        Toast.makeText(MainActivity.this, "Data sent successfully" + response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(MainActivity.this, "Error sending data", Toast.LENGTH_SHORT).show();
                    }
                }) {
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("mydata",inputText); // Sending the input data as "data" parameter
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");
                return headers;
            }

        };

        // Add the request to the RequestQueue
        requestQueue.add(stringRequest);

    }

//    private void fetchDataFromBackend() {
//         String url = "http://10.0.2.2:8080/hello";
//
//        // Create a request
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        // Display the response from the server
//                        textViewResult.setText(response);
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                // Display error message
//                textViewResult.setText("Failed to retrieve data 1"+error);
//            }
//        });
//
//        // Add the request to the request queue
//        requestQueue.add(stringRequest);
//    }
}