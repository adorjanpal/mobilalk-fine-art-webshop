package com.example.fineartwebshop.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fineartwebshop.R;
import com.example.fineartwebshop.databinding.ProductCardBinding;
import com.example.fineartwebshop.listener.OnProductClickListener; // Import the listener
import com.example.fineartwebshop.model.ProductModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import java.io.InputStream;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductModel> products;
    private OnProductClickListener listener;

    public ProductAdapter(List<ProductModel> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
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
        holder.productPrice.setText(product.getPriceWithCurrency());

        loadImage(product.getImgUrl(), holder.productImage);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (products == null) {
            return 0;
        }
        return products.size();
    }
    public static void loadImage(String imageName, ImageView imageView) {
        if (imageName == null || imageName.isEmpty()) {
            Log.e("StorageError", "Image name is empty or null");
            return;
        }

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("images/" + imageName);

        storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get()
                    .load(uri)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(imageView);
        }).addOnFailureListener(e -> {
            Log.e("StorageError", "Failed to load image", e);
        });
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName;
        TextView productPrice;

        public ProductViewHolder(ProductCardBinding binding) {
            super(binding.getRoot());
            productImage = binding.productImage;
            productName = binding.productName;
            productPrice = binding.productPrice;
        }
    }
}