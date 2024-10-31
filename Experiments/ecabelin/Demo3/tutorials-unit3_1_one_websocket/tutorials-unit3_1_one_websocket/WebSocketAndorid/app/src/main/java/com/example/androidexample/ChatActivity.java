package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity extends AppCompatActivity implements WebSocketListener{

    private Button sendBtn;
    private EditText msgEtx;
    private TextView msgTv;
    private TextView Count;
    private Integer hi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendBtn);
        msgEtx = (EditText) findViewById(R.id.msgEdt);
        msgTv = (TextView) findViewById(R.id.tx1);
        Count = (TextView) findViewById(R.id.Counter);

        /* connect this activity to the websocket instance */
        WebSocketManager.getInstance().setWebSocketListener(ChatActivity.this);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            try {
                // send message
                WebSocketManager.getInstance().sendMessage(msgEtx.getText().toString());
                //String input = msgTv.getText().toString();
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
            CheckHi(s);
            msgTv.setText(s + "\n"+message);
        });

    }

    @Override
    public void onWebSocketClose(int code, String reason, boolean remote) {
        String closedBy = remote ? "server" : "local";
        runOnUiThread(() -> {
            String s = msgTv.getText().toString();
            msgTv.setText(s + "---\nconnection closed by " + closedBy + "\nreason: " + reason);
        });
    }

    @Override
    public void onWebSocketOpen(ServerHandshake handshakedata) {}

    @Override
    public void onWebSocketError(Exception ex) {}

    public void CheckHi(String test1){
        String[] arr = test1.split("\n");
        String test = arr[arr.length - 1];
        if (test.contains("Hi") || test.contains("hi") || test.contains("hello") || test.contains("Hello")){
            hi++;
        } else if (test.contains("goodbye") || test.contains("Goodbye") || test.contains("bye") || test.contains("Bye")){
            hi--;
        }
        String output = "Hello Counter: " + hi;
        Count.setText(output);
    }
}