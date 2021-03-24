package com.syd.ticktocktimepieces.models;

import com.google.gson.annotations.SerializedName;

public class PlaceOrderRestore {
    @SerializedName("success")
    private String success;

    @SerializedName("massage")
    private String massage;

    public String getSuccess() {
        return success;
    }

    public String getMassage() {
        return massage;
    }
}
