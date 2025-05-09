package com.example.fineartwebshop.ui.product_details;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView; // Import ImageView
import android.widget.TextView; // Import TextView
import android.widget.Toast;

import androidx.annotation.NonNull; // Import NonNull
import androidx.annotation.Nullable; // Import Nullable
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.fineartwebshop.R;
import com.example.fineartwebshop.dao.UserDAO;
import com.example.fineartwebshop.databinding.FragmentProductDetailsBinding; // Assuming you're using View Binding for the detail layout
import com.example.fineartwebshop.model.ProductModel;
import com.example.fineartwebshop.type.FirestoreCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage; // Import FirebaseStorage
import com.google.firebase.storage.StorageReference; // Import StorageReference
import com.squareup.picasso.Picasso; // Import Picasso

import java.util.List;

public class ProductDetails extends Fragment {

    private FragmentProductDetailsBinding binding;
    private ProductModel product;

    public ProductDetails() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            product = (ProductModel) getArguments().getSerializable("product");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (product != null) {
            binding.productNameDetail.setText(product.getName());
            binding.productPriceDetail.setText(product.getPriceWithCurrency());
            binding.productDescriptionDetail.setText(product.getDescription());
            binding.productSellerDetail.setText("Seller: " + product.getSeller());

            loadImage(product.getImgUrl(), binding.productImageDetail);
        }

        binding.backButton.setOnClickListener(v -> {
            navigateBack();
        });

        binding.addToCartButton.setOnClickListener(v -> {
            handleAddToCart();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void loadImage(String imageName, ImageView imageView) {
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
            Log.e("StorageError", "Failed to load image in ProductDetails", e);
        });
    }

    private void navigateBack() {
        NavHostFragment.findNavController(this).navigateUp();
    }

    private void handleAddToCart() {
        UserDAO.addToCart(FirebaseAuth.getInstance().getCurrentUser().getEmail(), product, new FirestoreCallback() {
            @Override
            public void onSuccess(List<ProductModel> productList) {
                Toast.makeText(getContext(), "Product added to cart", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Error adding product", Toast.LENGTH_SHORT).show();
            }
        });
    }
}