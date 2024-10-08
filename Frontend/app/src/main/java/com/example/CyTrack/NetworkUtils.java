package com.example.CyTrack;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

class NetworkUtils {

    /**
     * Posts user data to the given URL with the given parameters
     * @param context the context
     * @param url the URL to post the user data to
     * @param params the parameters to post the user data with
     * @param responseListener the response listener
     * @param errorListener the error listener
     */
    static void postUserData(Context context, String url, Map<String, String> params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    interface postUserAndGetIDCallback {
        void onSuccess(int id);
        void onError(Exception e);
    }

    static void postUserAndGetID(Context context, String url, Map<String, String> params, postUserAndGetIDCallback callback) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, response -> {
            try {
                int id = response.getInt("id");
                callback.onSuccess(id);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> callback.onError(new Exception(error.getMessage()))) {
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
     * Callback interface for fetching user data, to be used with {@link NetworkUtils#fetchUserData(Context, String, fetchUserDataCallback)}
     */
    interface fetchUserDataCallback {
        void onSuccess(User user);
        void onError(Exception e);
    }

    /**
     * Fetches user data from the given URL and calls the appropriate callback method
     * @param context the context
     * @param url the URL to fetch the user data from
     * @param callback the callback to call when the data is fetched
     */
    static void fetchUserData(Context context, String url, fetchUserDataCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                User user =new User(
                        response.getInt("id"),
                        response.getString("name"),
                        response.getInt("age"),
                        response.getString("gender"),
                        response.getInt("streak"));
                callback.onSuccess(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> callback.onError(new Exception(error.getMessage()))) {
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
     * Modifies user data on the given URL with the given parameters
     * @param context the context
     * @param url the URL to modify the user data on
     * @param params the parameters to modify the user data with
     * @param responseListener the response listener
     * @param errorListener the error listener
     */
    static void modifyUserData(Context context, String url, Map<String, String> params, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        JSONObject jsonObject = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, jsonObject, responseListener, errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
