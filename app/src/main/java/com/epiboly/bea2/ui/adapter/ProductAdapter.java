package com.epiboly.bea2.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // 使用Glide库加载图片
import com.epiboly.bea2.R;
import com.epiboly.bea2.app.AppAdapter;
import com.epiboly.bea2.http.glide.GlideApp;
import com.epiboly.bea2.http.model.Product;

import java.util.List;

public class ProductAdapter extends AppAdapter<Product> {
    private List<Product> productList;
    private Context context;

    public ProductAdapter(Context context) {
        super(context);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return getItem(position).getType();
//    }


    @NonNull
    @Override
    public AppAdapter<?>.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder();
    }

//    @Override
//    public int getItemCount() {
//        return productList.size();
//    }

    private final class ViewHolder extends AppAdapter<?>.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productScore;
        TextView productPrice;
        TextView productQuantity;

        public ViewHolder() {
            super(R.layout.item_product);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productScore = itemView.findViewById(R.id.product_score);
            productQuantity = itemView.findViewById(R.id.product_quantity);
        }

        @Override
        public void onBindView(int position) {
            Product product = getItem(position);
            productName.setText(product.getName());
            productScore.setText("消耗积分：" + product.getScore());
            productPrice.setText("商品价值：" + product.getPrice().toPlainString());
            productQuantity.setText("剩余：" + product.getQuantity() + "件");
            GlideApp.with(getContext())
                    .asBitmap()
                    .load(product.getImageUrl())
//                    .placeholder(placeholder)
//                    .error(placeholder)
                    .into(productImage);
        }
    }
}