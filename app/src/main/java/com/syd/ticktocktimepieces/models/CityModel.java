package com.syd.ticktocktimepieces.models;

public class CityModel {
    String name;
    public CityModel(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}

