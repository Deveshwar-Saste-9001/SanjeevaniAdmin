package com.example.sanjeevaniadmin.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sanjeevaniadmin.Models.ChatModel;
import com.example.sanjeevaniadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListPhotoAdapter extends RecyclerView.Adapter<ListPhotoAdapter.ViewHolder> {


    public static final int LIST_TYPE_LEFT = 0;
    public static final int LIST_TYPE_RIGHT = 1;
    private Context context;
    private List<ChatModel> listModelList;
    private static boolean show_imagesss;

    FirebaseUser user;

    public ListPhotoAdapter(Context context, List<ChatModel> chatModelList) {
        this.context = context;
        this.listModelList = chatModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LIST_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            show_imagesss = false;
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_list_adapter, parent, false);
            show_imagesss = true;
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String message = listModelList.get(position).getMessage();
        String messasgeId = listModelList.get(position).getMessageId();
        holder.setData(message, messasgeId, position);


    }

    @Override
    public int getItemCount() {
        return listModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView show_image;
        public TextView show_message;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        private void setData(final String message, final String messasgeId, int index) {
            if (show_imagesss) {
                show_image = itemView.findViewById(R.id.list_PHOTO);
                if (!message.equals("")) {
                    Glide.with(context).load(message).into(show_image);
                }
                show_image.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        //Toast.makeText(context, messasgeId, Toast.LENGTH_SHORT).show();
                        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("list/" + messasgeId + ".jpg");
                        storageReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Lists").child(messasgeId);
                                    reference.removeValue();
                                    Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "not deleted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
//                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                        builder.setTitle("Delete");
//                        builder.setMessage("Are you sure to delete?");
//                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        });
//                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
                        return true;
                    }
                });
            } else {
                show_message = itemView.findViewById(R.id.show_Messege);
                show_message.setText(message);
            }


        }
    }

    @Override
    public int getItemViewType(int position) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (listModelList.get(position).getSender().equals(user.getUid())) {
            return LIST_TYPE_RIGHT;
        } else {
            return LIST_TYPE_LEFT;
        }
    }
}
