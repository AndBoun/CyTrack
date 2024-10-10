package com.coms309.rountrip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    EditText inputFirstName, inputLastName, inputEmail;
    TextView textGetResponse;
    Button buttonGet, buttonPost;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inputFirstName = findViewById(R.id.input_first_name);
        inputLastName = findViewById(R.id.input_last_name);
        inputEmail = findViewById(R.id.input_email);
        textGetResponse = findViewById(R.id.text_get_response);
        buttonGet = findViewById(R.id.button_get);
        buttonPost = findViewById(R.id.button_post);


        // Initialize the ExecutorService with a single thread pool
        executorService = Executors.newSingleThreadExecutor();


        // Button to send GET request
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        sendGetRequest("http://10.0.2.2:8080/mytestapi");
                    }
                });
            }
        });


        // Button to send POST request
        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = inputFirstName.getText().toString();
                String lastName = inputLastName.getText().toString();
                String email = inputEmail.getText().toString();


                // Create JSON object for POST request
                JSONObject json = new JSONObject();
                try {
                    json.put("id", 13); // Example static ID
                    json.put("firstName", firstName);
                    json.put("lastName", lastName);
                    json.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        sendPostRequest("http://10.0.2.2:8080/students", json.toString());
                    }
                });
            }
        });
    }


    // Method to send GET Request
    private void sendGetRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");


            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }


            in.close();
            urlConnection.disconnect();


            String result = content.toString();
            Log.d("GET RESPONSE", result); // Log the response for debugging


            // Update UI on the main thread
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    textGetResponse.setText(result);
                }
            });


        } catch (Exception e) {
            Log.e("GET ERROR", e.getMessage(), e); // Log any errors
            e.printStackTrace();
        }
    }

    // Method to send POST Request
    private void sendPostRequest(String urlString, String jsonData) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setDoOutput(true);


            // Write JSON data to the output stream
            OutputStream os = conn.getOutputStream();
            os.write(jsonData.getBytes("UTF-8"));
            os.flush();
            os.close();


            // Get the response
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                sb.append(output);
            }


            conn.disconnect();
            String result = sb.toString();


            // Optionally handle the response from the POST request
            Log.d("POST RESPONSE", result);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Shutdown the executor service when activity is destroyed
        executorService.shutdown();
    }



}