package com.example.fineartwebshop.model;

public class ProductModel {
    private String author;
    private String imgUrl;
    private String name;
    private Integer price;
    private String seller;

    public ProductModel(String author, String imgUrl, String name, Integer price, String seller) {
        this.author = author;
        this.imgUrl = imgUrl;
        this.name = name;
        this.price = price;
        this.seller = seller;
    }

    public static ProductModel initNew() {
        return new ProductModel("", "", "", -1, "");
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }
}
