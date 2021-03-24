package com.syd.ticktocktimepieces;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.syd.ticktocktimepieces.databse.DatabaseHandler;
import com.syd.ticktocktimepieces.models.PlaceOrderRestore;
import com.syd.ticktocktimepieces.models.WatchModel;
import com.syd.ticktocktimepieces.network.NetworkClient;
import com.syd.ticktocktimepieces.network.NetworkServices;
import com.syd.ticktocktimepieces.utils.Constants;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    RecyclerView WatchRecyclerView;
    ImageView imageback;
    TextView textTotalAmount;
    List<WatchModel> cartitems;
    Button PlaceOrder;
    int TotalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        PlaceOrder = findViewById(R.id.btn_placeorder);
        PlaceOrder.setOnClickListener(v -> {
            SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
            boolean isLoggedIn = preferences.getBoolean(Constants.KEY_IS_LOGGED_IN, false);
            boolean isGoogleLogedIn = preferences.getBoolean(Constants.KEY_IS_GOOGLE_LOGED_IN,false);
            if (!isLoggedIn && !isGoogleLogedIn) {
                Toast.makeText(CartActivity.this, "Please First Login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            } else {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_email", preferences.getString(Constants.KEY_EMAIL, "N/A"));
                params.put("total_ammount", String.valueOf(TotalAmount));
                params.put("products", String.valueOf(new Gson().toJson(cartitems)));
                params.put("order_date", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
                placeorder(params);
            }
        });
        textTotalAmount = findViewById(R.id.text_total_amount);
        imageback = findViewById(R.id.image_back);
        imageback.setOnClickListener(v -> onBackPressed());
        WatchRecyclerView = findViewById(R.id.cart_recycler_view);
        WatchRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        WatchRecyclerView.setHasFixedSize(true);
        getCartItems();
    }

    private void getCartItems() {
        cartitems = new DatabaseHandler(getApplicationContext()).getcartitems();
        WatchRecyclerView.setAdapter(new WatchAdapter(cartitems));
        CalculateTotal();
    }

    @SuppressLint("SetTextI18n")
    private void CalculateTotal() {
        TotalAmount = 0;
        for (int i = 0; i <= cartitems.size() - 1; i++) {

            TotalAmount = TotalAmount + Integer.parseInt(cartitems.get(i).getPrice());

        }
        textTotalAmount.setText("Total Amt. \u20B9" + String.valueOf(TotalAmount));

    }

    private class WatchAdapter extends RecyclerView.Adapter<WatchAdapter.WatchViewHolder> {

        List<WatchModel> watch;

        WatchAdapter(List<WatchModel> watch) {
            this.watch = watch;
        }

        @NonNull
        @Override
        public WatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new WatchViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_container, parent, false));
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull WatchViewHolder holder, int position) {
            if (watch.get(position).getName() != null && !watch.get(position).getName().equals("")) {
                holder.textName.setText(watch.get(position).getName());
            } else {
                holder.textName.setVisibility(View.GONE);
            }
            if (watch.get(position).getCOmpany() != null && !watch.get(position).getCOmpany().equals("")) {
                holder.textCompany.setText(watch.get(position).getCOmpany());
            } else {
                holder.textCompany.setVisibility(View.GONE);
            }
            if (watch.get(position).getPrice() != null && !watch.get(position).getPrice().equals("")) {
                holder.textPrice.setText("\u20B9" + watch.get(position).getPrice());
            } else {
                holder.textPrice.setVisibility(View.GONE);
            }
            if (watch.get(position).getImage() != null && !watch.get(position).getImage().equals("")) {
                Picasso.get().load(watch.get(position).getImage()).into(holder.imageWatch);
            } else {
                holder.imageWatch.setVisibility(View.GONE);
            }

            holder.textRemoveItem.setPaintFlags(holder.textRemoveItem.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.textRemoveItem.setOnClickListener(v -> {
                new DatabaseHandler(getApplicationContext()).removeItem(watch.get(position).getId());
                cartitems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartitems.size());
                CalculateTotal();
                Toast.makeText(CartActivity.this, "Item Removed From Cart!", Toast.LENGTH_SHORT).show();
            });
        }


        @Override
        public int getItemCount() {
            return watch.size();
        }

        public class WatchViewHolder extends RecyclerView.ViewHolder {
            ImageView imageWatch;
            TextView textName, textCompany, textPrice, textRemoveItem,quant;


            public WatchViewHolder(@NonNull View view) {
                super(view);


                imageWatch = (ImageView) view.findViewById(R.id.image_watch);
                textName = (TextView) view.findViewById(R.id.text_watch_name);
                textCompany = (TextView) view.findViewById(R.id.text_watch_company);
                textPrice = (TextView) view.findViewById(R.id.text_watch_price);
                textRemoveItem = (TextView) view.findViewById(R.id.text_remove_item);
            }
        }
    }

    private void placeorder(HashMap<String, String> params) {
        new DatabaseHandler(getApplicationContext()).deletecart();
        AlertDialog alertDialog = new SpotsDialog(CartActivity.this, R.style.Custom5);
        alertDialog.show();
        NetworkServices networkService = NetworkClient.getClient().create(NetworkServices.class);
        Call<PlaceOrderRestore> placedordercall = networkService.placeorder(params);
        placedordercall.enqueue(new Callback<PlaceOrderRestore>() {
            @Override
            public void onResponse(@NonNull Call<PlaceOrderRestore> call, @NonNull Response<PlaceOrderRestore> response) {
                PlaceOrderRestore responseBody = response.body();
                if (responseBody != null) {
                    if (responseBody.getSuccess().equals("1")) {
                        startActivity(new Intent(getApplicationContext(),GooglePayActivity.class));
                        SharedPreferences preferences= getSharedPreferences(Constants.PREFERENCE_NAME,MODE_PRIVATE);
                        boolean isSuccess = preferences.getBoolean(Constants.KEY_ISPAYMENTSUCCESS,false);
                        if(!isSuccess){
                            Toast.makeText(getApplicationContext(),"PAYMENT NOT SUCCESS",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Thanks For Payment",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(CartActivity.this, responseBody.getMassage(), Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.cancel();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PlaceOrderRestore> call, Throwable t) {
                alertDialog.cancel();
            }
        });
    }

}