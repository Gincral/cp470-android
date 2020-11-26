package com.example.androidassignments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MessageFragment extends Fragment {
    private ChatWindowActivity chatWindow;
    private TextView message;
    private TextView id;
    private Button button;
    public MessageFragment(@Nullable ChatWindowActivity window ) {
        chatWindow=window;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View lay = inflater.inflate(R.layout.message_fragment, container,false);

        message = lay.findViewById(R.id.messageD);
        message.setText(getArguments().getString("message"));
        id = lay.findViewById(R.id.idD);
        id.setText(String.valueOf(getArguments().getLong("id")));

        button = lay.findViewById(R.id.deleteD);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatWindow==(null)){
                    Intent data = new Intent();
                    data.putExtra("messageId",Integer.parseInt(id.getText().toString()));
                    getActivity().setResult(Activity.RESULT_OK, data);
                    getActivity().finish();
                }
                else{
                    chatWindow.deleteMessage(Integer.parseInt(id.getText().toString()));
                }
            }
        });
        return lay;

    }
}
