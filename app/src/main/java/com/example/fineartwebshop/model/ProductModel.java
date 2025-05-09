package com.example.fineartwebshop.model;

public class ProductModel {
    private String description;
    private String imgUrl;
    private String name;
    private Integer price;
    private String seller;

    public ProductModel() {}
    public ProductModel(String description, String imgUrl, String name, Integer price, String seller) {
        this.description = description;
        this.imgUrl = imgUrl;
        this.name = name;
        this.price = price;
        this.seller = seller;
    }

    public static ProductModel initNew() {
        return new ProductModel("", "", "", -1, "");
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return !imgUrl.isEmpty() ? imgUrl : "brush_24px";
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

    public String getPriceWithCurrency() {
        return "$" + this.price;
    }
}
