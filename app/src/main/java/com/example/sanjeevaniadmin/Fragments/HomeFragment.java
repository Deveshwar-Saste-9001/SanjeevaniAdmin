package com.example.sanjeevaniadmin.Fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.sanjeevaniadmin.Adapter.HomePageAdapter;
import com.example.sanjeevaniadmin.DatabaseQuries;
import com.example.sanjeevaniadmin.Models.HomePageModel;
import com.example.sanjeevaniadmin.Models.HorizontalProductScrollModel;
import com.example.sanjeevaniadmin.Models.SliderModel;
import com.example.sanjeevaniadmin.Models.WishListModel;
import com.example.sanjeevaniadmin.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.sanjeevaniadmin.DatabaseQuries.lists;
import static com.example.sanjeevaniadmin.DatabaseQuries.loadFragmentData;
import static com.example.sanjeevaniadmin.DatabaseQuries.loadedCategoriesNames;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    private RecyclerView HomepageRecycler;
    private List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    private FirebaseFirestore firebaseFirestore;
    private HomePageAdapter homePageAdapter;
    private ImageView noInternetConnection;
    public static SwipeRefreshLayout swipeRefreshLayout;
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        swipeRefreshLayout = view.findViewById(R.id.Refresh_layout1);
        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary), getContext().getResources().getColor(R.color.colorPrimary));

        noInternetConnection = view.findViewById(R.id.no_internet_connection1);

        HomepageRecycler = view.findViewById(R.id.HomePage_reciclerView1);


        final LinearLayoutManager homepageLayoutManager = new LinearLayoutManager(getContext());
        homepageLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        HomepageRecycler.setLayoutManager(homepageLayoutManager);


        homePageAdapter = new HomePageAdapter(homePageModelFakeList);

        /////////HomePage Fake List;
        List<SliderModel> sliderModelFakeList = new ArrayList<>();
        sliderModelFakeList.add(new SliderModel("null", "#b6b6b6"));
        sliderModelFakeList.add(new SliderModel("null", "#b6b6b6"));
        sliderModelFakeList.add(new SliderModel("null", "#b6b6b6"));
        sliderModelFakeList.add(new SliderModel("null", "#b6b6b6"));
        List<HorizontalProductScrollModel> horizontalProductScrollModelFakeList = new ArrayList<>();
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));
        horizontalProductScrollModelFakeList.add(new HorizontalProductScrollModel("", "", "", "", ""));

        homePageModelFakeList.add(new HomePageModel(0, sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1, "", "#b6b6b6"));
        homePageModelFakeList.add(new HomePageModel(2, "", "#b6b6b6", horizontalProductScrollModelFakeList, new ArrayList<WishListModel>()));
        homePageModelFakeList.add(new HomePageModel(3, "", "#b6b6b6", horizontalProductScrollModelFakeList));

        ////////////


        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            noInternetConnection.setVisibility(View.GONE);

            HomepageRecycler.setVisibility(View.VISIBLE);


            if (lists.size() == 0) {
                loadedCategoriesNames.add("Home");
                lists.add(new ArrayList<HomePageModel>());
                homePageAdapter = new HomePageAdapter(lists.get(0));
                loadFragmentData(HomepageRecycler, getContext(), 0, "Home");
            } else {
                homePageAdapter = new HomePageAdapter(lists.get(0));
                homePageAdapter.notifyDataSetChanged();
            }
            HomepageRecycler.setAdapter(homePageAdapter);

        } else {
            HomepageRecycler.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.giphy).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
        }

        ////////////refresslayou
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                networkInfo = connectivityManager.getActiveNetworkInfo();
                DatabaseQuries.clearData();
                if (networkInfo != null && networkInfo.isConnected() == true) {
                    noInternetConnection.setVisibility(View.GONE);
                    HomepageRecycler.setVisibility(View.VISIBLE);


                    homePageAdapter = new HomePageAdapter(homePageModelFakeList);

                    HomepageRecycler.setAdapter(homePageAdapter);

                    loadedCategoriesNames.add("Home");
                    lists.add(new ArrayList<HomePageModel>());
                    loadFragmentData(HomepageRecycler, getContext(), 0, "Home");


                } else {
                    HomepageRecycler.setVisibility(View.GONE);

                    swipeRefreshLayout.setRefreshing(false);
                    Glide.with(getContext()).load(R.drawable.giphy).into(noInternetConnection);
                    noInternetConnection.setVisibility(View.VISIBLE);

                }

            }
        });
        ////////////
        return view;
    }

}
