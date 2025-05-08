package com.example.fineartwebshop.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fineartwebshop.adapter.ProductAdapter;
import com.example.fineartwebshop.dao.ProductDAO;
import com.example.fineartwebshop.databinding.FragmentHomeBinding;
import com.example.fineartwebshop.model.ProductModel;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private ProductAdapter adapter;
    private RecyclerView recyclerView;
    private List<ProductModel> products;
    private final String TAG = "HomeFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Log.d(TAG, "onCreate() called");

        recyclerView = binding.productsRecyclerView;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);

        products = ProductDAO.generateDummyProducts();
        Log.d(TAG, "Number of products: " + products.size());

        if (adapter == null) {
            adapter = new ProductAdapter(products);
        }

        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}