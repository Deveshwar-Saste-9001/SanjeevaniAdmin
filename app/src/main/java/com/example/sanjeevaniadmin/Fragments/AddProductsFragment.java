package com.example.sanjeevaniadmin.Fragments;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.sanjeevaniadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductsFragment extends Fragment {


    public AddProductsFragment() {
        // Required empty public constructor
    }

    private TextInputEditText productTitle, productPrice, productDescription, productCuttedPrice, productOtherDetail, Stock, subtitle, tagsForSearch;
    private ImageView productImage;
    private Button addProductBtn;
    private String photo;
    private Uri imageuri;
    private boolean updatePhoto = false;
    private Dialog loadingDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_products, container, false);
        productTitle = view.findViewById(R.id.product_title);
        productPrice = view.findViewById(R.id.product_price);
        subtitle = view.findViewById(R.id.productsubtitle);
        productDescription = view.findViewById(R.id.product_description);
        productCuttedPrice = view.findViewById(R.id.productOriginalPrice);
        productOtherDetail = view.findViewById(R.id.productOtherDetail);
        Stock = view.findViewById(R.id.productStock);
        productImage = view.findViewById(R.id.productImage);
        addProductBtn = view.findViewById(R.id.add_product);
        tagsForSearch = view.findViewById(R.id.productTaglist);
        Stock.setText("100");

        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/+");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/+");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(productTitle.getText())) {
                    if (!TextUtils.isEmpty(productPrice.getText())) {
                        if (!TextUtils.isEmpty(productDescription.getText())) {
                            if (!TextUtils.isEmpty(productOtherDetail.getText())) {
                                if (!TextUtils.isEmpty(productCuttedPrice.getText())) {
                                    if (!TextUtils.isEmpty(subtitle.getText())) {
                                        if (!TextUtils.isEmpty(tagsForSearch.getText())) {
                                            updatePhotoF();
                                        } else {
                                            tagsForSearch.requestFocus();
                                            Toast.makeText(getContext(), "please add at least one tag", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        subtitle.requestFocus();
                                        Toast.makeText(getContext(), "please enter subtitle", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    productCuttedPrice.requestFocus();
                                    Toast.makeText(getContext(), "please enter original price", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                productOtherDetail.requestFocus();
                                Toast.makeText(getContext(), "please enter other detail", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            productDescription.requestFocus();
                            Toast.makeText(getContext(), "please enter decription", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        productPrice.requestFocus();
                        Toast.makeText(getContext(), "please enter productPrice", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    productTitle.requestFocus();
                    Toast.makeText(getContext(), "please enter product title", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    imageuri = data.getData();
                    updatePhoto = true;
                    Glide.with(getContext()).load(imageuri).into(productImage);
                } else {
                    Toast.makeText(getContext(), "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/+");
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void updatePhotoF() {
        ////updating photo
        if (updatePhoto) {
            addProductBtn.setEnabled(false);
            loadingDialog.show();
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
            String datefinal = df.format(c);
            Random random = new Random();
            int randumno = random.nextInt(99 - 1) + 1;
            final String productid = productTitle.getText() + datefinal + randumno;
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("products/" + productid + ".jpg");
            if (imageuri != null) {
                Glide.with(getContext()).asBitmap().load(imageuri).into(new ImageViewTarget<Bitmap>(productImage) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        resource.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] data = baos.toByteArray();

                        UploadTask uploadTask = storageReference.putBytes(data);
                        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                imageuri = task.getResult();
                                                Map<String, Object> updatedata = new HashMap<>();
                                                updatedata.put("1_star", (long) 0);
                                                updatedata.put("2_star", (long) 0);
                                                updatedata.put("3_star", (long) 0);
                                                updatedata.put("4_star", (long) 0);
                                                updatedata.put("5_star", (long) 0);
                                                updatedata.put("COD", (boolean) true);
                                                updatedata.put("avarage_rating", "0");
                                                updatedata.put("free_coupen_body", " ");
                                                updatedata.put("free_coupen_title", " ");
                                                updatedata.put("free_coupens", (long) 0);
                                                updatedata.put("max_quantity", (long) 100);
                                                updatedata.put("no_of_product_images", (long) 2);
                                                updatedata.put("offers_applied", (long) 0);
                                                updatedata.put("product_description", productDescription.getText().toString());
                                                updatedata.put("product_image_1", task.getResult().toString());
                                                updatedata.put("product_other_detail", productOtherDetail.getText().toString());
                                                updatedata.put("product_price", productPrice.getText().toString());
                                                updatedata.put("product_title", productTitle.getText().toString());
                                                updatedata.put("cutted_price", productCuttedPrice.getText().toString());
                                                updatedata.put("stock_quantity", (long) 100);
                                                updatedata.put("subtitle", subtitle.getText().toString());
                                                updatedata.put("total_rating", (long) 0);
                                                final String[] tags = tagsForSearch.getText().toString().toLowerCase().split(" ");
                                                updatedata.put("tags", Arrays.asList(tags));
                                                updatedata.put("use_tab_layout", (boolean) false);

                                                FirebaseFirestore.getInstance().collection("PRODUCTS").document(productid)
                                                        .set(updatedata).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            productTitle.setText("");
                                                            productPrice.setText("");
                                                            productDescription.setText("");
                                                            productOtherDetail.setText("");
                                                            productCuttedPrice.setText("");
                                                            subtitle.setText("");
                                                            tagsForSearch.setText("");
                                                            Toast.makeText(getContext(), "ProductAdded successfully", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            String error = task.getException().getMessage();
                                                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                        }
                                                        loadingDialog.dismiss();
                                                        productImage.setImageResource(R.drawable.proandcatplaceholder);
                                                        addProductBtn.setEnabled(true);
                                                    }
                                                });

                                            } else {
                                                loadingDialog.dismiss();
                                                addProductBtn.setEnabled(true);
                                                String error = task.getException().getMessage();
                                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else {
                                    loadingDialog.dismiss();
                                    addProductBtn.setEnabled(true);
                                    String error = task.getException().getMessage();
                                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                    @Override
                    protected void setResource(@Nullable Bitmap resource) {
                        productImage.setImageResource(R.drawable.proandcatplaceholder);
                    }
                });
            }


        }


    }
}
