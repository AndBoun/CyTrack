package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity extends AppCompatActivity implements WebSocketListener{

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendBtn);
        msgEtx = (EditText) findViewById(R.id.msgEdt);
        msgTv = (TextView) findViewById(R.id.tx1);

        /* connect this activity to the websocket instance */
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {
                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
                msgEtx.setText("");
            } catch (Exception e) {
                Log.d("ExceptionSendMessage:", e.getMessage().toString());
            }
        });
    }


    @Override
    public void onWebSocketMessage(String message) {
        /**
         * In Android, all UI-related operations must be performed on the main UI thread
         * to ensure smooth and responsive user interfaces. The 'runOnUiThread' method
         * is used to post a runnable to the UI thread's message queue, allowing UI updates
         * to occur safely from a background or non-UI thread.
         */
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "\n"+message);
        });
    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);

            Toast.makeText(this, "connection closed", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {
        Toast.makeText(this, "connection Opened", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onWebSocketError(Exception ex) {}

    @Override
    public void onBackPressed() {
        // Close the WebSocket connection
        WebSocketManager.getInstance().disconnectWebSocket();
        // Call the superclass method to handle the normal back button behavior
        super.onBackPressed();
    }
}