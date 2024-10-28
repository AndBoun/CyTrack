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

public class GetBooksAndAuthors {

    private final RequestQueue requestQueue;
    private final TextView booksTextView;

    public GetBooksAndAuthors(Context context, TextView booksTextView) {
        this.requestQueue = Volley.newRequestQueue(context);
        this.booksTextView = booksTextView;
    }

    public void getBooksAndAuthors() {
        String url = "http://10.0.2.2:8080/books"; // replace with actual endpoint

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    StringBuilder booksText = new StringBuilder();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject book = response.getJSONObject(i);
                            booksText.append("Title: ").append(book.getString("title")).append("\nAuthors: ");

                            JSONArray authors = book.getJSONArray("authors");
                            for (int j = 0; j < authors.length(); j++) {
                                JSONObject author = authors.getJSONObject(j);
                                booksText.append(author.getString("name"));
                                if (j < authors.length() - 1) booksText.append(", ");
                            }
                            booksText.append("\n\n");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    booksTextView.setText(booksText.toString());
                },
                error -> booksTextView.setText("Failed to retrieve books!")
        );

        requestQueue.add(jsonArrayRequest);
    }
}

