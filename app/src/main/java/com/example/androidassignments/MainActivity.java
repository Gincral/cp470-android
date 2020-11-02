package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "MainActivity";
    private android.widget.Button loginButton;
    private android.widget.Button chatButton;
    private android.widget.Button toolBarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = findViewById(R.id.button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(MainActivity.this, ListItemsActivity.class);
                startActivityForResult(intent,10);
            }
        });

        chatButton = findViewById(R.id.button_sc);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                android.content.Intent intent = new android.content.Intent(MainActivity.this, ChatWindowActivity.class);
                startActivityForResult(intent,10);
            }
        });

        toolBarButton = findViewById(R.id.toolbar_btn);
        toolBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.util.Log.i(ACTIVITY_NAME, "User clicked Test Toolbar");
                android.content.Intent intent = new android.content.Intent(MainActivity.this, TestToolbar.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart() {
        android.util.Log.i(ACTIVITY_NAME, "In onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        android.util.Log.i(ACTIVITY_NAME, "In onPause()");
        super.onPause();
    }

    @Override
    protected void onResume() {
        android.util.Log.i(ACTIVITY_NAME, "In onResume()");
        super.onResume();
    }

    @Override
    protected void onStop() {
        android.util.Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        android.util.Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, android.content.Intent data){
        android.util.Log.i(ACTIVITY_NAME, "In onActivityResult()");
        super.onActivityResult(requestCode, responseCode, data);
        if(requestCode == 10){
            android.util.Log.i(ACTIVITY_NAME, "Returned to MainActivity.onActivityResult");
        }
        if(responseCode == android.app.Activity.RESULT_OK){
            String messagePassed = data.getStringExtra("Response");
            android.widget.Toast toast = android.widget.Toast.makeText(this , "ListItemsActivity passed: "+messagePassed, android.widget.Toast.LENGTH_SHORT); //this is the ListActivity
            toast.show();

        }
    }
}