package com.example.sanjeevaniadmin;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddBannerSliderActivity extends AppCompatActivity {

    private EditText position;
    private Button addImage, finishBtn;
    private LinearLayout itemcontaner;
    private ImageView selectedImageView;
    private Dialog loadingDialog;
    private ArrayList<String> uploadConpmleted;
    private ArrayList<Uri> mArrayUri = new ArrayList<>();
    private View addView;
    private LayoutInflater layoutInflater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_banner_slider);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Banners List");

        addImage = findViewById(R.id.addImagebanner);
        finishBtn = findViewById(R.id.FinishBtn);
        position = findViewById(R.id.position);
        itemcontaner = findViewById(R.id.containerImage);
        loadingDialog = new Dialog(AddBannerSliderActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_bar);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        addView = layoutInflater.inflate(R.layout.additemlayout, null);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((LinearLayout) itemcontaner).getChildCount() > 0) {
                    ((LinearLayout) itemcontaner).removeAllViews();
                }
                Intent i = new Intent();
                i.setType("image/*");
                //i.setType("video/*");
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(i, "android.intent.action.SEND_MULTIPLE"), 1);

            }
        });

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(position.getText())) {
                    Toast.makeText(AddBannerSliderActivity.this, "plaese add index", Toast.LENGTH_SHORT).show();
                } else {
                    if (mArrayUri.size() < 3) {
                        Toast.makeText(AddBannerSliderActivity.this, "you have to select 3 or more banner", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingDialog.show();
                        updatePhotoF(mArrayUri);
                    }

                }

            }
        });


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imagesEcoded;
        List<String> imagesEcodedList;
        String[] filepaths;

        if (requestCode == 1 && resultCode == RESULT_OK) {
            filepaths = new String[]{MediaStore.Images.Media.DATA};
            mArrayUri.clear();
            imagesEcodedList = new ArrayList<String>();
            if (data.getData() != null) {
                Uri imageUri = data.getData();

                mArrayUri.clear();
                mArrayUri = new ArrayList<Uri>();
                Cursor cursor = getContentResolver().query(imageUri, filepaths, null, null, null);
                cursor.moveToFirst();
                int columnindex = cursor.getColumnIndex(filepaths[0]);
                imagesEcoded = cursor.getString(columnindex);
                imagesEcodedList.add(imagesEcoded);
                cursor.close();

            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();

                    mArrayUri.clear();
                    mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();

                        mArrayUri.add(uri);
                        // Get the cursor
                        Cursor cursor = getContentResolver().query(uri, filepaths, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filepaths[0]);
                        imagesEcoded = cursor.getString(columnIndex);
                        imagesEcodedList.add(imagesEcoded);
                        cursor.close();


                    }
                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());

                    System.out.println("Selected Images" + mArrayUri.size());
                    for (int i = 0; i < mArrayUri.size(); i++) {
                        layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        addView = layoutInflater.inflate(R.layout.additemlayout, null);

                        selectedImageView = addView.findViewById(R.id.selectedImages);
                        Glide.with(AddBannerSliderActivity.this).load(mArrayUri.get(i)).into(selectedImageView);
                        itemcontaner.addView(addView);


                    }
                }
            }

        } else {
            Toast.makeText(this, "You haven't picked Image",
                    Toast.LENGTH_LONG).show();
        }


    }

    private void updatePhotoF(ArrayList<Uri> rrayUri) {
        final int usedchieldCount = itemcontaner.getChildCount();
        uploadConpmleted = new ArrayList<>();
        for (int i = 0; i < usedchieldCount; i++) {
            View thisChild = itemcontaner.getChildAt(i);
            ImageView banner = thisChild.findViewById(R.id.selectedImages);
            String id = String.valueOf(new Date().getTime());
            final String productid = "banner" + id;
            final StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("banners/" + productid + ".jpg");
            if (rrayUri.get(i) != null) {
                final int finalI = i;
                Glide.with(AddBannerSliderActivity.this).asBitmap().load(rrayUri.get(i)).into(new ImageViewTarget<Bitmap>(banner) {
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

                                                uploadConpmleted.add(task.getResult().toString());
                                                if (finalI == usedchieldCount - 1) {

                                                    Map<String, Object> addbanner = new HashMap<>();
                                                    addbanner.put("view_type", (long) 0);
                                                    addbanner.put("no_of_banner", uploadConpmleted.size());
                                                    addbanner.put("index", Long.parseLong(position.getText().toString()));
                                                    for (int x = 0; x < uploadConpmleted.size(); x++) {
                                                        addbanner.put("banner" + (x + 1), uploadConpmleted.get(x));
                                                        addbanner.put("banner" + (x + 1) + "_background", "#ffffff");
                                                    }
                                                    FirebaseFirestore.getInstance().collection("CATEGORIES").document("Home").collection("TOP_DEALS").document().set(addbanner)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        loadingDialog.dismiss();
                                                                        finish();
                                                                        Toast.makeText(AddBannerSliderActivity.this, "added successful", Toast.LENGTH_SHORT).show();
                                                                    } else {
                                                                        loadingDialog.dismiss();
                                                                        finish();
                                                                        Toast.makeText(AddBannerSliderActivity.this, "not added please try again", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                }

                                            }
                                        }

                                    });

                                } else {
                                    loadingDialog.dismiss();
                                    String error = task.getException().getMessage();
                                    Toast.makeText(AddBannerSliderActivity.this, error, Toast.LENGTH_SHORT).show();
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


    }
}




