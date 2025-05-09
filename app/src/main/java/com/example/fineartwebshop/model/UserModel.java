package com.example.fineartwebshop.model;

import java.util.List;

public class UserModel {
    private String email;
    private List<ProductModel> likedProducts;
    private List<ProductModel> cart;

    public UserModel() {}

    public UserModel(String email, List<ProductModel> likedProducts, List<ProductModel> cart) {
        this.email = email;
        this.likedProducts = likedProducts;
        this.cart = cart;
    }

    public List<ProductModel> getLikedProducts() {
        return likedProducts;
    }

    public void setLikedProducts(List<ProductModel> likedProducts) {
        this.likedProducts = likedProducts;
    }

    public List<ProductModel> getCart() {
        return cart;
    }

    public void setCart(List<ProductModel> cart) {
        this.cart = cart;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
