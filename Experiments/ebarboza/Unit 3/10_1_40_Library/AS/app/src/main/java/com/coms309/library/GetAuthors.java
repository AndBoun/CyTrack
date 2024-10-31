package com.coms309.library;

import android.content.Context;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetAuthors {

    private final RequestQueue requestQueue;
    private final TextView authorsTextView;

    public GetAuthors(Context context, TextView authorsTextView) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.authorsTextView = authorsTextView;
    }

    public void getAuthors() {
        String url = "http://10.0.2.2:8080/authors"; // replace with actual endpoint

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    StringBuilder authorsText = new StringBuilder();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject author = response.getJSONObject(i);
                            authorsText.append(author.getString("name")).append("\n");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    authorsTextView.setText(authorsText.toString());
                },
                error -> authorsTextView.setText("Failed to retrieve authors!")
        );

        requestQueue.add(jsonArrayRequest);
    }
}

