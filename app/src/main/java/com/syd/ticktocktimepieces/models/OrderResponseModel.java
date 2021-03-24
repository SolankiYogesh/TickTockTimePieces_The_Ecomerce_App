package com.syd.ticktocktimepieces.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponseModel {
    @SerializedName("orders")
    private List<OrdersModel> orders;

    public List<OrdersModel> getOrders() {
        return orders;
    }
}
