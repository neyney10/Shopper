package com.arielu.shopper.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.arielu.shopper.demo.models.Message;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MessageBoardActivity extends AppCompatActivity {
    ListView listview;
    List<Message> messages;
    MessageAdapter messageAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_board);
        listview = findViewById(R.id.message_list);
        messages = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            messages.add(new Message("Title "+i,"this fake message","12/24/2019"));
        }
        messageAdapter =  new MessageAdapter(getApplicationContext(),messages);
        listview.setAdapter(messageAdapter);
    }
    public void sendMessage(View v){
        //TODO content to database
        Toast.makeText(getApplicationContext(), "sending message...", Toast.LENGTH_SHORT).show();
    }
}
