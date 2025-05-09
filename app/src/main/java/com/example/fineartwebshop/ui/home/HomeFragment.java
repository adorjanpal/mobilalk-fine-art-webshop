package com.example.fineartwebshop.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar; // Import ProgressBar
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fineartwebshop.R;
import com.example.fineartwebshop.adapter.ProductAdapter;
import com.example.fineartwebshop.dao.ProductDAO;
import com.example.fineartwebshop.databinding.FragmentHomeBinding;
import com.example.fineartwebshop.listener.OnProductClickListener;
import com.example.fineartwebshop.model.ProductModel;
import com.example.fineartwebshop.type.FirestoreCallback;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements OnProductClickListener {

    private FragmentHomeBinding binding;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;
    private List<ProductModel> products;
    private ProgressBar loadingProgressBar;
    private final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.d(TAG, "onCreate() called");

        recyclerView = binding.productsRecyclerView;
        loadingProgressBar = binding.loadingProgressBar;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        products = new ArrayList<>();
        adapter = new ProductAdapter(products, this);
        recyclerView.setAdapter(adapter);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        loadProductsFromFirestore();
    }

    private void loadProductsFromFirestore() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        ProductDAO.getAll(new FirestoreCallback() {
            @Override
            public void onSuccess(List<ProductModel> productList) {
                Log.d(TAG, "Products loaded: " + productList.size());
                products.clear();
                products.addAll(productList);
                adapter.notifyDataSetChanged();

                loadingProgressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FirestoreError", "Failed to fetch products", e);
                loadingProgressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onProductClick(ProductModel product) {
        NavController navController = Navigation.findNavController(requireView());
        Bundle bundle = new Bundle();

        bundle.putSerializable("product", product);
        navController.navigate(R.id.navigation_product_details, bundle);
    }

}