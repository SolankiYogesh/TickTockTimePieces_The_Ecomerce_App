package com.syd.ticktocktimepieces.network;



import com.syd.ticktocktimepieces.models.CategoryResponseModel;
import com.syd.ticktocktimepieces.models.LoginResponseModel;
import com.syd.ticktocktimepieces.models.OrderResponseModel;
import com.syd.ticktocktimepieces.models.PlaceOrderRestore;
import com.syd.ticktocktimepieces.models.RegistrationResponseModel;
import com.syd.ticktocktimepieces.models.WatchResponseModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface NetworkServices {
@POST("categories.php")
 Call<CategoryResponseModel> getCategoriesFromServer();
@FormUrlEncoded
@POST("watch.php")
    Call<WatchResponseModel> getWatchByCategories(@Field("category") String category );

    @FormUrlEncoded
    @POST("registration.php")
    Call<RegistrationResponseModel> register(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("login.php")
    Call<LoginResponseModel> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("place_order.php")
    Call<PlaceOrderRestore> placeorder(@FieldMap HashMap<String, String> params);

    @FormUrlEncoded
    @POST("order_history.php")
    Call<OrderResponseModel> getOrders(@Field("email") String email);

}


