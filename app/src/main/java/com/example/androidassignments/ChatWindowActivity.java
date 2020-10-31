package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class ChatWindowActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "ChatWindowActivity";
    private android.widget.ListView listView;
    private android.widget.EditText editText;
    private android.widget.Button button;
    private java.util.ArrayList<String> stringArrayList = new java.util.ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

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
            }
        });
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