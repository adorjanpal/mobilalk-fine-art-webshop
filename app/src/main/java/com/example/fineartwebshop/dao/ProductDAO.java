package com.example.fineartwebshop.dao;

import android.util.Log;


import com.example.fineartwebshop.model.ProductModel;
import com.example.fineartwebshop.type.FirestoreCallback;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.callback.Callback;

public class ProductDAO {
    private final static String COLLECTION_NAME = "products";

    public static void getAll(FirestoreCallback callback) {
        CollectionReference productsRef = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<ProductModel> productList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    ProductModel product = document.toObject(ProductModel.class);
                    productList.add(product);
                }
                callback.onSuccess(productList);
            } else {
                Exception e = task.getException();
                Log.e("FirestoreError", "Error getting documents: ", e);
                callback.onFailure(e);
            }
        });
    }

    public static void save(ProductModel product) {
        CollectionReference productsRef = FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

        productsRef.add(product);
    }
}
