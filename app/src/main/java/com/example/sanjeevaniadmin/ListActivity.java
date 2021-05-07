package com.example.sanjeevaniadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.example.sanjeevaniadmin.Adapter.ListPhotoAdapter;
import com.example.sanjeevaniadmin.Models.ChatModel;
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

public class ListActivity extends AppCompatActivity {

    private CircleImageView profile;
    private TextView username;

    private FirebaseUser firebaseUser;

    private List<ChatModel> chatModelList;
    DatabaseReference databaseReference;

    private RecyclerView listRecyclerView;
    private ImageView send_Replaybtn;
    private EditText replayToSend;
    private ListPhotoAdapter listAdapter;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        profile = findViewById(R.id.profileImage_replay);
        username = findViewById(R.id.username_replay);
        listRecyclerView = findViewById(R.id.listview_RecyclerView);
        send_Replaybtn = findViewById(R.id.send_replay);
        replayToSend = findViewById(R.id.replayTosend);

        Toolbar toolbar = (Toolbar) findViewById(R.id.listView_toolbar);
        setSupportActionBar(toolbar);


        intent = getIntent();
        final String userid = intent.getStringExtra("userID");
        int position = intent.getIntExtra("position", -1);

        listRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ListActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        listRecyclerView.setLayoutManager(linearLayoutManager);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        send_Replaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = replayToSend.getText().toString();
                if (!msg.equals("")) {
                    sendMessasge(FirebaseAuth.getInstance().getUid(), userid, msg);
                } else {
                    Toast.makeText(ListActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                replayToSend.setText("");
                replayToSend.requestFocus();
            }
        });
        readMessage(FirebaseAuth.getInstance().getUid(), userid);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

    }

    private void sendMessasge(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference rootref = reference.child("Lists").push();

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
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModelList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ChatModel chatModel = dataSnapshot1.getValue(ChatModel.class);
                    if (chatModel.getReceiver().equals(myid) && chatModel.getSender().equals(userId) || chatModel.getReceiver().equals(userId) && chatModel.getSender().equals(myid)) {
                        chatModelList.add(chatModel);
                    }
                    listAdapter = new ListPhotoAdapter(ListActivity.this, chatModelList);
                    listRecyclerView.setAdapter(listAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
