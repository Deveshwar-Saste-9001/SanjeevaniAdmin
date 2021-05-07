package com.example.sanjeevaniadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevaniadmin.CategoryActivity;
import com.example.sanjeevaniadmin.Models.CategoryModel;
import com.example.sanjeevaniadmin.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<CategoryModel> categoryModelList;
    private int lastposition = -1;
    private Context context;

    public CategoryAdapter(List<CategoryModel> categoryModelList, Context context) {
        this.categoryModelList = categoryModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categoryitemlayout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String icon = categoryModelList.get(position).getCategoryIconlink();
        String title = categoryModelList.get(position).getCategoryName();
        holder.setCategory(title, position);
        holder.setCategoryicon(icon);
        if (lastposition < position) {
            //Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in));
            lastposition = position;
        }


    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView categoryicon;
        private TextView categoryTitle;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            categoryicon = itemView.findViewById(R.id.categoryImageView);
            categoryTitle = itemView.findViewById(R.id.categoryTitle);
        }

        private void setCategoryicon(String iconUrl) {
            if (!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.categoryplaceholder)).into(categoryicon);
            } else {
                Glide.with(itemView.getContext()).load(R.drawable.ic_home_black_24dp).apply(new RequestOptions().placeholder(R.drawable.categoryplaceholder)).into(categoryicon);
            }

        }

        private void setCategory(final String name, final int position) {
            categoryTitle.setText(name);
            if (!name.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position != 0) {
                            Intent categoryIntent = new Intent(context, CategoryActivity.class);
                            categoryIntent.putExtra("CategoryName", name);
                            itemView.getContext().startActivity(categoryIntent);
                        }

                    }
                });
            }
        }
    }
}
