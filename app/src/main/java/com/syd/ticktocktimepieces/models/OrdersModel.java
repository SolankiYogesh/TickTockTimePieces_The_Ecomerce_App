package com.syd.ticktocktimepieces.models;

import com.google.gson.annotations.SerializedName;

public class OrdersModel {
    @SerializedName("id")
    private String id;
    @SerializedName("user_email")
    private String user_email;
    @SerializedName("total_ammount")
    private String total_ammount;
    @SerializedName("products")
    private String products;
    @SerializedName("order_date")
    private String order_date;

    public String getId() {
        return id;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getTotal_ammount() {
        return total_ammount;
    }

    public String getProducts() {
        return products;
    }

    public String getOrder_date() {
        return order_date;
    }
}
