package com.coms309.library;

import android.content.Context;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class SendAuthorAndBook {

    private final RequestQueue requestQueue;
    private final TextView authorsTextView;

    public SendAuthorAndBook(Context context, TextView authorsTextView) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.authorsTextView = authorsTextView;
    }

    public void sendAuthorAndBook(String authorName, String bookTitle) {
        String url = "http://10.0.2.2:8080/books"; // replace with actual endpoint

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", authorName);
            jsonObject.put("title", bookTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> authorsTextView.setText("Author and Book added successfully!"),
                error -> authorsTextView.setText("Failed to add Author and Book!")
        );

        requestQueue.add(jsonObjectRequest);
    }
}

