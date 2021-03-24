package com.syd.ticktocktimepieces.models;

import com.google.gson.annotations.SerializedName;

public class LoginResponseModel {
    @SerializedName("success")
    private String success;
    @SerializedName("massage")
    private String massage;


    @SerializedName("user_details")
    private UserDetailModel userDetailsObject;

    public UserDetailModel getUserDetailsObject() {
        return userDetailsObject;
    }

    public String getSuccess() {
        return success;
    }

    public String getMassage() {
        return massage;
    }
}
