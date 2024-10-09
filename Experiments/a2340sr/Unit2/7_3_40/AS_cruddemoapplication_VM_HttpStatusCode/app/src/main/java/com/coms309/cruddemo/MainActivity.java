package com.coms309.cruddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    EditText inputId, inputFirstName, inputLastName, inputEmail;
    TextView textGetResponse;
    Button buttonGet, buttonPost, buttonPut, buttonDelete, buttonGetAll;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize inputs
        inputId = findViewById(R.id.input_id);
        inputFirstName = findViewById(R.id.input_first_name);
        inputLastName = findViewById(R.id.input_last_name);
        inputEmail = findViewById(R.id.input_email);
        textGetResponse = findViewById(R.id.text_get_response);
        buttonGet = findViewById(R.id.button_get);
        buttonPost = findViewById(R.id.button_post);
        buttonPut = findViewById(R.id.button_put);
        buttonDelete = findViewById(R.id.button_delete);
        buttonGetAll = findViewById(R.id.button_get_all);

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // URL
//        String URL = "http://10.0.2.2:8080";
        String URL = "https://8ecbebd9-99d9-4dec-aad4-686501d967b6.mock.pstmn.io";

        // Button to send GET request
//        buttonGet.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                sendGetRequest(URL+"/mytestapi");
//            }
//        });

        // Button to send GET request
        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = inputId.getText().toString();
                if (!id.isEmpty()) {
                    // Send GET request
                    sendGetRequest(URL + "/students/" + id);
                } else {
                    textGetResponse.setText("Please enter a valid Student ID");
                }
            }
        });


        // Button to send GET ALL request
        buttonGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendGetAllRequest(URL + "/students");
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

                sendPostRequest(URL+"/students", json);

            }
        });

        // Button to send PUT request
        buttonPut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = inputId.getText().toString();
                String firstName = inputFirstName.getText().toString();
                String lastName = inputLastName.getText().toString();
                String email = inputEmail.getText().toString();

                // Create JSON object for PUT request
                JSONObject json = new JSONObject();
                try {
                    json.put("id", id);
                    json.put("firstName", firstName);
                    json.put("lastName", lastName);
                    json.put("email", email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Send PUT request
                sendPutRequest(URL + "/students/" + id, json);
            }
        });

        // Button to send DELETE request
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = inputId.getText().toString();
                if (!id.isEmpty()) {
                    // Send DELETE request
                    sendDeleteRequest(URL + "/students/" + id);
                } else {
                    textGetResponse.setText("Please enter a valid Student ID");
                }
            }
        });
    }



    // Method to send GET Request using Volley
//    private void sendGetRequest(String url) {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("GET RESPONSE", response);
//                        textGetResponse.setText(response);  // Update UI with the response
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("GET ERROR", error.toString());
//                error.printStackTrace();
//            }
//        });
//
//        // Add the request to the RequestQueue
//        requestQueue.add(stringRequest);
//    }
    // Method to send GET Request
    private void sendGetRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("GET RESPONSE", response.toString());

                        try {
                            // Check if the student exists or not
                            if (response.has("student")) {
                                // Extract the student information and status from the JSON response
                                JSONObject student = response.getJSONObject("student");
                                String firstName = student.getString("firstName");
                                String lastName = student.getString("lastName");
                                String email = student.getString("email");
                                String status = response.getString("status");

                                // Update the TextView with the student details and status from the response
                                textGetResponse.setText("Student Info:\nFirst Name: " + firstName +
                                        "\nLast Name: " + lastName + "\nEmail: " + email +
                                        "\nStatus: " + status);
                            } else {
                                // Handle the case when the student doesn't exist (i.e., 404 Not Found)
                                String message = response.getString("message");
                                String status = response.getString("status");
                                textGetResponse.setText("Response: " + message + "\nStatus: " + status);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textGetResponse.setText("Error parsing response");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GET ERROR", error.toString());
                error.printStackTrace();
                textGetResponse.setText("Error: " + error.toString());
            }
        });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    // Method to send GET request to retrieve all students
    private void sendGetAllRequest(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("GET ALL RESPONSE", response.toString());

                        try {
                            // Prepare a string to display all students' information
                            StringBuilder studentsInfo = new StringBuilder();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject student = response.getJSONObject(i);
                                String id = student.getString("id");
                                String firstName = student.getString("firstName");
                                String lastName = student.getString("lastName");
                                String email = student.getString("email");

                                // Append student details to the string
                                studentsInfo.append("ID: ").append(id)
                                        .append("\nFirst Name: ").append(firstName)
                                        .append("\nLast Name: ").append(lastName)
                                        .append("\nEmail: ").append(email)
                                        .append("\n\n");
                            }

                            // Update the TextView with the list of students
                            textGetResponse.setText(studentsInfo.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textGetResponse.setText("Error parsing response");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("GET ALL ERROR", error.toString());
                error.printStackTrace();
                textGetResponse.setText("Error: " + error.toString());
            }
        });

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }


     // Method to send POST Request using Volley
    private void sendPostRequest(String url, JSONObject jsonData) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("POST RESPONSE", response.toString());

                        try {
                            // Extract the "message" and "status" from the JSON response
                            String message = response.getString("message");
                            String status = response.getString("status");

                            // Update the TextView with the message from the response
                            textGetResponse.setText("Response: " + message + "\nStatus: " + status);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textGetResponse.setText("Error parsing response");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("POST ERROR", error.toString());
                error.printStackTrace();

                // Optionally, show the error in the TextView
                textGetResponse.setText("Error: " + error.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    // Method to send PUT Request
    private void sendPutRequest(String url, JSONObject jsonData) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("PUT RESPONSE", response.toString());

                        try {
                            // Extract the "message" and "status" from the JSON response
                            String message = response.getString("message");
                            String status = response.getString("status");

                            // Update the TextView with the message from the response
                            textGetResponse.setText("Updated Student: " + message + "\nStatus: " + status);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textGetResponse.setText("Error parsing response");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PUT ERROR", error.toString());
                error.printStackTrace();
                textGetResponse.setText("Error: " + error.toString());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    // Method to send DELETE Request
    private void sendDeleteRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("DELETE RESPONSE", response.toString());

                        try {
                            // Extract the "message" and "status" from the JSON response
                            String message = response.getString("message");
                            String status = response.getString("status");

                            // Update the TextView with the message from the response
                            textGetResponse.setText("Response: " + message + "\nStatus: " + status);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            textGetResponse.setText("Error parsing response");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("DELETE ERROR", error.toString());
                error.printStackTrace();
                textGetResponse.setText("Error: " + error.toString());
            }
        });

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }


    protected void onDestroy() {
        super.onDestroy();
        // Cancel all pending requests when activity is destroyed
        if (requestQueue != null) {
            requestQueue.cancelAll(this);
        }
    }
}
