package com.example.sanjeevaniadmin;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddStripAdsActivity extends AppCompatActivity {

    private EditText position;
    private Button finishBtn;
    private ImageView selectedImageView;
    private Dialog loadingDialog;
    private Uri imageuri;
    private boolean updatePhoto = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_strip_ads);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Strip Banners");

        finishBtn = findViewById(R.id.FinishBtn);
        position = findViewById(R.id.position);
        selectedImageView = findViewById(R.id.add_strip_ad_image1);
        loadingDialog = new Dialog(AddStripAdsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(position.getText())) {
                    updatePhotoF();
                } else {
                    Toast.makeText(AddStripAdsActivity.this, "Please Enter the index", Toast.LENGTH_SHORT).show();
                }
            }
        });

        selectedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/+");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/+");
                    startActivityForResult(galleryIntent, 1);
                }
            }
        });

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
                Toast.makeText(AddStripAdsActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    imageuri = data.getData();
                    updatePhoto = true;
                    Glide.with(AddStripAdsActivity.this).load(imageuri).into(selectedImageView);
                } else {
                    Toast.makeText(AddStripAdsActivity.this, "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void updatePhotoF() {
        loadingDialog.show();
        String id = String.valueOf(new Date().getTime());
        final String productid = "stripads" + id;
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("banners/" + productid + ".jpg");
        if (imageuri != null) {
            Glide.with(AddStripAdsActivity.this).asBitmap().load(imageuri).into(new ImageViewTarget<Bitmap>(selectedImageView) {
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
                                            Map<String, Object> addbanner = new HashMap<>();
                                            addbanner.put("view_type", (long) 1);
                                            addbanner.put("index", Long.parseLong(position.getText().toString()));
                                            addbanner.put("strip_ad_banner", task.getResult().toString());
                                            addbanner.put("background", "#ffffff");
                                            FirebaseFirestore.getInstance().collection("CATEGORIES").document("Home").collection("TOP_DEALS").document().set(addbanner)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(AddStripAdsActivity.this, "added ", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(AddStripAdsActivity.this, "not added", Toast.LENGTH_SHORT).show();
                                                            }
                                                            finish();
                                                            loadingDialog.dismiss();
                                                        }
                                                    });
                                        } else {
                                            loadingDialog.dismiss();
                                        }
                                    }

                                });

                            } else {
                                loadingDialog.dismiss();
                                String error = task.getException().getMessage();
                                Toast.makeText(AddStripAdsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                }

                @Override
                protected void setResource(@Nullable Bitmap resource) {
                }

            });
        }


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
