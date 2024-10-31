package com.coms309.library;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText authorInput, bookInput;
    private TextView authorsTextView, booksTextView;
    private Button sendButton, getAuthorsButton, getBooksButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        authorInput = findViewById(R.id.authorInput);
        bookInput = findViewById(R.id.bookInput);
        authorsTextView = findViewById(R.id.authorsTextView);
        booksTextView = findViewById(R.id.booksTextView);
        sendButton = findViewById(R.id.sendButton);
        getAuthorsButton = findViewById(R.id.getAuthorsButton);
        getBooksButton = findViewById(R.id.getBooksButton);

        // Set button listeners to call methods from other classes
        sendButton.setOnClickListener(v -> {
            String authorName = authorInput.getText().toString();
            String bookTitle = bookInput.getText().toString();
            new SendAuthorAndBook(this, authorsTextView).sendAuthorAndBook(authorName, bookTitle);
        });

        getAuthorsButton.setOnClickListener(v -> new GetAuthors(this, authorsTextView).getAuthors());

        getBooksButton.setOnClickListener(v -> new GetBooksAndAuthors(this, booksTextView).getBooksAndAuthors());
    }
}
