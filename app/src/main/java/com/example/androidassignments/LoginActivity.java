package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    private android.widget.Button loginButton;
    private android.widget.EditText loginEditText;

    public static final String SHARED_PREFERENCE_LOGIN = "shared preferences";
    public static final String DEFAULT_EMAIL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (android.widget.Button) findViewById(R.id.button2);
        loginEditText = (android.widget.EditText) findViewById(R.id.editText);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPreferences();
            }
        });

        loadSharedPreferences();
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
    protected void onStop() {
        android.util.Log.i(ACTIVITY_NAME, "In onStop()");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        android.util.Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
    }

    public void saveSharedPreferences(){
        android.content.SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_LOGIN, MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DEFAULT_EMAIL, loginEditText.getText().toString());
        editor.apply();
    }

    public void loadSharedPreferences(){
        android.util.Log.i(ACTIVITY_NAME, "In load()");
        android.content.SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_LOGIN, MODE_PRIVATE);
        loginEditText.setText(sharedPreferences.getString(DEFAULT_EMAIL, "email@domain.com"));
    }
}