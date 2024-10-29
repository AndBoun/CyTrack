package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.handshake.ServerHandshake;

public class ChatActivity1 extends AppCompatActivity {

    private Button sendBtn, backMainBtn;
    private EditText msgEtx;
    private TextView msgTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat1);

        /* initialize UI elements */
        sendBtn = (Button) findViewById(R.id.sendBtn);
        backMainBtn = (Button) findViewById(R.id.backMainBtn);
        msgEtx = (EditText) findViewById(R.id.msgEdt);
        msgTv = (TextView) findViewById(R.id.tx1);

        /* send button listener */
        sendBtn.setOnClickListener(v -> {
            if (msgEtx.getText().length() == 0) {
                return; // do nothing if the input is empty
            }

            // broadcast this message to the WebSocketService
            // tag it with the key - to specify which WebSocketClient (connection) to send
            // in this case: "chat1"
            Intent intent = new Intent("SendWebSocketMessage");
            intent.putExtra("key", "chat1");
            intent.putExtra("message", msgEtx.getText().toString());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            msgEtx.setText(""); // clear the input field
        });

        /* back button listener */
        backMainBtn.setOnClickListener(view -> {
            // got to chat activity
            finish();
            Toast.makeText( this, "Exiting chat 1", Toast.LENGTH_SHORT).show();
        });
    }

    // For receiving messages
    // only react to messages with tag "chat1"
    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = intent.getStringExtra("key");
            if ("chat1".equals(key)){
                String message = intent.getStringExtra("message");
                runOnUiThread(() -> {
                    String s = msgTv.getText().toString();
                    msgTv.setText(s + "\n\t" + message);
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
                new IntentFilter("WebSocketMessageReceived"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver);
    }
}