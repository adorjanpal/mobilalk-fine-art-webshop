package com.example.fineartwebshop.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fineartwebshop.databinding.CartItemBinding;
import com.example.fineartwebshop.model.ProductModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder> {

    private List<ProductModel> cartItems;
    private OnRemoveClickListener onRemoveClickListener;
    public CartItemAdapter(List<ProductModel> cartItems) {
        this.cartItems = cartItems;
    }


    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }

    public void setOnRemoveClickListener(OnRemoveClickListener listener) {
        this.onRemoveClickListener = listener;
    }

    @NonNull
    @Override
    public CartItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CartItemBinding binding = CartItemBinding.inflate(inflater, parent, false);
        return new CartItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemViewHolder holder, int position) {
        ProductModel currentItem = cartItems.get(position);
        holder.bind(currentItem);


        holder.binding.removeButton.setOnClickListener(v -> {
            if (onRemoveClickListener != null) {
                onRemoveClickListener.onRemoveClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public void setCartItems(List<ProductModel> newCartItems) {
        this.cartItems = newCartItems;
        notifyDataSetChanged();
    }

    public ProductModel getItem(int position) {
        return cartItems.get(position);
    }


    static class CartItemViewHolder extends RecyclerView.ViewHolder {

        private final CartItemBinding binding;

        public CartItemViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductModel product) {
            binding.cartItemName.setText(product.getName());
            binding.cartItemPrice.setText(product.getPriceWithCurrency());
            binding.cartItemSeller.setText(product.getSeller());
            loadImage(product.getImgUrl(), binding.cartItemImage);
        }

        private void loadImage(String imageName, ImageView imageView) {
            if (imageName == null || imageName.isEmpty()) {
                imageView.setImageResource(android.R.drawable.ic_menu_gallery);
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
                Log.e("StorageError", "Failed to load image in CartItemAdapter", e);
                imageView.setImageResource(android.R.drawable.ic_menu_report_image);
            });
        }
    }
}