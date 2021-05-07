package com.example.sanjeevaniadmin;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevaniadmin.Adapter.CategoryAdapter;
import com.example.sanjeevaniadmin.Adapter.HomePageAdapter;
import com.example.sanjeevaniadmin.Adapter.OrdersAdapter;
import com.example.sanjeevaniadmin.Adapter.SelectProductAdapter;
import com.example.sanjeevaniadmin.Fragments.HomeFragment;
import com.example.sanjeevaniadmin.Models.CategoryModel;
import com.example.sanjeevaniadmin.Models.HomePageModel;
import com.example.sanjeevaniadmin.Models.HorizontalProductScrollModel;
import com.example.sanjeevaniadmin.Models.MyOrderItemModel;
import com.example.sanjeevaniadmin.Models.OrderModel;
import com.example.sanjeevaniadmin.Models.SliderModel;
import com.example.sanjeevaniadmin.Models.WishListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class DatabaseQuries {
    public static FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//    public static String email, fullname, profile, usernamemobile;

    public static List<CategoryModel> categoryModelList = new ArrayList<CategoryModel>();

    public static List<List<HomePageModel>> lists = new ArrayList<>();
    public static List<String> loadedCategoriesNames = new ArrayList<>();

    public static List<OrderModel> orderModelList = new ArrayList<>();
    public static List<WishListModel> selectproductList = new ArrayList<>();


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

    public static void loadCategories(final RecyclerView categoryRecyclerView, final Context context) {
        categoryModelList.clear();
        firebaseFirestore.collection("CATEGORIES").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                categoryModelList.add(new CategoryModel(documentSnapshot.get("icon").toString(), documentSnapshot.get("categoryName").toString()));
                            }
                            CategoryAdapter categoryAdapter = new CategoryAdapter(categoryModelList, context);
                            categoryRecyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void loadFragmentData(final RecyclerView homePageRecyclerView, final Context context, final int index, String categoryName) {
        firebaseFirestore.collection("CATEGORIES")
                .document(categoryName).collection("TOP_DEALS").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                if ((long) documentSnapshot.get("view_type") == 0) {
                                    List<SliderModel> sliderModelList = new ArrayList<>();
                                    long no_of_banners = (long) documentSnapshot.get("no_of_banner");
                                    for (long x = 1; x < no_of_banners + 1; x++) {
                                        sliderModelList.add(new SliderModel(documentSnapshot.get("banner" + x).toString(), documentSnapshot.get("banner" + x + "_background").toString()));
                                    }
                                    lists.get(index).add(new HomePageModel(0, sliderModelList));

                                } else if ((long) documentSnapshot.get("view_type") == 1) {
                                    lists.get(index).add(new HomePageModel(1, documentSnapshot.get("strip_ad_banner").toString(), documentSnapshot.get("background").toString()));


                                } else if ((long) documentSnapshot.get("view_type") == 2) {
                                    List<WishListModel> ViewAllProductList = new ArrayList<>();
                                    List<HorizontalProductScrollModel> horizontalProductScrollModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {

                                        horizontalProductScrollModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString()
                                                , " "
                                                , " "
                                                , " "
                                                , " "));


                                        ViewAllProductList.add(new WishListModel(documentSnapshot.get("product_ID_" + x).toString()
                                                , " "
                                                , " "
                                                , 0
                                                , " "
                                                , 0
                                                , " "
                                                , " "
                                                , false
                                                , false));

                                    }
                                    lists.get(index).add(new HomePageModel(2, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), horizontalProductScrollModelList, ViewAllProductList));

                                } else if ((long) documentSnapshot.get("view_type") == 3) {
                                    List<HorizontalProductScrollModel> GridProductlayoutModelList = new ArrayList<>();
                                    long no_of_products = (long) documentSnapshot.get("no_of_products");
                                    for (long x = 1; x < no_of_products + 1; x++) {
                                        GridProductlayoutModelList.add(new HorizontalProductScrollModel(documentSnapshot.get("product_ID_" + x).toString()
                                                , " "
                                                , " "
                                                , " "
                                                , " "));
                                    }
                                    lists.get(index).add(new HomePageModel(3, documentSnapshot.get("layout_title").toString(), documentSnapshot.get("layout_background").toString(), GridProductlayoutModelList));
                                }

                            }
                            HomePageAdapter homePageAdapter = new HomePageAdapter(lists.get(index));
                            homePageRecyclerView.setAdapter(homePageAdapter);
                            homePageAdapter.notifyDataSetChanged();
                            HomeFragment.swipeRefreshLayout.setRefreshing(false);
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public static void loadWishList(final RecyclerView selectProductRecycler, final Context context) {
        selectproductList.clear();
        firebaseFirestore.collection("PRODUCTS")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        selectproductList.add(new WishListModel(documentSnapshot.getId().toString()
                                , documentSnapshot.getString("product_image_1")
                                , documentSnapshot.getString("product_title")
                                , (long) documentSnapshot.get("free_coupens")
                                , documentSnapshot.getString("avarage_rating")
                                , (long) documentSnapshot.get("total_rating")
                                , documentSnapshot.getString("product_price").toString()
                                , documentSnapshot.getString("cutted_price").toString()
                                , (boolean) documentSnapshot.get("COD")
                                , true
                        ));
                    }
                    SelectProductAdapter selectProductAdapter = new SelectProductAdapter(selectproductList,context);
                    selectProductRecycler.setAdapter(selectProductAdapter);
                    selectProductAdapter.notifyDataSetChanged();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public static void clearData() {
        categoryModelList.clear();
        lists.clear();
        loadedCategoriesNames.clear();

    }

}
