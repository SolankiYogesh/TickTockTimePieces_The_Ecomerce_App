package com.syd.ticktocktimepieces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.syd.ticktocktimepieces.models.OrderResponseModel;
import com.syd.ticktocktimepieces.models.OrdersModel;
import com.syd.ticktocktimepieces.network.NetworkClient;
import com.syd.ticktocktimepieces.network.NetworkServices;
import com.syd.ticktocktimepieces.utils.Constants;

import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyOrdersActivity extends AppCompatActivity {
    ImageView imageback;
    RecyclerView OrderRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        imageback = (ImageView) findViewById(R.id.image_menu);
        imageback.setOnClickListener(v -> onBackPressed());
        OrderRecyclerView = findViewById(R.id.orders_recycler_view);
        OrderRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getorders();
    }
    private void getorders(){
        AlertDialog alertDialog = new SpotsDialog(MyOrdersActivity.this,R.style.Custom2);
        alertDialog.show();
        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
            NetworkServices services= NetworkClient.getClient().create(NetworkServices.class);
            Call<OrderResponseModel> getorderscall=services.getOrders(preferences.getString(Constants.KEY_EMAIL,"N/A"));
            getorderscall.enqueue(new Callback<OrderResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<OrderResponseModel> call, @NonNull Response<OrderResponseModel> response) {
                    OrderResponseModel orderResponse=response.body();
                    if (orderResponse!=null){
                        OrderRecyclerView.setAdapter(new OrderAdapterr(orderResponse.getOrders()));
                    }
                    alertDialog.cancel();

                }

                @Override
                public void onFailure(Call<OrderResponseModel> call, Throwable t) {
                    alertDialog.cancel();
                }
            });


    }
    private class OrderAdapterr extends RecyclerView.Adapter<OrderViewHolder>{
        List<OrdersModel> orders;
        OrderAdapterr(List<OrdersModel> orders){
            this.orders=orders;
        }
        @NonNull
        @Override
        public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new OrderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_item_container, parent, false));
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
            holder.textid.setText("Order Id :"+orders.get(holder.getAdapterPosition()).getId());
            holder.textdate.setText("Order Date :"+orders.get(holder.getAdapterPosition()).getOrder_date());
            holder.textprice.setText("Order Total Prices :"+"\u20B9"+orders.get(holder.getAdapterPosition()).getTotal_ammount());

            holder.layoutorder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(MyOrdersActivity.this,OrderDetailActivity.class);
                    intent.putExtra("products",orders.get(holder.getAdapterPosition()).getProducts());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return orders.size();
        }
    }
    private static class OrderViewHolder extends RecyclerView.ViewHolder{
            TextView textid,textdate,textprice;
            CardView layoutorder;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutorder=itemView.findViewById(R.id.order_layout);
            textid = itemView.findViewById(R.id.text_order_id);
            textdate = itemView.findViewById(R.id.text_order_date);
            textprice = itemView.findViewById(R.id.text_order_total);
        }
    }
}