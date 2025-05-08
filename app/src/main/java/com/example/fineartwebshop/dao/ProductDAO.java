package com.example.fineartwebshop.dao;

import com.example.fineartwebshop.model.ProductModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public static List<ProductModel> generateDummyProducts() {
        List<ProductModel> products = new ArrayList<>();
        products.add(new ProductModel("Author 1", "product_1.jpg", "Product 1", 25, "Seller 1"));
        products.add(new ProductModel("Author 2", "product_2.jpg", "Product 2", 39, "Seller 2"));
        products.add(new ProductModel("Author 3", "product_1.jpg", "Product 3", 19, "Seller 3"));
        products.add(new ProductModel("Author 4", "product_2.jpg", "Product 4", 49, "Seller 4"));
        products.add(new ProductModel("Author 5", "product_1.jpg", "Product 5", 12, "Seller 5"));
        products.add(new ProductModel("Author 3", "product_1.jpg", "Product 3", 19, "Seller 3"));
        products.add(new ProductModel("Author 4", "product_2.jpg", "Product 4", 49, "Seller 4"));
        products.add(new ProductModel("Author 5", "product_1.jpg", "Product 5", 12, "Seller 5"));
        return products;
    }

    public static void save(ProductModel product) {
        CollectionReference productsRef = FirebaseFirestore.getInstance().collection("products");

        productsRef.add(product);
    }
}
