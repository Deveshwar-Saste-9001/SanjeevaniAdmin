package com.example.sanjeevaniadmin;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sanjeevaniadmin.Adapter.SelectProductAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddGridProductList extends AppCompatActivity {
    private EditText position;
    private Button finishBtn;
    private EditText title;
    private RecyclerView selectProductRecyclerView;
    private SelectProductAdapter selectProductAdapter;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grid_product_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Horizontal List");
        loadingDialog = new Dialog(AddGridProductList.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        position = findViewById(R.id.position);
        finishBtn = findViewById(R.id.FinishBtn);
        title = findViewById(R.id.productViewTitle);
        selectProductRecyclerView = findViewById(R.id.selectProductforView);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(position.getText())) {
                    Toast.makeText(AddGridProductList.this, "Please select Index", Toast.LENGTH_SHORT).show();
                } else {
                    loadingDialog.show();
                    Map<String, Object> productListMap = new HashMap<>();
                    productListMap.put("index", Long.parseLong(position.getText().toString()));
                    productListMap.put("view_type", (long) 3);
                    productListMap.put("layout_background", "#FFFFFF");
                    productListMap.put("layout_title", title.getText().toString());
                    productListMap.put("no_of_products", (long) SelectProductAdapter.productIds.size());

                    for (int x = 0; x < SelectProductAdapter.productIds.size(); x++) {
                        productListMap.put("product_ID_" + (x + 1), SelectProductAdapter.productIds.get(x));

                    }
                    FirebaseFirestore.getInstance().collection("CATEGORIES").document("Home").collection("TOP_DEALS").document().set(productListMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        loadingDialog.dismiss();
                                        SelectProductAdapter.productIds.clear();
                                        finish();
                                        Toast.makeText(AddGridProductList.this, "added successful", Toast.LENGTH_SHORT).show();
                                    } else {
                                        loadingDialog.dismiss();
                                        finish();
                                        Toast.makeText(AddGridProductList.this, "not added please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
        final LinearLayoutManager homepageLayoutManager = new LinearLayoutManager(AddGridProductList.this);
        homepageLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        selectProductRecyclerView.setLayoutManager(homepageLayoutManager);

        if (DatabaseQuries.selectproductList.size() == 0) {
            DatabaseQuries.loadWishList(selectProductRecyclerView, AddGridProductList.this);
        } else {
            selectProductAdapter = new SelectProductAdapter(DatabaseQuries.selectproductList, AddGridProductList.this);
            selectProductAdapter.notifyDataSetChanged();
        }
        selectProductRecyclerView.setAdapter(selectProductAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
