package com.example.sanjeevaniadmin.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sanjeevaniadmin.Models.WishListModel;
import com.example.sanjeevaniadmin.R;

import java.util.ArrayList;
import java.util.List;

public class SelectProductAdapter extends RecyclerView.Adapter<SelectProductAdapter.ViewHolder> {
    private List<WishListModel> wishListModelList;
    private Boolean wishlist;
    public static List<String> productIds = new ArrayList<>();
    private int lastposition = -1;
    private Context context;
    private boolean fromsearch;

    public SelectProductAdapter(List<WishListModel> wishListModelList, Context context) {
        this.wishListModelList = wishListModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.productwithcheckbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final String productId = wishListModelList.get(position).getProductId();
        String resource = wishListModelList.get(position).getProductImageWish();
        String title = wishListModelList.get(position).getProductTitleWish();
        long freeCoupens = wishListModelList.get(position).getFreeCoupensNo();
        String rating = wishListModelList.get(position).getRating();
        long totalRatings = wishListModelList.get(position).getTotalRating();
        String productprice = wishListModelList.get(position).getProductPriceWish();
        String cuttedprice = wishListModelList.get(position).getCuttedPriceWish();
        boolean payment = wishListModelList.get(position).isCOD();
        boolean inStock = wishListModelList.get(position).isInStock();
        holder.setData(productId, resource, title, freeCoupens, rating, totalRatings, productprice, cuttedprice, payment, position, inStock);
        holder.selectproduct.setChecked(false);
        holder.selectproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.selectproduct.isChecked()) {
                    productIds.add(productId);
                    Toast.makeText(holder.itemView.getContext(), productId + " is selected !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(holder.itemView.getContext(), productId + " is removed!", Toast.LENGTH_SHORT).show();
                    productIds.remove(productId);
                }
            }
        });
//        holder.selectproduct.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                holder.selectproduct.setChecked(isChecked);
////                productIds.add(productId);
////                Toast.makeText(context, productId + " is added", Toast.LENGTH_SHORT).show();
//            }
//        });

        if (lastposition < position) {
            //Animation animation= AnimationUtils.loadAnimation(holder.itemView.getContext(),R.anim.fade_in);
            holder.itemView.setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in));
            lastposition = position;
        }

    }

    @Override
    public int getItemCount() {
        return wishListModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productIamgewish;
        private TextView producttitleWish;
        private TextView freeCoupensWish;
        private ImageView coupenIconWish;
        private TextView Ratingwish;
        private TextView totalRatings;
        private View pricecut;
        private TextView productPricewish;
        private TextView cuttedPriceWish;
        private TextView paymentMethod;
        private CheckBox selectproduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productIamgewish = itemView.findViewById(R.id.product_image_wish1);
            producttitleWish = itemView.findViewById(R.id.product_title_wish1);
            freeCoupensWish = itemView.findViewById(R.id.free_coupen_wish1);
            coupenIconWish = itemView.findViewById(R.id.coupen_icon_wish1);
            Ratingwish = itemView.findViewById(R.id.tv_product_rating_miniView_wish1);
            totalRatings = itemView.findViewById(R.id.total_rating_wish1);
            pricecut = itemView.findViewById(R.id.pricecut_wish1);
            productPricewish = itemView.findViewById(R.id.product_price_wish1);
            cuttedPriceWish = itemView.findViewById(R.id.cutted_price_wish1);
            paymentMethod = itemView.findViewById(R.id.payment_method_wish1);
            selectproduct = itemView.findViewById(R.id.selectproductcheckbox);

        }

        private void setData(final String productid, String resource, String title, long freeCoupensWishNo, String avragerating, long totalRatingNo, String price, String cuttedprice, boolean COD, final int index, boolean instock) {

            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.proandcatplaceholder)).into(productIamgewish);

            producttitleWish.setText(title);
            if (freeCoupensWishNo != 0 && instock) {
                coupenIconWish.setVisibility(View.VISIBLE);
                if (freeCoupensWishNo == 1) {
                    freeCoupensWish.setText("Free " + freeCoupensWishNo + " Coupen");
                } else {
                    freeCoupensWish.setText("Free " + freeCoupensWishNo + " Coupens");
                }

            } else {
                coupenIconWish.setVisibility(View.INVISIBLE);
                freeCoupensWish.setVisibility(View.INVISIBLE);
            }
            LinearLayout rating = (LinearLayout) Ratingwish.getParent();
            if (instock) {
                rating.setVisibility(View.VISIBLE);
                Ratingwish.setVisibility(View.VISIBLE);
                totalRatings.setVisibility(View.VISIBLE);
                productPricewish.setTextColor(Color.parseColor("#000000"));
                cuttedPriceWish.setVisibility(View.VISIBLE);
                paymentMethod.setVisibility(View.VISIBLE);


                Ratingwish.setText(avragerating);
                totalRatings.setText("(" + totalRatingNo + ") Ratings");
                productPricewish.setText("Rs." + price + "/-");
                cuttedPriceWish.setText("Rs." + cuttedprice + "/-");
                if (COD) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            } else {
                rating.setVisibility(View.INVISIBLE);
                Ratingwish.setVisibility(View.INVISIBLE);
                totalRatings.setVisibility(View.INVISIBLE);
                productPricewish.setText("Out of stock");
                productPricewish.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                cuttedPriceWish.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);
            }

        }
    }

}
