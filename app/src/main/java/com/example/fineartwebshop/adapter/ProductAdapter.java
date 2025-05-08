package com.example.fineartwebshop.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fineartwebshop.R;
import com.example.fineartwebshop.databinding.ProductCardBinding;
import com.example.fineartwebshop.model.ProductModel;


import java.io.InputStream;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductModel> products;

    public ProductAdapter(List<ProductModel> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductCardBinding binding = ProductCardBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductModel product = products.get(position);
        holder.productName.setText(product.getName());

        Glide.with(holder.itemView.getContext())
                .load("file:///android_asset/" + product.getImgUrl())
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;

        public ProductViewHolder(ProductCardBinding binding) {
            super(binding.getRoot());
            productImage = binding.productImage;
            productName = binding.productName;
        }
    }
}