package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.List;

public class ChatWindowActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    private android.widget.ListView listView;
    private android.widget.EditText editText;
    private android.widget.Button button;
    private java.util.ArrayList<String> stringArrayList = new java.util.ArrayList();
    private ChatDatabaseHelper chatDatabaseHelper;
    private SQLiteDatabase db;

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
            Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
        }
        cursor.close();

        listView = findViewById(R.id.chatView);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.send_button);

        final ChatAdapter messageAdapter =new ChatAdapter( this );
        listView.setAdapter(messageAdapter);

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