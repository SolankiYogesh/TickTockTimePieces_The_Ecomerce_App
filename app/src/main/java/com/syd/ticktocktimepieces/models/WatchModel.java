package com.syd.ticktocktimepieces.models;

import com.google.gson.annotations.SerializedName;

public class WatchModel {
    @SerializedName("id")
    private String Id;
    @SerializedName("category")
    private String Category;
    @SerializedName("name")
    private String Name;
    @SerializedName("Company")
    private String COmpany;
    @SerializedName("description")
    private String Description;
    @SerializedName("image")
    private String Image;
    @SerializedName("image_2")
    private String Image_2;
    @SerializedName("image_3")
    private String Image_3;
    @SerializedName("image_4")
    private String Image_4;
    @SerializedName("published_year")
    private String PublishedYear;
    @SerializedName("price")
    private String Price;
    private String Quantity;

    public String getId() {
        return Id;
    }

    public String getCategory() {
        return Category;
    }

    public String getName() {
        return Name;
    }

    public String getCOmpany() {
        return COmpany;
    }

    public String getDescription() {
        return Description;
    }

    public String getImage() {
        return Image;
    }

    public String getPublishedYear() {
        return PublishedYear;
    }

    public String getPrice() {
        return Price;
    }

    public String getImage_2() {
        return Image_2;
    }

    public String getImage_3() {
        return Image_3;
    }

    public String getImage_4() {
        return Image_4;
    }
    public String getQuantity() {
        return Quantity ;
    }
    public void setId(String id) {
        Id = id;
    }

    public void setCategory(String category) {
        Category = category;
    }
    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCOmpany(String COmpany) {
        this.COmpany = COmpany;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setImage(String image) {
        Image = image;
    }

    public void setImage_2(String image_2) {
        Image_2 = image_2;
    }

    public void setImage_3(String image_3) {
        Image_3 = image_3;
    }

    public void setImage_4(String image_4) {
        Image_4 = image_4;
    }

    public void setPublishedYear(String publishedYear) {
        PublishedYear = publishedYear;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
