package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

public class ListItemsActivity extends AppCompatActivity {

    protected static final int REQUEST_IMAGE_CAPTURE = 1;
    protected static final String ACTIVITY_NAME = "ListItemsActivity";
    private android.widget.ImageButton cameraButton;
    private android.widget.Switch swicth;
    private android.widget.CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        cameraButton = findViewById(R.id.imageButton3);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent takePictureIntent = new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (android.content.ActivityNotFoundException e) {
                    android.util.Log.i(ACTIVITY_NAME, "Camera Error");
                }
            }
        });

        swicth = findViewById(R.id.switch1);
        swicth.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setOnCheckedChanged();
                } else {
                    setOffCheckedChanged();
                }
            }
        });

        checkBox = findViewById(R.id.checkBox);
        checkBox.setOnCheckedChangeListener(new android.widget.CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                checkTheCheckBox();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        android.util.Log.i(ACTIVITY_NAME, "In onActivityResult()");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            android.graphics.Bitmap imageBitmap = (android.graphics.Bitmap) extras.get("data");
            cameraButton.setImageBitmap(imageBitmap);
        }
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

    public void setOnCheckedChanged() {
        CharSequence text = "Switch is On";
        int duration = android.widget.Toast.LENGTH_SHORT;
        android.widget.Toast toast = android.widget.Toast.makeText(this , text, duration);
        toast.show();
    }

    public void setOffCheckedChanged() {
        CharSequence text = "Switch is Off";
        int duration = android.widget.Toast.LENGTH_LONG;
        android.widget.Toast toast = android.widget.Toast.makeText(this , text, duration);
        toast.show();
    }

    public void checkTheCheckBox(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ListItemsActivity.this);
        builder.setMessage(R.string.dialog_message)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.ok, new android.content.DialogInterface.OnClickListener() {
                    public void onClick(android.content.DialogInterface dialog, int id) {
                        android.util.Log.i(ACTIVITY_NAME, "In Yes()");
                        android.content.Intent resultIntent = new android.content.Intent(ListItemsActivity.this, MainActivity.class);
                        resultIntent.putExtra("Response", "Message from activity list page.");
                        setResult(android.app.Activity.RESULT_OK, resultIntent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new android.content.DialogInterface.OnClickListener() {
                    public void onClick(android.content.DialogInterface dialog, int id) {
                        android.util.Log.i(ACTIVITY_NAME, "In No()");
                    }
                })
                .show();
    }


}