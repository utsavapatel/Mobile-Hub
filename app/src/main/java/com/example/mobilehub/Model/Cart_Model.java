package com.example.mobilehub.Model;

public class Cart_Model {
    String uid,pro_id,item_qty,cart_product_name,cart_product_price,cart_procuct_image,product_qty,updated_price;

    public String getUpdated_price() {
        return updated_price;
    }

    public void setUpdated_price(String updated_price) {
        this.updated_price = updated_price;
    }

    public String getItem_qty() {
        return item_qty;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPro_id() {
        return pro_id;
    }

    public void setPro_id(String pro_id) {
        this.pro_id = pro_id;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public void setItem_qty(String item_qty) {
        this.item_qty = item_qty;
    }

    public String getCart_product_name() {
        return cart_product_name;
    }

    public void setCart_product_name(String cart_product_name) {
        this.cart_product_name = cart_product_name;
    }

    public String getCart_product_price() {
        return cart_product_price;
    }

    public void setCart_product_price(String cart_product_price) {
        this.cart_product_price = cart_product_price;
    }

    public String getCart_procuct_image() {
        return cart_procuct_image;
    }

    public void setCart_procuct_image(String cart_procuct_image) {
        this.cart_procuct_image = cart_procuct_image;
    }
}
