package com.example.fineartwebshop.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast; // Import Toast

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // Keep this if you use ViewModel for other things
import androidx.recyclerview.widget.LinearLayoutManager; // Import LinearLayoutManager

import com.example.fineartwebshop.adapter.CartItemAdapter;
import com.example.fineartwebshop.dao.UserDAO; // Import UserDAO
import com.example.fineartwebshop.databinding.FragmentCartBinding;
import com.example.fineartwebshop.model.ProductModel;
import com.example.fineartwebshop.model.UserModel;
import com.example.fineartwebshop.type.FirestoreCallback; // Import FirestoreCallback
import com.google.firebase.auth.FirebaseAuth; // Import FirebaseAuth
import com.google.firebase.auth.FirebaseUser; // Import FirebaseUser

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements CartItemAdapter.OnRemoveClickListener {

    private CartItemAdapter cartItemAdapter;
    private FragmentCartBinding binding;
    private List<ProductModel> cartItems = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCartBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartItemAdapter = new CartItemAdapter(cartItems);
        binding.cartRecyclerView.setAdapter(cartItemAdapter);

        cartItemAdapter.setOnRemoveClickListener(this);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchUserCart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    private void fetchUserCart() {
        UserDAO.getCartByEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail(), new FirestoreCallback() {
            @Override
            public void onSuccess(List<ProductModel> productList) {
                 cartItems.clear();
                 cartItems.addAll(productList);
                 cartItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Error fetching cart", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRemoveClick(int position) {
        Log.d("CartFragment", "Remove button clicked for item at position: " + position);

        ProductModel itemToRemove = cartItemAdapter.getItem(position);
        if (itemToRemove != null) {
            // TODO: Implement removal from Firestore
            // Example (conceptual):
            // UserDAO.removeItemFromCart(FirebaseAuth.getInstance().getCurrentUser().getEmail(), itemToRemove.getId(), task -> {
            //     if (task.isSuccessful()) {
            //         // Item removed from Firestore successfully
            //         // Update the local list and adapter
            //         cartItems.remove(position);
            //         cartItemAdapter.notifyItemRemoved(position);
            //         Toast.makeText(getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
            //     } else {
            //         Log.e("CartFragment", "Error removing item from Firestore", task.getException());
            //         Toast.makeText(getContext(), "Error removing item", Toast.LENGTH_SHORT).show();
            //     }
            // });
        }
    }
}