package com.example.fineartwebshop.dao;

import com.example.fineartwebshop.model.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    public static List<ProductModel> generateDummyProducts() {
        List<ProductModel> products = new ArrayList<>();
        products.add(new ProductModel("Author 1", "brush_24px", "Product 1", 25, "Seller 1"));
        products.add(new ProductModel("Author 2", "brush_24px", "Product 2", 39, "Seller 2"));
        products.add(new ProductModel("Author 3", "brush_24px", "Product 3", 19, "Seller 3"));
        products.add(new ProductModel("Author 4", "brush_24px", "Product 4", 49, "Seller 4"));
        products.add(new ProductModel("Author 5", "brush_24px", "Product 5", 12, "Seller 5"));
        return products;
    }
}
