package com.example.CyTrack.Utilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.CyTrack.Utilities.User;

public class NetworkUtils {

    /**
     * Posts user data to the given URL with the given parameters.
     *
     * @param context  the context
     * @param url      the URL to post the user data to
     * @param params   the parameters to post the user data with
     * @param callBack the callback to handle the response
     */
    public static void postData(Context context, String url, Map<String, String> params, callbackMessage callBack) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
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

    /**
     * Callback interface for posting user data and getting the user ID.
     */
    public interface postUserAndGetIDCallback {
        void onSuccess(int id, String message);

        void onError(String message);
    }

    /**
     * Posts user data to the given URL and retrieves the user ID.
     *
     * @param context  the context
     * @param url      the URL to post the user data to
     * @param params   the parameters to post the user data with
     * @param callback the callback to handle the response
     */
    public static void postUserAndGetID(Context context, String url, Map<String, String> params, postUserAndGetIDCallback callback) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
            try {
                String message = response.getString("message");
                JSONObject data = response.getJSONObject("data");

                int userID = data.getInt("userID");
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
     * Callback interface for fetching user data.
     */
    public interface fetchUserDataCallback {
        void onSuccess(User user, String message);

        void onError(String message);
    }

    /**
     * Fetches user data from the given URL and calls the appropriate callback method.
     *
     * @param context  the context
     * @param url      the URL to fetch the user data from
     * @param callback the callback to call when the data is fetched
     */
    public static void fetchUserData(Context context, String url, fetchUserDataCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                String message = response.getString("message");
                JSONObject data = response.getJSONObject("data");

                User user = new User(
                        data.getInt("userID"),
                        data.getString("username"),
                        data.getString("firstName"),
                        data.getString("lastName"),
                        data.getInt("age"),
                        data.getString("gender"),
                        data.getInt("streak")
                );
                callback.onSuccess(user, message);
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
     * Modifies user data on the given URL with the given parameters.
     *
     * @param context         the context
     * @param url             the URL to modify the user data on
     * @param params          the parameters to modify the user data with
     * @param callbackMessage the callback to handle the response
     */
    public static void modifyData(Context context, String url, Map<String, String> params, callbackMessage callbackMessage) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, response -> {
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

    /**
     * Callback interface for handling success and error messages.
     */
    public interface callbackMessage {
        void onSuccess(String message);

        void onError(String message);
    }

    /**
     * Sends a DELETE request to the given URL.
     *
     * @param context  the context
     * @param url      the URL to send the DELETE request to
     * @param callback the callback to handle the response
     */
    public static void deleteRequest(Context context, String url, callbackMessage callback) {
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

    /**
     * Extracts and returns an error message from a VolleyError.
     *
     * @param error the VolleyError
     * @return the error message
     */
    public static String errorResponse(VolleyError error) {
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