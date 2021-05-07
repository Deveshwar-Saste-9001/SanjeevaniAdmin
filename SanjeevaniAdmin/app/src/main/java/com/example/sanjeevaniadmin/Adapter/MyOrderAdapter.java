package com.example.sanjeevaniadmin.Adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sanjeevaniadmin.Models.MyOrderItemModel;
import com.example.sanjeevaniadmin.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private List<MyOrderItemModel> myOrderItemModelList;

    public MyOrderAdapter(List<MyOrderItemModel> myOrderItemModelList) {
        this.myOrderItemModelList = myOrderItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String productId = myOrderItemModelList.get(position).getProductId();
        String resource = myOrderItemModelList.get(position).getProductImageOrder();
        int rating = myOrderItemModelList.get(position).getRating();
        String title = myOrderItemModelList.get(position).getProductTitleOrder();
        String orderStatus = myOrderItemModelList.get(position).getOrderStatus();
        String price = myOrderItemModelList.get(position).getProductPrice();
        long qty = myOrderItemModelList.get(position).getProductQuantity();
        Date date;
        switch (orderStatus) {
            case "Ordered":
                date = myOrderItemModelList.get(position).getOrderedDate();
                break;
            case "Packed":
                date = myOrderItemModelList.get(position).getPackedDate();
                break;
            case "Shipped":
                date = myOrderItemModelList.get(position).getShippeddate();
                break;
            case "Delivered":
                date = myOrderItemModelList.get(position).getDeliveredDate();
                break;
            case "Cancelled":
                date = myOrderItemModelList.get(position).getCancelledDate();
                break;
            default:
                date = myOrderItemModelList.get(position).getCancelledDate();
        }

        holder.setData(resource, title, orderStatus, date, rating, productId, position, price, qty);


    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImageorder;
        private ImageView orderIndicatororder;
        private TextView productTitleorder;
        private TextView DeliveryStatusorder;
        private TextView productPrice;
        private TextView productQty;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImageorder = itemView.findViewById(R.id.product_image_order1);
            orderIndicatororder = itemView.findViewById(R.id.order_satus_indicater1);
            productTitleorder = itemView.findViewById(R.id.product_title_order_d1);
            DeliveryStatusorder = itemView.findViewById(R.id.order_delivered_date1);
            productPrice = itemView.findViewById(R.id.productpriceorder);
            productQty = itemView.findViewById(R.id.productQtyorder);


        }

        private void setData(String resourse, String title, String OrderStatus, Date date, final int rating, final String ProductId, final int position, String price, long Qty) {
            Glide.with(itemView.getContext()).load(resourse).into(productImageorder);

            productTitleorder.setText(title);
            if (OrderStatus.equals("Cancelled")) {
                orderIndicatororder.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colorRed)));
            } else {
                orderIndicatororder.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM YYYY, hh:mm aa");

            DeliveryStatusorder.setText(OrderStatus + " on " + simpleDateFormat.format(date));
            productPrice.setText("Rs." + price + "/-");
            productQty.setText("Oty:-" + Qty);


        }


    }
}
