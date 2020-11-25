package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

public class ChatWindowActivity<frameLayout> extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    private android.widget.ListView listView;
    private android.widget.EditText editText;
    private android.widget.Button button;
    private java.util.ArrayList<String> stringArrayList = new java.util.ArrayList();
    private ChatDatabaseHelper chatDatabaseHelper;
    private SQLiteDatabase db;
    private FrameLayout frameLayout;
    private ArrayList arrayId = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        android.util.Log.i(ACTIVITY_NAME, "In onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        chatDatabaseHelper = new ChatDatabaseHelper(getApplicationContext());
        db = chatDatabaseHelper.getWritableDatabase();

        String[] projection = {
                chatDatabaseHelper.KEY_ID,
                chatDatabaseHelper.KEY_MESSAGE
        };

        Cursor cursor = db.query( chatDatabaseHelper.TABLE_NAME, projection,null,null,null,null,null);
        Log.i(ACTIVITY_NAME, "Cursor's column count = " + cursor.getColumnCount());
        Log.i(ACTIVITY_NAME, "FIRST COLUMN NAME: " + cursor.getColumnName(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID)));
        Log.i(ACTIVITY_NAME, "SECOND COLUMN NAME: " + cursor.getColumnName(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
        while(cursor.moveToNext()) {
            String message = cursor.getString(cursor.getColumnIndexOrThrow(chatDatabaseHelper.KEY_MESSAGE));
            stringArrayList.add(message);
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(chatDatabaseHelper.KEY_ID));
            arrayId.add(id);
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
        }
        cursor.close();

        listView = findViewById(R.id.chatView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.send_button);
        frameLayout = findViewById(R.id.frame_layout);
        if(frameLayout != null){
            Log.i(ACTIVITY_NAME, "Using Tablet Layout");
        }

        final ChatAdapter messageAdapter =new ChatAdapter( this );
        listView.setAdapter(messageAdapter);
//        listView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DisplayMetrics metrics = new DisplayMetrics();
//                getWindowManager().getDefaultDisplay().getMetrics(metrics);
//                int width = metrics.widthPixels;
//                if (width > 600 ){
//                    android.util.Log.i(ACTIVITY_NAME, "Using Tablet");
//
//                }else{
//                    android.util.Log.i(ACTIVITY_NAME, "Using Phone");
//                    android.content.Intent intent = new android.content.Intent(ChatWindowActivity.this, MessageDetails.class);
//                    startActivity(intent);
//                }
//
//            }
//        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = String.valueOf(editText.getText());
                stringArrayList.add(content);
                messageAdapter.notifyDataSetChanged();
                editText.setText("");

                ContentValues values = new ContentValues();
                values.put(ChatDatabaseHelper.KEY_MESSAGE, content);
                long newRowId = db.insert(ChatDatabaseHelper.TABLE_NAME, null, values);
                android.util.Log.i("INSERT ROW", String.valueOf(newRowId));
            }
        });

    }

    @Override
    protected void onDestroy() {
        android.util.Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
        chatDatabaseHelper.close();
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(android.content.Context ctx){
            super(ctx,0);
        }

        public int getCount(){
            return stringArrayList.size();
        }

        public String getItem(int position){
            return stringArrayList.get(position);
        }

        public long getItemId(int position) {
            return (long) arrayId.get(position);
        }

        public View getView(int position, View convertView, android.view.ViewGroup parent){
            android.view.LayoutInflater inflater = ChatWindowActivity.this.getLayoutInflater();
            View result = null ;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            else
                result = inflater.inflate(R.layout.chat_row_incoming, null);
            android.widget.TextView message = (android.widget.TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }
    }

}