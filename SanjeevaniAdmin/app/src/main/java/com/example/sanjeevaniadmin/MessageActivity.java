package com.example.sanjeevaniadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sanjeevaniadmin.Adapter.ChatAdapter;
import com.example.sanjeevaniadmin.Models.ChatModel;
import com.example.sanjeevaniadmin.Models.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {

    private CircleImageView profile;
    private TextView username;

    private FirebaseUser firebaseUser;

    private List<ChatModel> chatModelList;
    DatabaseReference databaseReference;

    private RecyclerView chatRecyclerView;
    private ImageView send_btn;
    private EditText messasgeToSend;
    private ChatAdapter chatAdapter;
    Intent intent;
    //private String reciverId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        profile = findViewById(R.id.profileImage_mess);
        username = findViewById(R.id.username_mess);
        chatRecyclerView = findViewById(R.id.chat_RecyclerView);
        send_btn = findViewById(R.id.send_message);
        messasgeToSend = findViewById(R.id.messageTosend);


        Toolbar toolbar = (Toolbar) findViewById(R.id.message_toolbar);
        setSupportActionBar(toolbar);


        intent = getIntent();
        final String userid = intent.getStringExtra("userID");
        int position = intent.getIntExtra("position", -1);

        chatRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessageActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = messasgeToSend.getText().toString();
                if (!msg.equals("")) {
                    sendMessasge(FirebaseAuth.getInstance().getUid(), userid, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                messasgeToSend.setText("");
                messasgeToSend.requestFocus();
            }
        });
        readMessage(FirebaseAuth.getInstance().getUid(), userid);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
    }

    private void sendMessasge(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rootref = reference.child("Chats").push();

        String MsgId = rootref.getKey();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("messageId", MsgId);
        rootref.setValue(hashMap);


    }

    private void readMessage(final String myid, final String userId) {
        chatModelList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModelList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChatModel chatModel = dataSnapshot1.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(myid) && chatModel.getSender().equals(userId) || chatModel.getReceiver().equals(userId) && chatModel.getSender().equals(myid)) {
                        chatModelList.add(chatModel);
                    }
                    chatAdapter = new ChatAdapter(MessageActivity.this, chatModelList);
                    chatRecyclerView.setAdapter(chatAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
