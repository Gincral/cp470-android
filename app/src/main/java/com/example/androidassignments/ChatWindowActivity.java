package com.example.androidassignments;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ChatWindowActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    private android.widget.ListView listView;
    private android.widget.EditText editText;
    private android.widget.Button button;
    private java.util.ArrayList<String> stringArrayList = new java.util.ArrayList();
    private ChatDatabaseHelper chatDatabaseHelper;
    private SQLiteDatabase db;
    private MessageFragment messageFragment;
    private Boolean tablet = false;
    private ChatAdapter messageAdapter;

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
        tablet = (findViewById(R.id.frame)!=(null));
        android.util.Log.i("Tablet: ", String.valueOf(tablet));

        messageAdapter =new ChatAdapter( this );
        listView.setAdapter(messageAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                android.util.Log.i("Clicked Item: ", "Position:" + String.valueOf(position) + " ID:" + String.valueOf(id));
                android.util.Log.i("Clicked Item: ", "Message:" + messageAdapter.getItem(position));

                Bundle data = new Bundle();
                data.putLong("id",id);
                data.putString("message", messageAdapter.getItem(position));

                messageFragment = new MessageFragment(ChatWindowActivity.this);
                messageFragment.setArguments(data);

                if (tablet){
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frame,messageFragment);
                    ft.commit();
                }
                else{
                    Intent intent = new Intent(ChatWindowActivity.this, MessageDetails.class);
                    intent.putExtra("data",data);
                    startActivityForResult(intent,11);
                }
            }
        });

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
    protected void onPause() {
        android.util.Log.i(ACTIVITY_NAME, "In onPause()");
        if (tablet){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(messageFragment);
            ft.commit();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        android.util.Log.i(ACTIVITY_NAME, "In onDestroy()");
        super.onDestroy();
        chatDatabaseHelper.close();
    }

    public void deleteMessage(int id) {
        android.util.Log.i(ACTIVITY_NAME, "In deleteMessage()");
        if (tablet){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(messageFragment);
            ft.commit();
        }
        db.delete(ChatDatabaseHelper.TABLE_NAME,ChatDatabaseHelper.KEY_ID+"="+id,null);
        updateMessage();
    }

    public void updateMessage(){
        android.util.Log.i(ACTIVITY_NAME, "In updateMessage()");
        stringArrayList = new ArrayList<>();
        Cursor cursor = db.query(false, chatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String msg = cursor.getString(cursor.getColumnIndex( ChatDatabaseHelper.KEY_MESSAGE));
            stringArrayList.add(msg);
            cursor.moveToNext();
        }
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 11 && resultCode == Activity.RESULT_OK){
            int id = data.getIntExtra("messageId",-1);
            if (id != -1){
                deleteMessage(id);
            }
        }
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

        public long getItemId(int position){
            Cursor cursor = db.query(false, chatDatabaseHelper.TABLE_NAME, null, null, null, null, null, null, null);
            cursor.moveToPosition(position);
            long id = cursor.getLong( cursor.getColumnIndex( ChatDatabaseHelper.KEY_ID));
            return id;
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