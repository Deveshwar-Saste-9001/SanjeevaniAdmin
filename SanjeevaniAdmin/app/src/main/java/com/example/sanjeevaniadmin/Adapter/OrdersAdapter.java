package com.example.sanjeevaniadmin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevaniadmin.Models.OrderModel;
import com.example.sanjeevaniadmin.OrderedProductListActivity;
import com.example.sanjeevaniadmin.R;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    List<OrderModel> orderModelList;
    Context context;

    public OrdersAdapter(List<OrderModel> orderModelList, Context context) {
        this.orderModelList = orderModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderitemlayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String userName = orderModelList.get(position).getFullName();
        String orderstatus = orderModelList.get(position).getOrderStatus();
        String userAddress = orderModelList.get(position).getAddress();

        String date = String.valueOf(orderModelList.get(position).getOrderedDate());
        String totalItems = "total " + orderModelList.get(position).getTotalItem() + " item";
        String totalAmount = "Total Amount:- Rs." + orderModelList.get(position).getTotalAmount() + "/-";
        String paymentStatus = orderModelList.get(position).getPaymentStatus();
        String paymentMethod = orderModelList.get(position).getPaymentMethod();
        holder.setOrderdata(userName, orderstatus, userAddress, date, totalItems, paymentStatus, paymentMethod, totalAmount, position);
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView username, orderstatus, date, totalitems, paymentstatus, totalamount, userAddress, paymentMethod;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.usernameorder);
            orderstatus = itemView.findViewById(R.id.orderStatus);
            userAddress = itemView.findViewById(R.id.userAddress);
            date = itemView.findViewById(R.id.orderDate);
            totalitems = itemView.findViewById(R.id.totalItem);
            totalamount = itemView.findViewById(R.id.totalAmount);
            paymentstatus = itemView.findViewById(R.id.paymetMethod);
            paymentMethod = itemView.findViewById(R.id.paymetStatus);
        }

        private void setOrderdata(String usernametxt, String orderStatustxt, String userAddresstxt, String datetxt, String totalItems, String PaymentMethodtxt, String PaymentStatustxt, String totalAmount, final int position) {
            username.setText(usernametxt);
            orderstatus.setText(orderStatustxt);
            userAddress.setText(userAddresstxt);
            date.setText(datetxt);
            totalitems.setText(totalItems);
            totalamount.setText(totalAmount);
            paymentstatus.setText(PaymentMethodtxt);
            paymentMethod.setText(PaymentStatustxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent orderlistIntent = new Intent(itemView.getContext(), OrderedProductListActivity.class);
                    orderlistIntent.putExtra("position", position);
                    itemView.getContext().startActivity(orderlistIntent);
                }
            });

        }
    }
}
