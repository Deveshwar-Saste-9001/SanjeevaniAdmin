package com.example.sanjeevaniadmin.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevaniadmin.Adapter.CategoryAdapter;
import com.example.sanjeevaniadmin.DatabaseQuries;
import com.example.sanjeevaniadmin.Models.CategoryModel;
import com.example.sanjeevaniadmin.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {


    public CategoryFragment() {
        // Required empty public constructor
    }

    private RecyclerView categoryRecyclerview;
    private CategoryAdapter categoryAdapter;
    private List<CategoryModel> categoryModeFakelList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        categoryRecyclerview = view.findViewById(R.id.categoryfragmentRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerview.setLayoutManager(linearLayoutManager);
        /////////////////////categoryFakeList
        categoryModeFakelList.add(new CategoryModel("null", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
        categoryModeFakelList.add(new CategoryModel("", ""));
////////////////////////
        categoryAdapter = new CategoryAdapter(categoryModeFakelList,getContext());

        if (DatabaseQuries.categoryModelList.size() == 0) {
            DatabaseQuries.loadCategories(categoryRecyclerview, getContext());
        } else {
            categoryAdapter = new CategoryAdapter(DatabaseQuries.categoryModelList,getContext());
            categoryAdapter.notifyDataSetChanged();
        }
        categoryRecyclerview.setAdapter(categoryAdapter);

        return view;
    }

}
