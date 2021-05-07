package com.example.sanjeevaniadmin.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevaniadmin.Adapter.OrdersAdapter;
import com.example.sanjeevaniadmin.DatabaseQuries;
import com.example.sanjeevaniadmin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {

    public OrdersFragment() {
        // Required empty public constructor
    }

    private RecyclerView orderReclyclerView;
    private OrdersAdapter ordersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        orderReclyclerView = view.findViewById(R.id.orderRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        orderReclyclerView.setLayoutManager(linearLayoutManager);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        if (DatabaseQuries.orderModelList.size() == 0) {
            DatabaseQuries.loadOrdersData(orderReclyclerView, getContext());
        } else {
            ordersAdapter = new OrdersAdapter(DatabaseQuries.orderModelList, getContext());
            ordersAdapter.notifyDataSetChanged();
        }
        orderReclyclerView.setAdapter(ordersAdapter);
    }
}
