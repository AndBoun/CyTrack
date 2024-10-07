package com.example.CyTrack;

import android.util.Log;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class NetworkHelper {

    private static final Executor executorService = Executors.newSingleThreadExecutor();

    public static String sendGetRequest(String urlString) { // TODO: Change to return JSONObject
        FutureTask<String> futureTask = new FutureTask<>(() -> {
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

                // Parse JSON response TODO: Implement this in actual login
                JSONArray jsonArray = new JSONArray(result);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    Log.d("JSON OBJECT " + i, jsonObject.toString(4)); // Log each JSON object
//                }

                return result;
            } catch (Exception e) {
                Log.e("GET ERROR", e.getMessage(), e); // Log any errors
                e.printStackTrace();
                return "ERROR";
            }
        });

        executorService.execute(futureTask);

        try {
            return futureTask.get(); // Wait for the result
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
}