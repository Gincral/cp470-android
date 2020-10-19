package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ChatWindowActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    private android.widget.ListView listView;
    private android.widget.EditText editText;
    private android.widget.Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        listView = findViewById(R.id.chatView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.send_button);
    }
}