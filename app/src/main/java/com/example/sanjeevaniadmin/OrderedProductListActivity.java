package com.example.sanjeevaniadmin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevaniadmin.Adapter.MyOrderAdapter;
import com.example.sanjeevaniadmin.Models.MyOrderItemModel;

import java.util.ArrayList;
import java.util.List;

public class OrderedProductListActivity extends AppCompatActivity {
    private RecyclerView orderItemListRecyclerView;
    private MyOrderAdapter myOrderAdapter;
    private int position;
    private List<MyOrderItemModel> myOrderItemModelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordered_product_list);
        orderItemListRecyclerView = findViewById(R.id.orderproductRecyclerView);
        position = getIntent().getIntExtra("position", -1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderedProductListActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderItemListRecyclerView.setLayoutManager(linearLayoutManager);
        if (position != -1) {
            myOrderItemModelList = (List<MyOrderItemModel>) DatabaseQuries.orderModelList.get(position).getMyOrderItemModelList();
        }
        myOrderAdapter = new MyOrderAdapter(myOrderItemModelList);
        myOrderAdapter.notifyDataSetChanged();
        orderItemListRecyclerView.setAdapter(myOrderAdapter);

    }
}
