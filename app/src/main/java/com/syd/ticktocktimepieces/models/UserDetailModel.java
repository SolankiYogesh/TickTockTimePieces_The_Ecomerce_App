package com.syd.ticktocktimepieces.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class UserDetailModel {
    public List<UserModel> getUser_Details() {
        return User_Details;
    }

    @SerializedName("user_details")
    private List<UserModel>User_Details;
}
