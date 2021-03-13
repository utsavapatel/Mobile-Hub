package com.example.mobilehub.Model;

public class Wishlist_Model {
    String uid;
    String pro_id;
    String wishlist_product_name;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    String wishlist_product_price;
    String wishlist_procuct_image;

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getWishlist_product_name() {
        return wishlist_product_name;
    }

    public void setWishlist_product_name(String wishlist_product_name) {
        this.wishlist_product_name = wishlist_product_name;
    }

    public String getWishlist_product_price() {
        return wishlist_product_price;
    }

    public void setWishlist_product_price(String wishlist_product_price) {
        this.wishlist_product_price = wishlist_product_price;
    }

    public String getWishlist_procuct_image() {
        return wishlist_procuct_image;
    }

    public void setWishlist_procuct_image(String wishlist_procuct_image) {
        this.wishlist_procuct_image = wishlist_procuct_image;
    }
}
