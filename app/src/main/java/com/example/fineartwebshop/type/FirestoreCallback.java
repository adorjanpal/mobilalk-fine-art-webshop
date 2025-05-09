package com.example.fineartwebshop.type;

import com.example.fineartwebshop.model.ProductModel;

import java.util.List;

public interface FirestoreCallback {
    void onSuccess(List<ProductModel> productList);
    void onFailure(Exception e);
}