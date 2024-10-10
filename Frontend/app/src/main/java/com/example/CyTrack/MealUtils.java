package com.example.CyTrack;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class MealUtils {

    /**
     * Posts meal data to the given URL with the given parameters
     * @param context the context
     * @param url the URL to post the user data to
     * @param params the parameters to post the user data with
     */
    static void postMeal(Context context, String url, Map<String, String> params, callbackMessage callBack) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response ->{
            try {
                String message = response.getString("message");
                callBack.onSuccess(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            String errorMessage = errorResponse(error);
            callBack.onError(errorMessage);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    interface postMealAndGetIDCallback {
        void onSuccess(int id, String message);
        void onError(String message);
    }

    static void postMealAndGetID(Context context, String url, Map<String, String> params, postMealAndGetIDCallback callback) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
            try {
                String message = response.getString("message");
                JSONObject data = response.getJSONObject("data");
                //TODO: Integrate MealIDS
                int userID = data.getInt("ID");
                callback.onSuccess(userID, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            callback.onError(errorResponse(error));
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Callback interface for fetching meal data, to be used with {@link MealUtils#fetchMealData(Context, String, fetchMealDataCallback)}
     */
    interface fetchMealDataCallback {
        void onSuccess(Meal meal, String message);
        void onError(String message);
    }

    /**
     * Fetches meal data from the given URL and calls the appropriate callback method
     * @param context the context
     * @param url the URL to fetch the meal data from
     * @param callback the callback to call when the data is fetched
     */
    public static void fetchMealData(Context context, String url, fetchMealDataCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                String message = response.getString("message");
                JSONObject data = response.getJSONObject("data");

                Meal meal = new Meal(
                        data.getInt("ID"),
                        data.getString("name"),
                        data.getString("calories"),
                        data.getString("carbs"),
                        data.getString("protein"));
                callback.onSuccess(meal, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            callback.onError(errorResponse(error));
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Modifies meal data on the given URL with the given parameters
     * @param context the context
     * @param url the URL to modify the meal data on
     * @param params the parameters to modify the meal data with
     */
    static void modifyData(Context context, String url, Map<String, String> params, callbackMessage callbackMessage) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, response ->{
            try {
                String message = response.getString("message");
                callbackMessage.onSuccess(message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            String errorMessage = errorResponse(error);
            callbackMessage.onError(errorMessage);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    interface callbackMessage {
        void onSuccess(String message);
        void onError(String message);
    }

    static void deleteRequest(Context context, String url, callbackMessage callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null, response -> {
            try {
                String message = response.getString("message");
                callback.onSuccess(message);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }, error -> {
            String errorMessage = errorResponse(error);
            callback.onError(errorMessage);
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private static String errorResponse(VolleyError error) {
        int statusCode = error.networkResponse != null ? error.networkResponse.statusCode : -1;
        String errorMessage = "";

        // Check if the server has returned error details in JSON
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                // Convert the response data (bytes) to a JSON object
                String jsonResponse = new String(error.networkResponse.data);
                JSONObject jsonObject = new JSONObject(jsonResponse);

                // Extract error details from JSON
                String status = jsonObject.getString("status");
                JSONObject errorObject = jsonObject.getJSONObject("error");
                int errorCode = errorObject.getInt("code");
                String message = errorObject.getString("message");
                String details = errorObject.getString("details");

                // Handle the error based on this JSON
//                errorMessage += "Error Status: " + status + "\n";
//                errorMessage += "Error Code: " + errorCode + "\n";
//                errorMessage += "Message: " + message + "\n";
//                errorMessage += "Details: " + details + "\n";
                return message;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return "Error Code: " + statusCode;


    }


}
