package com.syd.ticktocktimepieces.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WatchResponseModel {
    @SerializedName("watch")
    private List<WatchModel> watch;

    public List<WatchModel> getWatch() {
        return watch;
    }
}
