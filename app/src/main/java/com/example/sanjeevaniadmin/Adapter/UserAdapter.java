package com.example.sanjeevaniadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sanjeevaniadmin.ListActivity;
import com.example.sanjeevaniadmin.MessageActivity;
import com.example.sanjeevaniadmin.Models.UserModel;
import com.example.sanjeevaniadmin.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context context;
    private List<UserModel> userModelList;
    private boolean loadList;

    public UserAdapter(Context context, List<UserModel> userModelList, boolean loadList) {
        this.userModelList = userModelList;
        this.context = context;
        this.loadList = loadList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UserModel userModel = userModelList.get(position);
        holder.userText.setText(userModel.getName());
        if (userModel.getProfile().equals("")) {
            holder.profileImage.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(context).load(userModel.getProfile()).into(holder.profileImage);
        }
        holder.setData(userModel.getId(), position, loadList);


    }

    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userText;
        public CircleImageView profileImage;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userText = itemView.findViewById(R.id.username);
            profileImage = itemView.findViewById(R.id.profileImage);

        }

        private void setData(final String id, final int position, boolean load) {
//            userText.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, id, Toast.LENGTH_SHORT).show();
//
//                }
//            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (loadList) {
                        Intent listIntent = new Intent(context, ListActivity.class);
                        listIntent.putExtra("userID", id);
                        listIntent.putExtra("position", position);
                        context.startActivity(listIntent);
                    } else {
                        Intent messasgeIntent = new Intent(context, MessageActivity.class);
                        messasgeIntent.putExtra("userID", id);
                        messasgeIntent.putExtra("position", position);
                        context.startActivity(messasgeIntent);
                    }

                }
            });
        }


    }
}
