package com.example.sanjeevaniadmin;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevaniadmin.Adapter.OrdersAdapter;
import com.example.sanjeevaniadmin.Models.MyOrderItemModel;
import com.example.sanjeevaniadmin.Models.OrderModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DatabaseQuries {

    public static List<OrderModel> orderModelList = new ArrayList<>();

    public static void loadOrdersData(final RecyclerView userRecyclerView, final Context context) {
        orderModelList.clear();
        FirebaseFirestore.getInstance().collection("ORDERS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            final OrdersAdapter ordersAdapter = new OrdersAdapter(orderModelList, context);
                            for (final DocumentSnapshot documentSnapshot : task.getResult().getDocuments()) {
                                final List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();
                                FirebaseFirestore.getInstance().collection("ORDERS").document(documentSnapshot.getId()).collection("OrderItem").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    for (DocumentSnapshot orderItems : task.getResult().getDocuments()) {
                                                        myOrderItemModelList.add(new MyOrderItemModel(orderItems.getString("Product_Id")
                                                                , orderItems.getString("Product_Title")
                                                                , orderItems.getString("Product_Image")
                                                                , orderItems.getString("Order_Status")
                                                                , orderItems.getString("Address")
                                                                , orderItems.getString("Coupen_Id")
                                                                , orderItems.getString("Cutted_Price")
                                                                , orderItems.getDate("Ordered_Date")
                                                                , orderItems.getDate("Packed_Date")
                                                                , orderItems.getDate("Shipped_Date")
                                                                , orderItems.getDate("Delivered_Date")
                                                                , orderItems.getDate("Cancelled_Date")
                                                                , orderItems.getString("Discounted_price")
                                                                , orderItems.getLong("Free_Coupen")
                                                                , orderItems.getString("Full Name")
                                                                , orderItems.getString("ORDER_ID")
                                                                , orderItems.getString("Payment_Method")
                                                                , orderItems.getString("Pincode")
                                                                , orderItems.getString("Product_Price")
                                                                , orderItems.getLong("Product_Quantity")
                                                                , orderItems.getString("User_Id")
                                                                , orderItems.getString("Delivery_Price")
                                                                , orderItems.getBoolean("Cancellation_requested")
                                                        ));
                                                    }
                                                    orderModelList.add(new OrderModel(documentSnapshot.get("ORDER_ID").toString()
                                                            , documentSnapshot.get("Order_Status").toString()
                                                            , documentSnapshot.getDate("Ordered_Date")
                                                            , documentSnapshot.getDate("Packed_Date")
                                                            , documentSnapshot.getDate("Shipped_Date")
                                                            , documentSnapshot.getDate("Delivered_Date")
                                                            , documentSnapshot.getDate("Cancelled_Date")
                                                            , documentSnapshot.get("Payment_Status").toString()
                                                            , documentSnapshot.get("Payment_Method").toString()
                                                            , (long) documentSnapshot.get("Total_Item")
                                                            , (long) documentSnapshot.get("total_Amount")
                                                            , documentSnapshot.getString("Address")
                                                            , documentSnapshot.getString("Full Name")
                                                            , documentSnapshot.getString("Pincode")
                                                            , myOrderItemModelList
                                                    ));
                                                    ordersAdapter.notifyDataSetChanged();
                                                } else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                            userRecyclerView.setAdapter(ordersAdapter);
                            ordersAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
