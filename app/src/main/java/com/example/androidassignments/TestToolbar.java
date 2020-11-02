package com.example.androidassignments;

import android.content.ComponentCallbacks;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class TestToolbar extends AppCompatActivity {
    private String selectOne = "You selected item 1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_toolbar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "You just clicked the letter icon", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (android.view.Menu m){
        super.onCreateOptionsMenu(m);
        getMenuInflater().inflate(R.menu.toolbar_menu, m);
        return true;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem mi){
        switch (mi.getItemId()){
            case R.id.action_one:
                Log.d("Toolbar", "action one selected");
                Toolbar toolbar = findViewById(R.id.toolbar);
                Snackbar.make(toolbar, selectOne, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                break;
            case R.id.action_two:
                Log.d("Toolbar", "action two selected");
                goBackDialog();
                break;
            case R.id.action_three:
                Log.d("Toolbar", "action three selected");
                customDialog();
                break;
            case R.id.about:
                Log.d("Toolbar", "About selected");
                android.widget.Toast toast = android.widget.Toast.makeText(this , "Version 1.0, by Ni Yang <3", android.widget.Toast.LENGTH_SHORT); //this is the ListActivity
                toast.show();
                break;
        }
        return true;
    }

    public void goBackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
        builder.setTitle(R.string.back_to_previous);
        builder.setPositiveButton(R.string.ok, new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
                // Do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void customDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(TestToolbar.this);
        LayoutInflater inflater = TestToolbar.this.getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.custom_dialog, null);
        builder.setView(inflatedView);
        final EditText editText = inflatedView.findViewById(R.id.newMessage);
        builder.setPositiveButton(R.string.ok, new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
                Log.d("new message", editText.getText().toString());
                selectOne = editText.getText().toString();
            }
        });
        builder.setNegativeButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {
            public void onClick(android.content.DialogInterface dialog, int id) {
                // Do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}